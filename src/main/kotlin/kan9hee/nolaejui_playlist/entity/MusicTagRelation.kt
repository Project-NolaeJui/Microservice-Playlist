package kan9hee.nolaejui_playlist.entity

import jakarta.persistence.*

@Entity
@Table(name = "music_tag_relation",
    indexes = [
        Index(name = "idx_music_tag_relation_music_id", columnList = "music_id"),
        Index(name = "idx_music_tag_relation_tag_id", columnList = "tag_id")
    ])
class MusicTagRelation(@ManyToOne
                       @JoinColumn(name = "music_id", nullable = false)
                       val music: Music,

                       @ManyToOne
                       @JoinColumn(name = "tag_id", nullable = false)
                       val tag: Tag) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}