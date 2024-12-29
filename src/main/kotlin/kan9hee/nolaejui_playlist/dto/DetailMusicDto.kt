package kan9hee.nolaejui_playlist.dto

import java.time.LocalDate

data class DetailMusicDto(val id:Long,
                          val musicTitle:String,
                          val artist:String,
                          val tags:List<String>,
                          val dataType:String,
                          val dataUrl:String?,
                          val isPlayable:Boolean,
                          val uploadDate: LocalDate)