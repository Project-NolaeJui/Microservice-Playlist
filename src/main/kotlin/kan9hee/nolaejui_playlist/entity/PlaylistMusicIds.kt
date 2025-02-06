package kan9hee.nolaejui_playlist.entity

import jakarta.persistence.*

@Entity
@Table(name = "playlist_music_ids")
class PlaylistMusicIds(@Column(nullable = false)
                       val musicId: Long) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}