package nbc.nbcsubject.common.security.jwt;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nbc.nbcsubject.common.response.ResponseCode;

@Getter
@RequiredArgsConstructor
public enum JwtExceptionCode implements ResponseCode {

	INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명입니다.", "INVALID_SIGNATURE"),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다.", "EXPIRED_TOKEN"),
	UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다.", "UNSUPPORTED_TOKEN"),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 토큰이 잘못되었습니다.", "INVALID_TOKEN");

	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
