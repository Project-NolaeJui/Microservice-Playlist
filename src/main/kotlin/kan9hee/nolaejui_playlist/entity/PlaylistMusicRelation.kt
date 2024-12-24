package kan9hee.nolaejui_playlist.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.NoArgsConstructor

@Getter
@Entity
@NoArgsConstructor
@Table(name = "playlist_music_relation")
class PlaylistMusicRelation(@ManyToOne
                            @JoinColumn(name = "playlist_id", nullable = false)
                            val playlist: Playlist,

                            @ManyToOne
                            @JoinColumn(name = "music_id", nullable = false)
                            val music: Music) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}