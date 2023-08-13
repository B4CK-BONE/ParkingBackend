package cat.soft.oauth.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Getter
public class JwtTokenProvider {


    private final long JWT_ACCESS_TOKEN_EXPTIME;
    private final long JWT_REFRESH_TOKEN_EXPTIME;
    private final String  JWT_ACCESS_SECRET_KEY;
    private final String  JWT_REFRESH_SECRET_KEY;
    private Key accessKey;
    private Key refreshKey;


    @Autowired
    public JwtTokenProvider(
                            @Value("${jwt.time.access}") long JWT_ACCESS_TOKEN_EXPTIME,
                            @Value("${jwt.time.refresh}") long JWT_REFRESH_TOKEN_EXPTIME,
                            @Value("${jwt.secret.access}") String JWT_ACCESS_SECRET_KEY,
                            @Value("${jwt.secret.refresh}") String JWT_REFRESH_SECRET_KEY) {
        this.JWT_ACCESS_TOKEN_EXPTIME = JWT_ACCESS_TOKEN_EXPTIME;
        this.JWT_REFRESH_TOKEN_EXPTIME = JWT_REFRESH_TOKEN_EXPTIME;
        this.JWT_ACCESS_SECRET_KEY = JWT_ACCESS_SECRET_KEY;
        this.JWT_REFRESH_SECRET_KEY = JWT_REFRESH_SECRET_KEY;
    }


    @PostConstruct
    public void initialize() {
        byte[] accessKeyBytes = Decoders.BASE64.decode(JWT_ACCESS_SECRET_KEY);
        this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);

        byte[] secretKeyBytes = Decoders.BASE64.decode(JWT_REFRESH_SECRET_KEY);
        this.refreshKey = Keys.hmacShaKeyFor(secretKeyBytes);
    }

    // JWT 토큰 생성
    public String createAccessToken(String useremail) {
        Claims claims = Jwts.claims().setSubject(useremail); // JWT payload 에 저장되는 정보단위, 보통 여기서 user를 식별하는 값을 넣는다.
        claims.put("email",useremail);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_EXPTIME)) // set Expire Time
                .signWith(accessKey, SignatureAlgorithm.HS256)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    public String createRefreshToken(String useremail) {
        Claims claims = Jwts.claims().setSubject(useremail); // JWT payload 에 저장되는 정보단위, 보통 여기서 user를 식별하는 값을 넣는다.

        claims.put("email",useremail);

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_EXPTIME)) // set Expire Time
                .signWith(accessKey, SignatureAlgorithm.HS256) // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

//    // JWT 토큰에서 인증 정보 조회
//    public Authentication getAuthentication(String token) {
//        PrincipalDetails userDetails = (PrincipalDetails) userDetailsService.loadUserByUsername(this.getUserid(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }

    // 토큰에서 회원 정보 추출
    public String getUseridFromAcs(String token) {
        return Jwts.parserBuilder().setSigningKey(accessKey).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getUseridFromRef(String token) {
        return Jwts.parserBuilder().setSigningKey(accessKey).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public Claims getJwtContents(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(token).getBody();

        return claims;
    }

    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        long now = new Date().getTime();
        return expiration.getTime() - now;
    }
}