package nbc.nbcsubject.common.security.jwt;

import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.nbcsubject.domain.user.repository.UserRepository;
import nbc.nbcsubject.domain.user.security.CustomUserDetails;
import nbc.nbcsubject.domain.user.security.CustomUserDetailsService;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	private final UserRepository userRepository;
	private final CustomUserDetailsService customUserDetailsService;

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";
	private static final String ROLES_KEY = "roles";

	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.accessToken-expiration-time}")
	private long accessTokenExpirationTime;
	@Value("${jwt.refreshToken-expiration-time}")
	private long refreshTokenExpirationTime;
	private SecretKey secretKey;

	@PostConstruct
	protected void init() {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public String createAccessToken(Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		String nickName = userDetails.getUser().getNickname();

		String authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validity = new Date(now + this.accessTokenExpirationTime);

		return Jwts.builder()
			.subject(nickName)
			.claim(ROLES_KEY, authorities)
			.issuedAt(new Date())
			.expiration(validity)
			.signWith(secretKey, Jwts.SIG.HS512)
			.compact();
	}

	public String createRefreshToken(Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		String nickName = userDetails.getUser().getNickname();

		String authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validity = new Date(now + this.refreshTokenExpirationTime);

		return Jwts.builder()
			.subject(nickName)
			.issuedAt(new Date())
			.expiration(validity)
			.signWith(secretKey, Jwts.SIG.HS512)
			.compact();
	}

	public void validateToken(String token) throws JwtAuthenticationException {
		try {
			JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
			parser.parseClaimsJws(token);
		} catch (SecurityException | MalformedJwtException e) {
			throw new JwtAuthenticationException(JwtExceptionCode.INVALID_SIGNATURE);
		} catch (ExpiredJwtException e) {
			throw new JwtAuthenticationException(JwtExceptionCode.EXPIRED_TOKEN);
		} catch (UnsupportedJwtException e) {
			throw new JwtAuthenticationException(JwtExceptionCode.UNSUPPORTED_TOKEN);
		} catch (IllegalArgumentException e) {
			throw new JwtAuthenticationException(JwtExceptionCode.INVALID_TOKEN);
		}
	}

	public Long getUserIdFromToken(String token) {
		JwtParser parser = Jwts.parser()
			.verifyWith(secretKey)
			.build();

		Jws<Claims> claims = parser.parseSignedClaims(token);

		String subject = claims.getPayload().getSubject();

		return Long.parseLong(subject);
	}

	public Authentication getAuthentication(String token) {
		JwtParser parser = Jwts.parser()
			.verifyWith(secretKey)
			.build();

		Jws<Claims> claims = parser.parseSignedClaims(token);
		String username = claims.getBody().getSubject();

		UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

}
