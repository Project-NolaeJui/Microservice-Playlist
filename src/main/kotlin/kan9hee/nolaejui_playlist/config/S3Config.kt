package kan9hee.nolaejui_playlist.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import lombok.Data
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder

@Configuration
class S3Config {

    @Autowired
    private lateinit var s3ValueConfig: S3ValueConfig

    @Bean
    @Primary
    fun awsCredentialsProvider(): BasicAWSCredentials {
        return BasicAWSCredentials(s3ValueConfig.accessKey, s3ValueConfig.secretKey)
    }

    @Bean
    fun amazonS3(): AmazonS3 {
        val s3Builder: AmazonS3 = AmazonS3ClientBuilder.standard()
            .withRegion(s3ValueConfig.region)
            .withCredentials(AWSStaticCredentialsProvider(awsCredentialsProvider()))
            .build()
        return s3Builder
    }

    @Component
    @ConfigurationProperties(prefix = "aws")
    @Data
    class S3ValueConfig {
        lateinit var accessKey: String
        lateinit var secretKey: String
        lateinit var region: String
        lateinit var bucket: String
        lateinit var folder: String
        lateinit var stack: String
        lateinit var entityBaseURL: String
    }
}