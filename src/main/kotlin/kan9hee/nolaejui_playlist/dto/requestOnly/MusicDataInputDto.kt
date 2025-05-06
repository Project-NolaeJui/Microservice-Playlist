package kan9hee.nolaejui_playlist.dto.requestOnly

import java.time.LocalDateTime

data class MusicDataInputDto(
    val userName:String,
    val id:Long?,
    val musicTitle:String,
    val artist:String,
    val tags:List<String>,
    val dataType:String,
    val dataUrl:String?,
    val isPlayable:Boolean,
    val uploadDate: LocalDateTime
)