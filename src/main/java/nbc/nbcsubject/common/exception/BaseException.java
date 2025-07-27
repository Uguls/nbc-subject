package nbc.nbcsubject.common.exception;

import nbc.nbcsubject.common.response.ResponseCode;

public abstract class BaseException extends RuntimeException {
	public abstract ResponseCode getResponseCode();
}
