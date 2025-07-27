package nbc.nbcsubject.domain.user.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import nbc.nbcsubject.common.exception.BaseException;
import nbc.nbcsubject.common.response.ResponseCode;

@Getter
public class UserException extends BaseException {
	private final ResponseCode responseCode;
	private final HttpStatus httpStatus;

	public UserException(ResponseCode responseCode) {
		this.responseCode = responseCode;
		this.httpStatus = responseCode.getHttpStatus();
	}
}
