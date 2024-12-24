package kan9hee.nolaejui_playlist.repository

import kan9hee.nolaejui_playlist.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository:JpaRepository<Tag,Long> {
    fun findByName(name: String):Tag?
}