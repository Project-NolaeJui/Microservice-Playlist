package kan9hee.nolaejui_playlist.controller

import kan9hee.nolaejui_playlist.dto.DetailMusicDto
import kan9hee.nolaejui_playlist.dto.requestOnly.*
import kan9hee.nolaejui_playlist.service.DataService
import kan9hee.nolaejui_playlist.service.ExternalService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/playlist/playlist")
class PlayListController(private val dataService: DataService,
                         private val externalService: ExternalService) {

    @GetMapping("/getPlaylistMusicIds")
    suspend fun getPlaylistMusicIds(
        @RequestParam(value = "userName") userName:String,
        @RequestParam(value = "playlistName") playlistName: String,
        @RequestParam(value = "page") page: Int
    ): List<DetailMusicDto> {
        val playlistInfo = PlaylistReadDto(userName,playlistName,page)
        return dataService.getPlaylistMusics(playlistInfo)
    }

    @PostMapping("/addNewPlaylist")
    suspend fun addNewPlaylist(@RequestBody playlistCreateDto: PlaylistCdDto) {
        dataService.addNewPlaylist(playlistCreateDto)
    }

    @PostMapping("/changePlaylistTitleName")
    suspend fun changePlaylistTitleName(@RequestBody playlistUpdateDto: PlaylistUpdateDto) {
        dataService.updatePlaylistTitleName(playlistUpdateDto)
    }

    @PostMapping("/pickupMusics")
    suspend fun pickupMusics(@RequestBody locationDto: LocationDto) {
        externalService.pickupMusics(locationDto)
    }

    @PostMapping("/removeMusicFromPlaylist")
    suspend fun removeMusicFromPlaylist(@RequestBody musicToPlaylistDto: MusicToPlaylistDto) {
        dataService.removeMusicIdFromPlaylist(musicToPlaylistDto)
    }

    @PostMapping("/deletePlaylist")
    suspend fun deletePlaylist(@RequestBody playlistDeleteDto: PlaylistCdDto) {
        dataService.deletePlaylist(playlistDeleteDto)
    }
}