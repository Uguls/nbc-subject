package nbc.nbcsubject.common.security.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.nbcsubject.common.response.CommonResponse;
import nbc.nbcsubject.common.security.jwt.JwtAuthenticationException;
import nbc.nbcsubject.common.security.jwt.JwtTokenProvider;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String token = resolveToken(request);

		if (token != null) {
			try {
				tokenProvider.validateToken(token);
				Authentication authentication = tokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (JwtAuthenticationException e) {
				log.warn("JWT 인증 실패: {}", e.getMessage());
				setErrorResponse(response, e);
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	private void setErrorResponse(HttpServletResponse response, JwtAuthenticationException e) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json;charset=UTF-8");

		ObjectMapper objectMapper = new ObjectMapper();
		CommonResponse<?> errorResponse = CommonResponse.from(e.getResponseCode());
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}

	private String resolveToken(HttpServletRequest request) {
		String bearer = request.getHeader("Authorization");
		if (bearer != null && bearer.startsWith("Bearer ")) {
			return bearer.substring(7);
		}
		return null;
	}

}
