package kan9hee.nolaejui_playlist.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "music")
class Music(@Column(nullable = false)
            var musicTitle:String,
            @Column(nullable = false)
            var artist:String,
            @Column(nullable = false)
            var dataType:String,
            @Column
            var dataUrl:String?,
            @Column(nullable = false)
            var isPlayable:Boolean,
            @Column(nullable = false)
            var uploader:String,
            @Column(nullable = false)
            var uploadDate:LocalDate) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long = 0

    @OneToMany(mappedBy = "music", cascade = [CascadeType.ALL], orphanRemoval = true)
    val musicTagRelations: MutableList<MusicTagRelation> = mutableListOf()
}