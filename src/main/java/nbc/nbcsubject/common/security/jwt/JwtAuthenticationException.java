package nbc.nbcsubject.common.security.jwt;

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
