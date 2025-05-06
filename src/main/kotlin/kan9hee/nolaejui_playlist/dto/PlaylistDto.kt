package kan9hee.nolaejui_playlist.dto

data class PlaylistDto(
    val playListTitle:String,
    val playListOwner:String,
    val isDefaultStorage:Boolean,
    val musicIdList:List<Long>
)