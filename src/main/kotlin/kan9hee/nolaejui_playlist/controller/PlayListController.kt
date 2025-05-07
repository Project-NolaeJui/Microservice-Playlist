package kan9hee.nolaejui_playlist.controller

import kan9hee.nolaejui_playlist.dto.DetailMusicDto
import kan9hee.nolaejui_playlist.dto.PlaylistSummaryDto
import kan9hee.nolaejui_playlist.dto.requestOnly.*
import kan9hee.nolaejui_playlist.service.DataService
import kan9hee.nolaejui_playlist.service.ExternalService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/playlist/playlist")
class PlayListController(private val dataService: DataService,
                         private val externalService: ExternalService) {

    @GetMapping("/getUserPlaylists")
    fun getUserPlaylists(@RequestHeader("X-User-Id") userId:String): List<PlaylistSummaryDto> {
        return dataService.getPlaylist(userId)
    }

    @GetMapping("/getPlaylistMusics")
    suspend fun getPlaylistMusics(
        @RequestHeader("X-User-Id") userId:String,
        @RequestParam(value = "playlistName") playlistName: String,
        @RequestParam(value = "page") page: Int
    ): List<DetailMusicDto> {
        val playlistInfo = PlaylistReadDto(userId,playlistName,page)
        return dataService.getPlaylistMusics(playlistInfo)
    }

    @PostMapping("/addNewPlaylist")
    suspend fun addNewPlaylist(
        @RequestHeader("X-User-Id") userId:String,
        @RequestBody playlistCreateDto: PlaylistCdDto
    ) {
        dataService.addNewPlaylist(userId,playlistCreateDto)
    }

    @PostMapping("/changePlaylistTitleName")
    suspend fun changePlaylistTitleName(
        @RequestHeader("X-User-Id") userId:String,
        @RequestBody playlistUpdateDto: PlaylistUpdateDto
    ) {
        dataService.updatePlaylistTitleName(userId,playlistUpdateDto)
    }

    @PostMapping("/addMusicToPlaylist")
    suspend fun addMusicToPlaylist(
        @RequestHeader("X-User-Id") userId:String,
        @RequestBody musicToPlaylistDto: MusicToPlaylistDto
    ){
        dataService.addMusicIdsToPlaylist(
            musicToPlaylistDto.playlistName,
            userId,
            mutableListOf(musicToPlaylistDto.musicId)
        )
    }

    @PostMapping("/pickupMusics")
    suspend fun pickupMusics(
        @RequestHeader("X-User-Id") userId:String,
        @RequestBody locationDto: LocationDto
    ) {
        externalService.pickupMusics(userId,locationDto)
    }

    @PostMapping("/removeMusicFromPlaylist")
    suspend fun removeMusicFromPlaylist(
        @RequestHeader("X-User-Id") userId:String,
        @RequestBody musicToPlaylistDto: MusicToPlaylistDto
    ) {
        dataService.removeMusicIdFromPlaylist(userId,musicToPlaylistDto)
    }

    @PostMapping("/deletePlaylist")
    suspend fun deletePlaylist(
        @RequestHeader("X-User-Id") userId:String,
        @RequestBody playlistDeleteDto: PlaylistCdDto) {
        dataService.deletePlaylist(userId,playlistDeleteDto)
    }
}