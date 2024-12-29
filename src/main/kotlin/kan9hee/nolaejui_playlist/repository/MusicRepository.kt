package kan9hee.nolaejui_playlist.repository

import kan9hee.nolaejui_playlist.entity.Music
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MusicRepository:JpaRepository<Music,Long>{
}