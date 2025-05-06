package kan9hee.nolaejui_playlist.repository

import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.querydsl.expression.count
import com.linecorp.kotlinjdsl.querydsl.from.fetch
import com.linecorp.kotlinjdsl.querydsl.from.join
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import jakarta.persistence.criteria.JoinType
import kan9hee.nolaejui_playlist.dto.PlaylistSummaryDto
import kan9hee.nolaejui_playlist.entity.Playlist
import kan9hee.nolaejui_playlist.entity.PlaylistMusicIds
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlaylistRepository:JpaRepository<Playlist,Long> {
    fun deleteByPlayListTitleAndPlayListOwner(playListTitle: String, playlistOwner: String)
    fun deleteByPlayListOwner(playlistOwner: String)
}

interface PlaylistRepositoryCustom{
    fun getPlaylistsByOwner(playlistOwner: String): List<PlaylistSummaryDto>?
    fun getMusicsByTitleAndOwner(playlistName: String, playlistOwner: String): Playlist?
}

@Repository
class PlaylistRepositoryCustomImpl(private val queryFactory: SpringDataQueryFactory)
    :PlaylistRepositoryCustom{
    override fun getPlaylistsByOwner(playlistOwner: String): List<PlaylistSummaryDto>? {
        return queryFactory.listQuery<PlaylistSummaryDto> {
            selectMulti(
                column(Playlist::playListTitle),
                count(PlaylistMusicIds::id)
            )
            from(entity(Playlist::class))
            join(Playlist::playlistMusicIds, JoinType.LEFT)
            where(column(Playlist::playListOwner).equal(playlistOwner))
            groupBy(column(Playlist::playListTitle))
        }
    }

    override fun getMusicsByTitleAndOwner(playlistName: String, playlistOwner: String): Playlist? {
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