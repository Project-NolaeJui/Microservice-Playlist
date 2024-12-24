package kan9hee.nolaejui_playlist.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.NoArgsConstructor

@Getter
@Entity
@NoArgsConstructor
@Table(name = "music_tag_relation")
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