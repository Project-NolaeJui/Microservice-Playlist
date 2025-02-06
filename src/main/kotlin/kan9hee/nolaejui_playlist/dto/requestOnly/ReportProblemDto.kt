package kan9hee.nolaejui_playlist.dto.requestOnly

import kan9hee.nolaejui_playlist.dto.DetailMusicDto

data class ReportProblemDto(val detailMusicDto: DetailMusicDto,
                            val problemCase:String,
                            val problemDetail:String)