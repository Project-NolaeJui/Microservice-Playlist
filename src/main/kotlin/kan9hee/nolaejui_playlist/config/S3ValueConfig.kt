package kan9hee.nolaejui_playlist.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties(prefix = "aws")
class S3ValueConfig {
    lateinit var accessKey: String
    lateinit var secretKey: String
    lateinit var region: String
    lateinit var bucket: String
    lateinit var folder: String
    lateinit var stack: String
    lateinit var entityBaseURL: String
}