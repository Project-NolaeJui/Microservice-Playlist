package kan9hee.nolaejui_playlist.service

import jakarta.transaction.Transactional
import kan9hee.nolaejui_playlist.dto.DetailMusicDto
import kan9hee.nolaejui_playlist.dto.PlaylistSummaryDto
import kan9hee.nolaejui_playlist.dto.SearchOptionDto
import kan9hee.nolaejui_playlist.dto.requestOnly.*
import kan9hee.nolaejui_playlist.entity.*
import kan9hee.nolaejui_playlist.repository.*
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DataService(private val playlistRepository: PlaylistRepository,
                  private val playlistRepositoryCustomImpl:PlaylistRepositoryCustomImpl,
                  private val musicRepository: MusicRepository,
                  private val tagRepository: TagRepository,
                  private val awsService: AwsService,
                  private val searchService: SearchService) {

    @Transactional
    suspend fun createUsersMusic(userId:String,createInfo: MusicDataInputDto) {
        val music = Music(
            createInfo.musicTitle,
            createInfo.artist,
            createInfo.dataType,
            createInfo.dataUrl,
            true,
            userId,
            LocalDateTime.now()
        )
        addMusicAndTagRelation(createInfo.tags,music)
        musicRepository.save(music)
    }

    @Transactional
    fun addMusicAndTagRelation(tags:List<String>,music:Music){
        val existingTags = tagRepository.findByNameIn(tags).associateBy { it.name }
        val newTags = tags.filter { it !in existingTags }
            .map { Tag(it) }
            .let { tagRepository.saveAll(it) }
            .associateBy{ it.name }
        val allTags = existingTags + newTags

        val relations = allTags.values.map { tag -> MusicTagRelation(music,tag) }
        music.musicTagRelations.addAll(relations)
    }

    @Transactional
    fun getMusic(musicId:Long):DetailMusicDto{
        val music = findMusicEntity(musicId)

        return DetailMusicDto(
            music.id,
            music.musicTitle,
            music.artist,
            music.musicTagRelations.map { tagRelation -> tagRelation.tag.name },
            music.dataType,
            music.dataUrl,
            music.isPlayable,
            music.uploader,
            music.uploadDate
        )
    }

    @Transactional
    fun findMusicEntity(musicId:Long):Music{
        return musicRepository.findById(musicId)
            .orElseThrow {
                IllegalArgumentException("$musicId 에 해당하는 음원이 없습니다.")
            }
    }

    @Transactional
    suspend fun changeUsersMusic(userId:String,changeInfo: MusicDataInputDto){
        val music = changeInfo.id?.let { findMusicEntity(it) }
        music?.let {
            if(userId!=music.uploader)
                throw RuntimeException("음원의 소유주가 아닙니다.")

            music.musicTitle = changeInfo.musicTitle
            music.artist = changeInfo.artist
            music.dataType = changeInfo.dataType
            music.dataUrl = changeInfo.dataUrl
            music.isPlayable = changeInfo.isPlayable

            music.musicTagRelations.clear()
            addMusicAndTagRelation(changeInfo.tags,music)
            musicRepository.save(music)
        }
    }

    @Transactional
    suspend fun deleteUsersMusic(userId:String,deleteDto:MusicDeleteDto){
        val music = findMusicEntity(deleteDto.id)
        if(userId!=music.uploader)
            throw RuntimeException("음원의 소유주가 아닙니다.")

        musicRepository.deleteById(music.id)
        awsService.deleteMusicS3(music.musicTitle)
    }

    @Transactional
    fun getPlaylist(userName:String): List<PlaylistSummaryDto> {
        return playlistRepositoryCustomImpl.getPlaylistsByOwner(userName)
            ?:throw RuntimeException("플레이리스트 데이터에 문제가 발생했습니다. 관리자에게 문의 바랍니다.")
    }

    @Transactional
    suspend fun addNewPlaylist(userId:String,playlistCreateDto: PlaylistCdDto){
        playlistRepository.save(Playlist(
            playlistCreateDto.playlistName,
            userId,
            false))
    }

    @Transactional
    suspend fun updatePlaylistTitleName(userId:String,playlistUpdateDto: PlaylistUpdateDto){
        val playlist = getPlaylistEntity(playlistUpdateDto.basicPlaylistName,userId)
        if(userId != playlist.playListOwner)
            throw IllegalArgumentException("플레이리스트 소유주가 아닙니다.")

        playlist.playListTitle = playlistUpdateDto.newPlaylistName
    }

    @Transactional
    suspend fun getPlaylistMusics(playlistReadDto: PlaylistReadDto): List<DetailMusicDto> {
        val playlist = getPlaylistEntity(playlistReadDto.playlistName, playlistReadDto.userName)
        if(playlistReadDto.userName != playlist.playListOwner)
            throw IllegalArgumentException("플레이리스트 소유주가 아닙니다.")

        val ids = playlist.playlistMusicIds.map { it.musicId }

        return searchService.searchMusicByOverall(SearchOptionDto(
            ids,
            null,
            null,
            null,
            null,
            playlistReadDto.page))
    }

    @Transactional
    suspend fun addMusicIdsToPlaylist(playlistName:String, userName:String, musicsIds: MutableList<Long>){
        val playlist = getPlaylistEntity(playlistName,userName)

        val existingMusicIdsInPlaylist = playlist.playlistMusicIds.map { it.musicId }.toSet()
        val newMusicIds = musicsIds.filterNot { it in existingMusicIdsInPlaylist }
        val newPlaylistMusicIds = newMusicIds.map { PlaylistMusicIds(it) }

        playlist.playlistMusicIds.addAll(newPlaylistMusicIds)
        playlistRepository.save(playlist)
    }

    @Transactional
    suspend fun removeMusicIdFromPlaylist(userId:String,musicToPlaylistDto: MusicToPlaylistDto){
        val playlist = getPlaylistEntity(musicToPlaylistDto.playlistName,userId)
        val musicIdToRemove = playlist.playlistMusicIds.find { it.musicId == musicToPlaylistDto.musicId }
            ?: throw IllegalArgumentException("플레이리스트에 ${musicToPlaylistDto.musicId} 정보가 없습니다.")

        if(userId != playlist.playListOwner)
            throw IllegalArgumentException("플레이리스트 소유주가 아닙니다.")

        playlist.playlistMusicIds.remove(musicIdToRemove)
    }

    @Transactional
    suspend fun deletePlaylist(userId:String,playlistDeleteDto:PlaylistCdDto){
        if(playlistDeleteDto.playlistName == "pickup")
            throw IllegalArgumentException("픽업용 플레이리스트는 삭제할 수 없습니다.")

        val playlist = getPlaylistEntity(playlistDeleteDto.playlistName,userId)
        if(userId != playlist.playListOwner)
            throw IllegalArgumentException("플레이리스트 소유주가 아닙니다.")

        playlistRepository.deleteByPlayListTitleAndPlayListOwner(playlistDeleteDto.playlistName,userId)
    }

    @Transactional
    fun createDefaultUserPlaylist(requestUsername: String){
        playlistRepository.save(Playlist(
            "pickup",
            requestUsername,
            true))
    }

    @Transactional
    fun deleteUsersAllPlaylist(requestUsername: String){
        playlistRepository.deleteByPlayListOwner(requestUsername)
    }

    @Transactional
    private fun getPlaylistEntity(playlistName: String, playlistOwner: String): Playlist {
        val result = playlistRepositoryCustomImpl.getMusicsByTitleAndOwner(playlistName, playlistOwner)
            ?: throw IllegalArgumentException("$playlistOwner 의 $playlistName 에 따른 정보가 없습니다.")
        return result
    }
}