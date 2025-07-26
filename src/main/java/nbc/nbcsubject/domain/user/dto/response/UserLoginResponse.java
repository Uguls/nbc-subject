package nbc.nbcsubject.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserLoginResponse {
    private String token;
}
