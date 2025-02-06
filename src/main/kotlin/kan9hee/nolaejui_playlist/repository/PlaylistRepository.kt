package kan9hee.nolaejui_playlist.repository

import kan9hee.nolaejui_playlist.entity.Playlist
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlaylistRepository:JpaRepository<Playlist,Long> {
    fun findByPlayListTitleAndPlayListOwner(playListTitle: String, playlistOwner: String):Playlist?
    fun deleteByPlayListTitleAndPlayListOwner(playListTitle: String, playlistOwner: String)
    fun deleteByPlayListOwner(playlistOwner: String)
}