package cat.soft.src.oauth.auth.jwt;

import static cat.soft.src.oauth.util.BaseResponseStatus.*;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cat.soft.src.oauth.auth.AuthDao;
import cat.soft.src.oauth.user.UserDao;
import cat.soft.src.oauth.util.BaseException;
import cat.soft.src.oauth.util.BaseResponseStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;

@Component
@Getter
public class JwtTokenProvider {

	private final long JWT_ACCESS_TOKEN_EXPTIME;
	private final long JWT_REFRESH_TOKEN_EXPTIME;
	private final String JWT_ACCESS_SECRET_KEY;
	private final String JWT_REFRESH_SECRET_KEY;
	private Key accessKey;
	private Key refreshKey;

	private final UserDao userDao;
	private final AuthDao authDao;

	@Autowired
	public JwtTokenProvider(
		@Value("${jwt.time.access}") long JWT_ACCESS_TOKEN_EXPTIME,
		@Value("${jwt.time.refresh}") long JWT_REFRESH_TOKEN_EXPTIME,
		@Value("${jwt.secret.access}") String JWT_ACCESS_SECRET_KEY,
		@Value("${jwt.secret.refresh}") String JWT_REFRESH_SECRET_KEY, UserDao userDao, AuthDao authDao) {
		this.JWT_ACCESS_TOKEN_EXPTIME = JWT_ACCESS_TOKEN_EXPTIME;
		this.JWT_REFRESH_TOKEN_EXPTIME = JWT_REFRESH_TOKEN_EXPTIME;
		this.JWT_ACCESS_SECRET_KEY = JWT_ACCESS_SECRET_KEY;
		this.JWT_REFRESH_SECRET_KEY = JWT_REFRESH_SECRET_KEY;
		this.userDao = userDao;
		this.authDao = authDao;
	}

	@PostConstruct
	public void initialize() {
		byte[] accessKeyBytes = Decoders.BASE64.decode(JWT_ACCESS_SECRET_KEY);
		this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);

		byte[] secretKeyBytes = Decoders.BASE64.decode(JWT_REFRESH_SECRET_KEY);
		this.refreshKey = Keys.hmacShaKeyFor(secretKeyBytes);
	}

	public String createAccessToken(String useremail) {
		Claims claims = Jwts.claims().setSubject(useremail);
		claims.put("email", useremail);
		Date now = Date.from(ZonedDateTime.now().toInstant());
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(
				Date.from(ZonedDateTime.now().plusSeconds(JWT_ACCESS_TOKEN_EXPTIME).toInstant()))
			.signWith(accessKey, SignatureAlgorithm.HS256)
			.compact();
	}

	public String createRefreshToken(String useremail) {
		Claims claims = Jwts.claims().setSubject(useremail);

		claims.put("email", useremail);

		Date now = Date.from(ZonedDateTime.now().toInstant());
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(
				Date.from(ZonedDateTime.now().plusSeconds(JWT_REFRESH_TOKEN_EXPTIME).toInstant()))
			.signWith(accessKey, SignatureAlgorithm.HS256)
			.compact();
	}

	public String getUseridFromAcs(String token) {
		return Jwts.parserBuilder().setSigningKey(accessKey).build()
			.parseClaimsJws(token).getBody().getSubject();
	}

	public String getUseridFromRef(String token) {
		return Jwts.parserBuilder().setSigningKey(accessKey).build()
			.parseClaimsJws(token).getBody().getSubject();
	}

	public Claims getJwtContents(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(token).getBody();

		return claims;
	}

	public String getEmail(String token) {
		Claims claims = getJwtContents(token);
		return claims.get("email").toString();
	}

	public Long getExpiration(String accessToken) {
		Date expiration = Jwts.parserBuilder()
			.setSigningKey(accessKey)
			.build()
			.parseClaimsJws(accessToken)
			.getBody()
			.getExpiration();
		long now = Date.from(ZonedDateTime.now().toInstant()).getTime();
		return expiration.getTime() - now;
	}

	public void verifySignature(String token) throws BaseException {
		Key key = accessKey;
		Claims claims = this.getJwtContents(token);
		String email = String.valueOf(claims.get("email"));
		if (!authDao.checkRefreshToken(email))
			throw new BaseException(USERS_EMPTY_USER_EMAIL);
		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);
		} catch (Exception e) {
			throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
		}
	}
}