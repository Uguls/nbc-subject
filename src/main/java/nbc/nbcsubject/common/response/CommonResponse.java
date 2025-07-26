package nbc.nbcsubject.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {
	private String status;
	private String message;

	public static <T> CommonResponse<T> of(String status, String message) {
		return CommonResponse.<T>builder()
			.status(status)
			.message(message)
			.build();
	}

	public static <T> CommonResponse<T> from(ResponseCode responseCode) {
		return CommonResponse.<T>builder()
			.status(responseCode.getCode())
			.message(responseCode.getMessage())
			.build();
	}
}
