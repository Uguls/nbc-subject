package nbc.nbcsubject.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserSignupResponse {
    private String username;
    private String nickname;
    private List<String> roles;
}
