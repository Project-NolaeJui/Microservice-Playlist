package kan9hee.nolaejui_playlist.service

import co.elastic.clients.elasticsearch._types.FieldValue
import co.elastic.clients.elasticsearch._types.query_dsl.Query
import kan9hee.nolaejui_playlist.dto.DetailMusicDto
import kan9hee.nolaejui_playlist.dto.SearchOptionDto
import kan9hee.nolaejui_playlist.entity.elasticSearch.MusicSearch
import org.springframework.data.domain.PageRequest
import org.springframework.data.elasticsearch.client.elc.NativeQuery
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.stereotype.Service

@Service
class SearchService(private val elasticsearchOperations:ElasticsearchOperations) {

    fun searchMusicByOverall(searchOptionDto: SearchOptionDto): List<DetailMusicDto> {
        val mustQueries = mutableListOf<Query>()

        searchOptionDto.ids?.let { ids ->
            val fieldValues = ids.map { FieldValue.of(it) }
            mustQueries.add(Query.of { q -> q.terms { t -> t.field("id").terms { v -> v.value(fieldValues) } } })
        }
        searchOptionDto.musicTitle?.let {
            mustQueries.add(Query.of { q -> q.fuzzy { f -> f.field("musicTitle").value(it).fuzziness("AUTO") } })
        }
        searchOptionDto.artist?.let {
            mustQueries.add(Query.of { q -> q.fuzzy { f -> f.field("artist").value(it).fuzziness("AUTO") } })
        }
        searchOptionDto.dataType?.let { dataType ->
            val fieldValues = dataType.map { FieldValue.of(it) }
            mustQueries.add(Query.of { q -> q.terms { t -> t.field("dataType").terms { v -> v.value(fieldValues) } } })
        }
        searchOptionDto.tags?.let { tags ->
            val fieldValues = tags.map { FieldValue.of(it) }
            mustQueries.add(Query.of { q -> q.terms { t -> t.field("tags").terms { v -> v.value(fieldValues) } } })
        }

        val mustQuery = Query.of { q -> q.bool { b -> b.must(mustQueries) } }
        val pageable = PageRequest.of(searchOptionDto.page,30)

        val searchQuery = NativeQuery.builder()
            .withQuery(mustQuery)
            .withPageable(pageable)
            .build()

        val searchResult = elasticsearchOperations
            .search(searchQuery, MusicSearch::class.java)
            .map { it.content }

        val resultList = searchResult.map {
            DetailMusicDto(
                it.id,
                it.musicTitle,
                it.artist,
                it.tags,
                it.dataType,
                it.dataUrl,
                it.isPlayable,
                it.uploader,
                it.uploadDate
            )
        }.toList()

        return resultList
    }
}