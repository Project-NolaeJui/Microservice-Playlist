package kan9hee.nolaejui_playlist.repository

import com.linecorp.kotlinjdsl.querydsl.from.fetch
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import jakarta.persistence.criteria.JoinType
import kan9hee.nolaejui_playlist.entity.Playlist
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlaylistRepository:JpaRepository<Playlist,Long> {
    fun deleteByPlayListTitleAndPlayListOwner(playListTitle: String, playlistOwner: String)
    fun deleteByPlayListOwner(playlistOwner: String)
}

interface PlaylistRepositoryCustom{
    fun findWithMusicIdsByTitleAndOwner(playlistName: String, playlistOwner: String): Playlist?
}

@Repository
class PlaylistRepositoryCustomImpl(private val queryFactory: SpringDataQueryFactory)
    :PlaylistRepositoryCustom{
    override fun findWithMusicIdsByTitleAndOwner(playlistName: String, playlistOwner: String): Playlist? {
        return queryFactory.listQuery<Playlist> {
            select(entity(Playlist::class))
            from(entity(Playlist::class))
            fetch(Playlist::playlistMusicIds,JoinType.LEFT)
            whereAnd(
                column(entity(Playlist::class),Playlist::playListTitle).equal(playlistName),
                column(entity(Playlist::class),Playlist::playListOwner).equal(playlistOwner)
            )
        }.singleOrNull()
    }
}