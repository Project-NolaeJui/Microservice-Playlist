package kan9hee.nolaejui_playlist.config

import co.elastic.clients.elasticsearch.ElasticsearchClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter

@Configuration
abstract class AbstractElasticsearchConfig:ElasticsearchConfigurationSupport() {
    @Bean
    abstract fun elasticsearchClient(): ElasticsearchClient

    @Bean(name = ["elasticsearchOperations","elasticsearchTemplate"])
    fun elasticsearchOperations(
        elasticsearchConverter: ElasticsearchConverter,
        elasticsearchClient: ElasticsearchClient
    ): ElasticsearchOperations{
        return ElasticsearchTemplate(elasticsearchClient,elasticsearchConverter)
    }
}