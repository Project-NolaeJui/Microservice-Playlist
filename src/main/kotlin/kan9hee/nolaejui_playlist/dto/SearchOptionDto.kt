package kan9hee.nolaejui_playlist.dto

data class SearchOptionDto(
    val ids:List<Long>?,
    val musicTitle:String?,
    val artist:String?,
    val dataType:String?,
    val tags:List<String>?,
    val page:Int
)