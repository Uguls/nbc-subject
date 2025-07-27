package nbc.nbcsubject.domain.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import nbc.nbcsubject.domain.admin.dto.response.UpdateUserRoleResponse;
import nbc.nbcsubject.domain.user.entity.User;
import nbc.nbcsubject.domain.user.entity.UserRole;
import nbc.nbcsubject.domain.user.exception.UserException;
import nbc.nbcsubject.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

	@InjectMocks
	private AdminService adminService;

	@Mock
	private UserRepository userRepository;

	private User createUser(String username, String nickname, String password, UserRole userRole) {
		return User.builder()
			.username(username)
			.nickname(nickname)
			.password(password)
			.userRole(userRole)
			.build();
	}

	@Nested
	@DisplayName("유저 권한 상승 테스트")
	class UpdateUserRoleTest {

		@Test
		@DisplayName("유저 권한 상승 테스트 - 성공")
		void updateUserRole_Success () {
		    // given
			User user = createUser("testuser", "테스트 유저", "Password1!", UserRole.ROLE_USER);
			ReflectionTestUtils.setField(user, "id", 1L);

			given(userRepository.findById(1L)).willReturn(Optional.of(user));

			// when
			UpdateUserRoleResponse updateUserRoleResponse = adminService.updateUserRole(1L);

			// then
			assertThat(updateUserRoleResponse.getRoles()).containsExactly(UserRole.ROLE_ADMIN.name());
			assertThat(user.getUserRole()).isEqualTo(UserRole.ROLE_ADMIN);
		}

		@Test
		@DisplayName("유저 권한 상승 테스트 실패 - 존재하지 않는 사용자")
		void updateUserRole_Fail_UserNotFound () {
		    // given
			given(userRepository.findById(1L)).willReturn(Optional.empty());

		    // when
			UserException userException = assertThrows(UserException.class, () -> adminService.updateUserRole(1L));

			// then
			assertThat(userException.getResponseCode().getMessage()).isEqualTo("해당 유저를 찾을 수 없습니다.");
		}

	}
}
