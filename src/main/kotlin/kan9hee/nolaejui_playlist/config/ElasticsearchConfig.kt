package kan9hee.nolaejui_playlist.config

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.rest_client.RestClientTransport
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

@Configuration
@EnableElasticsearchRepositories
class ElasticsearchConfig:AbstractElasticsearchConfig() {
    override fun elasticsearchClient(): ElasticsearchClient  {
        val restClient = RestClient.builder(
            HttpHost("localhost",9200,"http")
        ).build()

        val transport = RestClientTransport(restClient,JacksonJsonpMapper())
        return ElasticsearchClient(transport)
    }
}