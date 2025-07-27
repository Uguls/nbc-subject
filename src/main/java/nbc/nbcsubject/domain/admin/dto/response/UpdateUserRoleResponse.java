package nbc.nbcsubject.domain.admin.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateUserRoleResponse {
	private String username;
	private String nickname;
	private List<String> roles;
}
