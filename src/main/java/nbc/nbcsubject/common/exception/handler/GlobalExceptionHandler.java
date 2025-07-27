package nbc.nbcsubject.common.exception.handler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import nbc.nbcsubject.common.exception.BaseException;
import nbc.nbcsubject.common.exception.dto.ValidationError;
import nbc.nbcsubject.common.response.CommonResponse;
import nbc.nbcsubject.common.response.CommonResponses;
import nbc.nbcsubject.common.response.ResponseCode;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BaseException.class)
	public ResponseEntity<CommonResponse<ResponseCode>> handleBaseException(BaseException baseException) {
		log.error("예외 발생: {} (ErrorCode: {})", baseException.getMessage(), baseException.getMessage());

		return ResponseEntity.status(baseException.getHttpStatus())
			.body(CommonResponse.from(baseException.getResponseCode()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CommonResponses<ValidationError>> inputValidationExceptionHandler(BindingResult result) {
		log.error(result.getFieldErrors().toString());

		List<ValidationError> validationErrors = result.getFieldErrors()
			.stream()
			.map(fieldError -> ValidationError.builder()
				.field(fieldError.getField())
				.message(fieldError.getDefaultMessage())
				.code(fieldError.getCode())
				.build())
			.toList();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(CommonResponses.of(HttpStatus.BAD_REQUEST.name(), "잘못된 요청입니다.", validationErrors));
	}
}
