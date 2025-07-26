package nbc.nbcsubject.common.security.jwt;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import nbc.nbcsubject.common.exception.BaseException;
import nbc.nbcsubject.common.response.ResponseCode;

@Getter
public class JwtAuthenticationException extends BaseException {
	private final ResponseCode responseCode;

	public JwtAuthenticationException(ResponseCode responseCode) {
		this.responseCode = responseCode;
	}
}
