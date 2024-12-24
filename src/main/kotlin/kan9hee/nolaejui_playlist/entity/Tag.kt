package kan9hee.nolaejui_playlist.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.NoArgsConstructor

@Getter
@Entity
@NoArgsConstructor
@Table(name = "tag")
class Tag(@Column(nullable = false, unique = true)
          val name: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @OneToMany(mappedBy = "tag", cascade = [CascadeType.ALL], orphanRemoval = true)
    val musicTagRelations: MutableList<MusicTagRelation> = mutableListOf()
}