package nbc.nbcsubject.domain.admin.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UpdateUserRoleResponse {
    private String username;
    private String nickname;
    private List<String> roles;
}
