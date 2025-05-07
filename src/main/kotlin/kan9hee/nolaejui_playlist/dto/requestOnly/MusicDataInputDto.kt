package kan9hee.nolaejui_playlist.dto.requestOnly

data class MusicDataInputDto(
    val id:Long?,
    val musicTitle:String,
    val artist:String,
    val tags:List<String>,
    val dataType:String,
    val dataUrl:String?,
    val isPlayable:Boolean
)