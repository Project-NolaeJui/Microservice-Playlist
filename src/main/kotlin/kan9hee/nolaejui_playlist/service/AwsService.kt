package kan9hee.nolaejui_playlist.service

import kan9hee.nolaejui_playlist.config.S3ValueConfig
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration

@Service
class AwsService(private val s3ValueConfig: S3ValueConfig,
                 private val s3Presigner: S3Presigner,
                 private val s3Client: S3Client) {

    fun deleteMusicS3(musicName: String) {
        s3Client.deleteObject(
            DeleteObjectRequest.builder()
                .bucket(s3ValueConfig.bucket)
                .key(s3ValueConfig.folder + musicName)
                .build()
        )
    }

    fun getPreSignedUrl(fileName: String): String {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(s3ValueConfig.bucket)
            .key(s3ValueConfig.folder + fileName)
            .build()

        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(1))
            .putObjectRequest(putObjectRequest)
            .build()

        return s3Presigner.presignPutObject(presignRequest).url().toString()
    }
}
