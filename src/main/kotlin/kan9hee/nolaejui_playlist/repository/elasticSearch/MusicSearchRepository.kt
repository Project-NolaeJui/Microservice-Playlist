package kan9hee.nolaejui_playlist.repository.elasticSearch

import kan9hee.nolaejui_playlist.entity.elasticSearch.MusicSearch
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface MusicSearchRepository: ElasticsearchRepository<MusicSearch,Long> {

    @Query("""
        {
          "bool": {
            "must": [
              { "terms": { "id": ?0 } },
              { "fuzzy": { "musicTitle": { "value": ?1, "fuzziness": "AUTO" } } },
              { "fuzzy": { "artist": { "value": ?2, "fuzziness": "AUTO" } } },
              { "match": { "dataType": ?3 } },
              { "terms": { "tags": ?4 } }
            ]
          }
        }
    """)
    fun searchMusicByMultiOption(ids: List<Long>?,
                                 musicTitle: String?,
                                 artist: String?,
                                 dataType: String?,
                                 tags: List<String>?,
                                 pageable: Pageable
    ): List<MusicSearch>
}