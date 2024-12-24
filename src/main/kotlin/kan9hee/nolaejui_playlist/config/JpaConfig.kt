package kan9hee.nolaejui_playlist.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = ["kan9hee.nolaejui_playlist"])
class JpaConfig {
    @PersistenceContext
    private lateinit var entityManager:EntityManager

    @Bean
    fun queryFactory():JPAQueryFactory{
        return JPAQueryFactory(entityManager)
    }
}