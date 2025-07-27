package nbc.nbcsubject.domain.user.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignupResponse {
	private Long userId;
	private String username;
	private String nickname;
	private List<String> roles;
}
