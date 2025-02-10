package kan9hee.nolaejui_playlist.controller

import kan9hee.nolaejui_playlist.dto.DetailMusicDto
import kan9hee.nolaejui_playlist.dto.SearchOptionDto
import kan9hee.nolaejui_playlist.dto.requestOnly.MusicDataInputDto
import kan9hee.nolaejui_playlist.dto.requestOnly.MusicDeleteDto
import kan9hee.nolaejui_playlist.dto.requestOnly.ReportProblemDto
import kan9hee.nolaejui_playlist.service.AwsService
import kan9hee.nolaejui_playlist.service.DataService
import kan9hee.nolaejui_playlist.service.ExternalService
import kan9hee.nolaejui_playlist.service.SearchService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/playlist/api/music")
class MusicController(private val dataService: DataService,
                      private val externalService: ExternalService,
                      private val awsService: AwsService,
                      private val searchService: SearchService) {

    @GetMapping("/getMusic")
    fun getMusic(@RequestParam(value = "musicId") musicId:Long): DetailMusicDto {
        return dataService.getMusic(musicId)
    }

    @GetMapping("/searchMusic")
    fun searchMusic(
        @RequestParam(value = "musicTitle", required = false) musicTitle: String?,
        @RequestParam(value = "artist", required = false) artist: String?,
        @RequestParam(value = "dataType", required = false) dataType: String?,
        @RequestParam(value = "tags", required = false) tags: List<String>?,
        @RequestParam(value = "page") page: Int,
        @RequestParam(value = "ids", required = false) ids: List<Long>?
    ): List<DetailMusicDto> {
        val searchOption = SearchOptionDto(ids, musicTitle, artist, dataType, tags, page)
        return searchService.searchMusicByOverall(searchOption)
    }

    @GetMapping("/getPreSignedS3Url")
    fun getPreSignedS3Url(@RequestParam(value = "fileName") fileName:String): String {
        return awsService.getPreSignedUrl(fileName)
    }

    @PostMapping("/registerMusic")
    suspend fun registerMusic(@RequestBody musicDataInputDto: MusicDataInputDto) {
        return dataService.createUsersMusic(musicDataInputDto)
    }

    @PostMapping("/changeMusicInfo")
    suspend fun changeMyMusicInfo(@RequestBody musicDataInputDto: MusicDataInputDto) {
        dataService.changeUsersMusic(musicDataInputDto)
    }

    @PostMapping("/deleteMyMusic")
    suspend fun deleteMyMusic(@RequestBody musicDeleteDto: MusicDeleteDto){
        dataService.deleteUsersMusic(musicDeleteDto)
    }

    @PostMapping("/reportMusicProblem")
    suspend fun reportMusicProblem(@RequestBody reportProblemDto: ReportProblemDto){
        externalService.reportMusicProblem(reportProblemDto)
    }

}