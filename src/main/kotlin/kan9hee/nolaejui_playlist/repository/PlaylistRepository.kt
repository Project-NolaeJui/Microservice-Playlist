package kan9hee.nolaejui_playlist.repository

import kan9hee.nolaejui_playlist.entity.Playlist
import org.springframework.data.jpa.repository.JpaRepository

interface PlaylistRepository:JpaRepository<Playlist,Long> {
    fun findByPlaylistNameAndPlaylistOwner(playlistName: String, playlistOwner: String):Playlist
    fun deleteByPlaylistNameAndPlaylistOwner(playlistName: String, playlistOwner: String)
}