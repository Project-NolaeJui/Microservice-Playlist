package kan9hee.nolaejui_playlist.dto.requestOnly

data class MusicToPlaylistDto(val accessToken:String,
                              val playlistName:String,
                              val musicId:Long)