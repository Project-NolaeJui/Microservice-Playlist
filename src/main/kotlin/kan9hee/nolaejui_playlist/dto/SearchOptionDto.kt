package kan9hee.nolaejui_playlist.dto

import java.time.LocalDate

data class SearchOptionDto(val musicTitle:String,
                           val dataType:String,
                           val tags:List<String>,
                           val storeDate: LocalDate,
                           val isDateAfter:Boolean) {
}