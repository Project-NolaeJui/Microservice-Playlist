package kan9hee.nolaejui_playlist.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.NoArgsConstructor

@Getter
@Entity
@NoArgsConstructor
@Table(name = "playlist")
class Playlist(@Column(nullable = false)
               var playListTitle:String,
               @Column(nullable = false)
               val playListOwner:String,
               @Column(nullable = false)
               val isDefaultStorage:Boolean) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long = 0

    @OneToMany(mappedBy = "playlist", cascade = [CascadeType.ALL], orphanRemoval = true)
    val playlistMusicRelations:MutableList<PlaylistMusicRelation> = mutableListOf()
}