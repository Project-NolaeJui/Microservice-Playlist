package kan9hee.nolaejui_playlist.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner

@Configuration
class S3Config(private val s3ValueConfig: S3ValueConfig) {

    @Bean
    fun s3Client(): S3Client = S3Client.builder()
        .region(Region.of(s3ValueConfig.region))
        .credentialsProvider(commonAwsCredentialsProvider())
        .build()

    @Bean
    fun s3Presigner(): S3Presigner = S3Presigner.builder()
        .region(Region.of(s3ValueConfig.region))
        .credentialsProvider(commonAwsCredentialsProvider())
        .build()

    private fun commonAwsCredentialsProvider() = StaticCredentialsProvider.create(
        AwsBasicCredentials.create(s3ValueConfig.accessKey, s3ValueConfig.secretKey)
    )
}