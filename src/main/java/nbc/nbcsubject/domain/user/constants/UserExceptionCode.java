package nbc.nbcsubject.domain.user.constants;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nbc.nbcsubject.common.response.ResponseCode;

@Getter
@RequiredArgsConstructor
public enum UserExceptionCode implements ResponseCode {
	USER_NOT_FOUND(HttpStatus.NOT_FOUND ,"해당 유저를 찾을 수 없습니다.", "USER_NOT_FOUND"),
	USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 가입된 사용자입니다.", "USER_ALREADY_EXISTS"),
	INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 올바르지 않습니다.", "INVALID_CREDENTIALS");

	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
