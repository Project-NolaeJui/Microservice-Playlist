package kan9hee.nolaejui_playlist.service

import kan9hee.nolaejui_playlist.dto.SearchOptionDto
import kan9hee.nolaejui_playlist.dto.SummaryMusicDto
import kan9hee.nolaejui_playlist.repository.elasticSearch.MusicSearchRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service


@Service
class SearchService(private val musicSearchRepository: MusicSearchRepository) {

    fun searchMusicByOverall(searchOptionDto: SearchOptionDto): List<SummaryMusicDto> {
        val searchResult = musicSearchRepository.searchMusicByMultiOption(
            searchOptionDto.ids,
            searchOptionDto.musicTitle,
            searchOptionDto.artist,
            searchOptionDto.dataType,
            searchOptionDto.tags,
            PageRequest.of(searchOptionDto.page,30)
        )

        return searchResult.map {
            SummaryMusicDto(
                it.id,
                it.musicTitle,
                it.artist
            )
        }
    }
}