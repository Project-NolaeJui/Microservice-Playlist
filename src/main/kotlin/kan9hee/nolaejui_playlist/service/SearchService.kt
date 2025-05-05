package kan9hee.nolaejui_playlist.service

import co.elastic.clients.elasticsearch._types.FieldValue
import co.elastic.clients.elasticsearch._types.query_dsl.Query
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kan9hee.nolaejui_playlist.dto.DetailMusicDto
import kan9hee.nolaejui_playlist.dto.SearchOptionDto
import kan9hee.nolaejui_playlist.entity.elasticSearch.MusicSearch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.elasticsearch.client.elc.NativeQuery
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class SearchService(@Autowired
                    private val objectMapper: ObjectMapper,
                    private val elasticsearchOperations:ElasticsearchOperations,
                    private val redisTemplate: StringRedisTemplate) {

    fun searchMusicByOverall(searchOptionDto: SearchOptionDto): List<DetailMusicDto> {

        val cacheKey = "music_search:" + searchOptionDto.toString().hashCode()
        val cachedResult = redisTemplate.opsForValue().get(cacheKey)
        cachedResult?.let {
            return jacksonObjectMapper().readValue(cachedResult)
        }

        val mustQueries = mutableListOf<Query>()

        searchOptionDto.ids?.let { ids ->
            val fieldValues = ids.map { FieldValue.of(it) }
            mustQueries.add(Query.of { q -> q.terms { t -> t.field("id").terms { v -> v.value(fieldValues) } } })
        }
        searchOptionDto.musicTitle?.let {
            mustQueries.add(Query.of { q -> q.match { m -> m.field("music_title").query(it) } })
        }
        searchOptionDto.artist?.let {
            mustQueries.add(Query.of { q -> q.fuzzy { f -> f.field("artist").value(it).fuzziness("AUTO") } })
        }
        searchOptionDto.dataType?.let { dataType ->
            mustQueries.add(Query.of { q -> q.term { t -> t.field("data_type").value(dataType) } })
        }
        searchOptionDto.tags?.let { tags ->
            mustQueries.add(Query.of { q -> q.match { m -> m.field("tags").query(tags.joinToString(" ")) } })
        }
        mustQueries.add(Query.of { q -> q.term { t -> t.field("is_playable").value(true) } })

        val mustQuery = Query.of { q -> q.bool { b -> b.must(mustQueries) } }

        val searchQuery = NativeQuery.builder()
            .withQuery(mustQuery)
            .build()

        val searchResult = elasticsearchOperations
            .search(searchQuery, MusicSearch::class.java)
            .map { it.content }

        val allResults = searchResult.map {
            DetailMusicDto(
                it.id,
                it.music_title,
                it.artist,
                it.tags,
                it.data_type,
                it.data_url,
                it.is_playable,
                it.uploader,
                it.upload_date
            )
        }
        val distinctResults = allResults.distinctBy { it.id }

        val start = searchOptionDto.page * 30
        val end = ((searchOptionDto.page + 1) * 30) - 1
        val paginatedResults = distinctResults.subList(start, end.coerceAtMost(distinctResults.size))

        redisTemplate.opsForValue().set(
            cacheKey,
            objectMapper.writeValueAsString(paginatedResults),
            5,
            TimeUnit.MINUTES
        )

        return paginatedResults
    }
}