package nbc.nbcsubject.common.response;

import org.springframework.http.HttpStatus;

public interface ResponseCode {
	HttpStatus getHttpStatus();

	String getMessage();

	String getCode();
}
