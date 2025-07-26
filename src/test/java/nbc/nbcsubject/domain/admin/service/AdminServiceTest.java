package nbc.nbcsubject.domain.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collection;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import nbc.nbcsubject.domain.admin.dto.response.UpdateUserRoleResponse;
import nbc.nbcsubject.domain.user.entity.User;
import nbc.nbcsubject.domain.user.entity.UserRole;
import nbc.nbcsubject.domain.user.exception.UserException;
import nbc.nbcsubject.domain.user.repository.UserRepository;
import nbc.nbcsubject.domain.user.security.CustomUserDetails;

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

	@Nested
  	@DisplayName("권한 체크 테스트")
	  class AuthorityCheckTest {

		  @Test
		  @DisplayName("일반 사용자 권한 확인")
		  void checkUserAuthority() {
			  // given
			  User user = User.builder()
				  .username("testuser")
				  .nickname("테스트유저")
				  .password("encodedPassword")
				  .userRole(UserRole.ROLE_USER)
				  .build();

			  CustomUserDetails userDetails = new CustomUserDetails(user);

			  // when
			  Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

			  // then
			  assertThat(authorities).hasSize(1);
			  assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_USER");
		  }

		  @Test
		  @DisplayName("관리자 권한 확인")
		  void checkAdminAuthority() {
			  // given
			  User admin = User.builder()
				  .username("admin")
				  .nickname("관리자")
				  .password("encodedPassword")
				  .userRole(UserRole.ROLE_ADMIN)
				  .build();

			  CustomUserDetails adminDetails = new CustomUserDetails(admin);

			  // when
			  Collection<? extends GrantedAuthority> authorities = adminDetails.getAuthorities();

			  // then
			  assertThat(authorities).hasSize(1);
			  assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
		  }

		  @Test
		  @DisplayName("권한 변경 후 권한 확인")
		  void checkAuthorityAfterRoleUpdate() {
			  // given
			  User user = User.builder()
				  .username("testuser")
				  .nickname("테스트유저")
				  .password("encodedPassword")
				  .userRole(UserRole.ROLE_USER)
				  .build();

			  // when
			  user.updateRole(UserRole.ROLE_ADMIN);
			  CustomUserDetails userDetails = new CustomUserDetails(user);

			  // then
			  Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
			  assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
		  }
	  }

}
