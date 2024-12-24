package kan9hee.nolaejui_playlist.repository

import kan9hee.nolaejui_playlist.entity.Music
import org.springframework.data.jpa.repository.JpaRepository

interface MusicRepository:JpaRepository<Music,Long>{
}