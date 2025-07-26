package nbc.nbcsubject.common.response;

import org.springframework.http.HttpStatus;

public interface ResponseCode {
	String getMessage();

	String getCode();
}
