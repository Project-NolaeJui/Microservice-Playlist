package kan9hee.nolaejui_playlist.controller

import kan9hee.nolaejui_playlist.dto.DetailMusicDto
import kan9hee.nolaejui_playlist.dto.SearchOptionDto
import kan9hee.nolaejui_playlist.dto.SummaryMusicDto
import kan9hee.nolaejui_playlist.dto.requestOnly.MusicDataInputDto
import kan9hee.nolaejui_playlist.dto.requestOnly.MusicDeleteDto
import kan9hee.nolaejui_playlist.dto.requestOnly.PlayLogDto
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
    fun searchMusic(@RequestParam(value = "searchOption") searchOption: SearchOptionDto): List<SummaryMusicDto> {
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

    @PostMapping("/addMusicPlayLog")
    suspend fun addMusicPlayLog(@RequestBody playLogDto: PlayLogDto) {
        externalService.addMusicPlayLog(playLogDto)
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