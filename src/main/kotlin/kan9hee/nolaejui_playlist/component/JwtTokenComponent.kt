package kan9hee.nolaejui_playlist.component

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.crypto.SecretKey

@Component
class JwtTokenComponent(@Value("\${jwt.secret}") secretKey:String) {

    private val key: SecretKey

    init {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun getUsernameFrom(accessToken:String): String {
        val claims=getClaimsFromToken(accessToken)

        return claims.subject
    }

    private fun getClaimsFromToken(token:String): Claims {
        val claims = try {
            Jwts.parser()
                .verifyWith(this.key)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: Exception) {
            throw RuntimeException("Invalid JWT token: ${e.message}", e)
        }

        if (claims == null) {
            throw RuntimeException("Claims cannot be null for token: $token")
        }
        return claims
    }
}