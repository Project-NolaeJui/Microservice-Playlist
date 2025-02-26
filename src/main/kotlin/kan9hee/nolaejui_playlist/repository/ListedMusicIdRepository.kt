package kan9hee.nolaejui_playlist.repository

import kan9hee.nolaejui_playlist.entity.PlaylistMusicIds
import org.springframework.data.jpa.repository.JpaRepository

interface ListedMusicIdRepository: JpaRepository<PlaylistMusicIds, Long> {
    fun findByMusicIdIn(names: List<Long>):List<PlaylistMusicIds>
}