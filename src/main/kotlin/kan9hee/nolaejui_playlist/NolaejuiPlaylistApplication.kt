package kan9hee.nolaejui_playlist

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class NolaejuiPlaylistApplication

fun main(args: Array<String>) {
	runApplication<NolaejuiPlaylistApplication>(*args)
}
