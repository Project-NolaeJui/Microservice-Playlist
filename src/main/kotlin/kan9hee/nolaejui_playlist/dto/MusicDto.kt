package kan9hee.nolaejui_playlist.dto

import java.time.LocalDate

data class MusicDto(val id:Long,
                    val musicTitle:String,
                    val tags:List<String>,
                    val dataType:String,
                    val dataUrl:String?,
                    val isPlayable:Boolean,
                    val uploadDate: LocalDate) {
}