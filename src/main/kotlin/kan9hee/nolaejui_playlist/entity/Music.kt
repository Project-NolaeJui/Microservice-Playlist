package kan9hee.nolaejui_playlist.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.NoArgsConstructor
import java.time.LocalDate

@Getter
@Entity
@NoArgsConstructor
@Table(name = "music")
class Music(@Column(nullable = false)
            val musicTitle:String,
            @Column(nullable = false)
            val dataType:String,
            @Column
            var dataUrl:String?,
            @Column(nullable = false)
            var isPlayable:Boolean,
            @Column(nullable = false)
            val uploader:String,
            @Column(nullable = false)
            val uploadDate:LocalDate) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long = 0

    @OneToMany(mappedBy = "music", cascade = [CascadeType.ALL], orphanRemoval = true)
    val playlistMusicRelations: MutableList<PlaylistMusicRelation> = mutableListOf()
    @OneToMany(mappedBy = "music", cascade = [CascadeType.ALL], orphanRemoval = true)
    val musicTagRelations: MutableList<MusicTagRelation> = mutableListOf()
}