package kan9hee.nolaejui_playlist.service

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.Headers
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import kan9hee.nolaejui_playlist.config.S3Config
import kan9hee.nolaejui_playlist.dto.PlayLogDto
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.util.*

@Service
class ExternalService(private val webClient: WebClient,
                      private val s3ValueConfig: S3Config.S3ValueConfig,
                      private val amazonS3:AmazonS3,
                      private val dataService: DataService) {

    fun pickupMusic(username:String,longitude:Double,latitude:Double){
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("http://nolaejui-location/getNearbyMusicId")
                    .queryParam("longitude",longitude)
                    .queryParam("latitude",latitude)
                    .build()
            }
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<Long>>() {})
            .block()

        response?.forEach {
            dataService.addMusicIdToPlaylist("pickups",username,it)
        }
    }

    fun playMusic(playLogDto: PlayLogDto){
        webClient.post()
            .uri("http://nolaejui-location/addMusicPlayLog")
            .bodyValue(playLogDto)
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        return dataService.getMusic(playLogDto.musicId)
    }

    fun deleteMusicS3(musicName:String){
        amazonS3.deleteObject(s3ValueConfig.bucket, s3ValueConfig.folder + musicName)
    }

    fun getPreSignedUrl(fileName:String): String {
        val fullPath = createPath(fileName)
        val generatePreSignedUrlRequest = getGeneratePreSignedUrlRequest(fullPath)
        val url = amazonS3.generatePresignedUrl(generatePreSignedUrlRequest)
        return url.toString()
    }

    private fun getGeneratePreSignedUrlRequest(filePath: String): GeneratePresignedUrlRequest {
        val generatePreSignedUrlRequest =
            GeneratePresignedUrlRequest(s3ValueConfig.bucket, filePath)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getPreSignedUrlExpiration())
        generatePreSignedUrlRequest.addRequestParameter(
            Headers.S3_CANNED_ACL,
            CannedAccessControlList.PublicRead.toString()
        )
        return generatePreSignedUrlRequest
    }

    private fun getPreSignedUrlExpiration(): Date {
        val expiration = Date()
        val expTimeMillis = expiration.time + 1000 * 60
        expiration.time = expTimeMillis
        return expiration
    }

    private fun createPath(fileName: String): String {
        return java.lang.String.format("%s%s", s3ValueConfig.folder, fileName)
    }
}