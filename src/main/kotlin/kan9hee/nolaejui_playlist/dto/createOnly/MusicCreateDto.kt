package kan9hee.nolaejui_playlist.dto.createOnly

import java.time.LocalDate

data class MusicCreateDto(val musicTitle:String,
                          val tags:List<String>,
                          val dataType:String,
                          val dataUrl:String?,
                          val isPlayable:Boolean,
                          val uploader:String,
                          val uploadDate: LocalDate) {
}