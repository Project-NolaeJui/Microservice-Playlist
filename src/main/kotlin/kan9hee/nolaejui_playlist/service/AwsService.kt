package kan9hee.nolaejui_playlist.service

import kan9hee.nolaejui_playlist.config.S3ValueConfig
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration
import java.util.*

@Service
class AwsService(private val s3ValueConfig: S3ValueConfig,
                 private val s3Presigner: S3Presigner,
                 private val s3Client: S3Client) {

    fun deleteMusicS3(musicName:String){
        val deleteRequest = DeleteObjectRequest.builder()
            .bucket(s3ValueConfig.bucket)
            .key(s3ValueConfig.folder + musicName)
            .build()

        s3Client.deleteObject(deleteRequest)
    }

    fun getPreSignedUrl(fileName:String): String {
        val fullPath = createPath(fileName)

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(s3ValueConfig.bucket)
            .key(fullPath)
            .build()

        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(getPreSignedUrlExpiration()))
            .putObjectRequest(putObjectRequest)
            .build()

        val generatedPreSignedUrlRequest = s3Presigner.presignPutObject(presignRequest)
        return generatedPreSignedUrlRequest.url().toString()
    }

    private fun getPreSignedUrlExpiration(): Long {
        val expiration = Date()
        val expTimeMillis = expiration.time + 1000 * 60
        return expTimeMillis
    }

    private fun createPath(fileName: String): String {
        return java.lang.String.format("%s%s", s3ValueConfig.folder, fileName)
    }
}