package kan9hee.nolaejui_playlist.service

import AuthServerGrpcKt
import PlayLogServerGrpcKt
import com.google.protobuf.Timestamp
import kan9hee.nolaejui_playlist.dto.requestOnly.LocationDto
import kan9hee.nolaejui_playlist.dto.requestOnly.MusicToPlaylistDto
import kan9hee.nolaejui_playlist.dto.requestOnly.ReportProblemDto
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId

@Service
class ExternalService(@GrpcClient("nolaejui-auth")
                      private val authStub: AuthServerGrpcKt.AuthServerCoroutineStub,
                      @GrpcClient("nolaejui-location")
                      private val playLogStub: PlayLogServerGrpcKt.PlayLogServerCoroutineStub,
                      @GrpcClient("nolaejui-management")
                      private val managementStub: AdminResponseServerGrpcKt.AdminResponseServerCoroutineStub,
                      private val dataService: DataService) {

    suspend fun getUsername(accessTokenString:String): String {
        val request = Playlist.AccessToken.newBuilder()
            .setAccessToken(accessTokenString)
            .build()

        val response = authStub.getUserName(request)
        if(!response.isSuccess)
            throw RuntimeException(response.resultMessage)

        return response.resultMessage
    }

    suspend fun pickupMusics(locationDto:LocationDto){
        val request = Playlist.LocationInfo.newBuilder()
            .setLongitude(locationDto.longitude)
            .setLatitude(locationDto.latitude)
            .build()

        val response = playLogStub.pickupMusics(request)
        response.musicIdsList.forEach {
            dataService.addMusicIdToPlaylist(
                MusicToPlaylistDto("pickup",locationDto.userName,it))
        }
    }

    suspend fun reportMusicProblem(reportProblemDto: ReportProblemDto): String {
        val request = Playlist.MusicProblem.newBuilder()
            .setMusicInfo(
                Playlist.MusicInfo.newBuilder()
                    .setMusicId(reportProblemDto.detailMusicDto.id)
                    .setMusicTitle(reportProblemDto.detailMusicDto.musicTitle)
                    .setArtist(reportProblemDto.detailMusicDto.artist)
                    .addAllTags(reportProblemDto.detailMusicDto.tags)
                    .setDataType(reportProblemDto.detailMusicDto.dataType)
                    .setDataUrl(reportProblemDto.detailMusicDto.dataUrl)
                    .setIsPlayable(reportProblemDto.detailMusicDto.isPlayable)
                    .setUploaderName(reportProblemDto.detailMusicDto.uploader)
                    .setUploadDate(convertLocalDateToProtoTimestamp(reportProblemDto.detailMusicDto.uploadDate))
            )
            .setProblemCase(reportProblemDto.problemCase)
            .setProblemDetail(reportProblemDto.problemDetail)
            .build()

        val response = managementStub.reportMusicProblem(request)
        return response.resultMessage
    }

    private fun convertLocalDateToProtoTimestamp(localDate: LocalDate): Timestamp {
        val instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        return Timestamp.newBuilder()
            .setSeconds(instant.epochSecond)
            .setNanos(instant.nano)
            .build()
    }
}