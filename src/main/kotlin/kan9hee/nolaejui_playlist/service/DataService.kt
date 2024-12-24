package kan9hee.nolaejui_playlist.service

import jakarta.transaction.Transactional
import kan9hee.nolaejui_playlist.dto.MusicDto
import kan9hee.nolaejui_playlist.dto.createOnly.MusicCreateDto
import kan9hee.nolaejui_playlist.entity.*
import kan9hee.nolaejui_playlist.repository.MusicRepository
import kan9hee.nolaejui_playlist.repository.PlaylistRepository
import kan9hee.nolaejui_playlist.repository.TagRepository
import org.springframework.stereotype.Service

@Service
class DataService(private val playlistRepository: PlaylistRepository,
                  private val musicRepository: MusicRepository,
                  private val tagRepository: TagRepository) {

    @Transactional
    fun createNewMusic(createInfo: MusicCreateDto){
        val music = Music(
            createInfo.musicTitle,
            createInfo.dataType,
            createInfo.dataUrl,
            true,
            createInfo.uploader,
            createInfo.uploadDate
        )

        createInfo.tags.forEach{ tagName ->
            val tag = tagRepository.findByName(tagName) ?: tagRepository.save(Tag(tagName))
            val relation = MusicTagRelation(music,tag)
            music.musicTagRelations.add(relation)
            tag.musicTagRelations.add(relation)
        }

        musicRepository.save(music)
    }

    @Transactional
    fun getMusic(musicId:Long){
        musicRepository.findById(musicId).map {
            MusicDto(
                id = it.id,
                musicTitle = it.musicTitle,
                tags = it.musicTagRelations.map { tagRelation -> tagRelation.tag.name },
                dataType = it.dataType,
                dataUrl = it.dataUrl,
                isPlayable = it.isPlayable,
                uploadDate = it.uploadDate
            )
        }
    }

    @Transactional
    fun deactivateMusic(musicId:Long){
        val music = musicRepository.findById(musicId).orElseThrow {
            IllegalArgumentException("$musicId 에 해당하는 음원이 없습니다.")
        }
        music.dataUrl = null
        music.isPlayable = false
    }

    @Transactional
    fun deleteMusic(musicId:Long){
        musicRepository.deleteById(musicId)
    }

    @Transactional
    fun addNewPlaylist(playlistName:String,playlistOwner:String){
        playlistRepository.save(Playlist(playlistName,playlistOwner,false))
    }

    @Transactional
    fun updatePlaylistTitleName(playlistName:String,playlistOwner:String,newTitle:String){
        val playlist = getPlaylistEntity(playlistName,playlistOwner)
        playlist.playListTitle = newTitle
    }

    @Transactional
    fun getPlaylist(playlistName:String,playlistOwner:String): List<MusicDto> {
        val playlist = getPlaylistEntity(playlistName, playlistOwner)

        return playlist.playlistMusicRelations.map {
            MusicDto(
                id = it.music.id,
                musicTitle = it.music.musicTitle,
                tags = it.music.musicTagRelations.map { tagRelation -> tagRelation.tag.name },
                dataType = it.music.dataType,
                dataUrl = it.music.dataUrl,
                isPlayable = it.music.isPlayable,
                uploadDate = it.music.uploadDate
            )
        }
    }

    @Transactional
    fun addMusicFromPlaylistById(playlistName:String,playlistOwner:String,musicId:Long){
        val playlist = getPlaylistEntity(playlistName,playlistOwner)
        val music = musicRepository.findById(musicId).orElseThrow {
            IllegalArgumentException("$musicId 에 해당하는 음원이 없습니다.")
        }

        val relation = PlaylistMusicRelation(playlist,music)
        playlist.playlistMusicRelations.add(relation)
        music.playlistMusicRelations.add(relation)
    }

    @Transactional
    fun removeMusicFromPlaylistById(playlistName:String,playlistOwner:String,musicId:Long){
        val playlist = getPlaylistEntity(playlistName,playlistOwner)
        val relationToRemove = playlist.playlistMusicRelations.firstOrNull { it.music.id == musicId }
        relationToRemove?.let {
            playlist.playlistMusicRelations.remove(it)
        }
    }

    @Transactional
    fun deletePlaylist(playlistName:String,playlistOwner:String){
        playlistRepository.deleteByPlaylistNameAndPlaylistOwner(playlistName,playlistOwner)
    }

    @Transactional
    private fun getPlaylistEntity(playlistName:String,playlistOwner:String): Playlist {
        return playlistRepository.findByPlaylistNameAndPlaylistOwner(playlistName, playlistOwner)
    }
}