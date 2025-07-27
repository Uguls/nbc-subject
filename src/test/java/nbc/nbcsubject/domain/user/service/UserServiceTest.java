package nbc.nbcsubject.domain.user.service;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import nbc.nbcsubject.common.security.jwt.JwtTokenProvider;
import nbc.nbcsubject.domain.user.dto.request.UserLoginRequest;
import nbc.nbcsubject.domain.user.dto.request.UserSignupRequest;
import nbc.nbcsubject.domain.user.dto.response.UserLoginResponse;
import nbc.nbcsubject.domain.user.dto.response.UserSignupResponse;
import nbc.nbcsubject.domain.user.entity.User;
import nbc.nbcsubject.domain.user.entity.UserRole;
import nbc.nbcsubject.domain.user.exception.UserException;
import nbc.nbcsubject.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@Mock
	private AuthenticationManager authenticationManager;

	private User createUser(String username, String nickname, String password) {
		return User.builder()
			.username(username)
			.nickname(nickname)
			.password(password)
			.userRole(UserRole.ROLE_USER)
			.build();
	}

	@Nested
	@DisplayName("회원가입 테스트")
	class SignUpTest {
		@Test
		@DisplayName("회원가입 테스트 - 성공")
		void signUp_Success () {
			// given
			UserSignupRequest request = new UserSignupRequest(
				"testuser",
				"테스트유저",
				"Password1!"
			);

			String encodedPassword = "encodedPassword";

			given(userRepository.existsByNickname("테스트유저")).willReturn(false);
			given(passwordEncoder.encode("Password1!")).willReturn(encodedPassword);

			// when
			UserSignupResponse response = userService.signUp(request);

			// then
			assertThat(response.getUsername()).isEqualTo("testuser");
			assertThat(response.getNickname()).isEqualTo("테스트유저");
			assertThat(response.getRoles()).containsExactly(UserRole.ROLE_USER.name());
		}

		@Test
		@DisplayName("회원가입 테스트 실패 - 닉네임 중복")
		void signUp_Fail_DeuplicateNickname () {
		    // given
			UserSignupRequest request = new UserSignupRequest(
				"testuser",
				"중복닉네임",
				"Password1!"
			);

			given(userRepository.existsByNickname("중복닉네임")).willReturn(true);
		
		    // when
			UserException userException = assertThrows(UserException.class, () -> userService.signUp(request));

			// then
			assertThat(userException.getResponseCode().getMessage()).isEqualTo("이미 가입된 사용자입니다.");
		}
	}

	@Nested
	@DisplayName("로그인 테스트")
	class LoginTest {
		@Test
		@DisplayName("로그인 테스트 - 성공")
		void Login_Success () {
			// given
			User user = createUser("testuser", "테스트 유저", "Password1!");
			ReflectionTestUtils.setField(user, "id", 1L);

			UserLoginRequest loginRequest = new UserLoginRequest("testuser", "Password1!");

			String accessToken = "accessToken";
			String refreshToken = "refreshToken";
			Authentication authentication = mock(Authentication.class);

			given(userRepository.findByUsername("testuser")).willReturn(Optional.of(user));
			given(passwordEncoder.matches("Password1!", user.getPassword())).willReturn(true);
			given(jwtTokenProvider.createAccessToken(authentication)).willReturn(accessToken);
			given(jwtTokenProvider.createRefreshToken(authentication)).willReturn(refreshToken);
			given(authenticationManager.authenticate(any())).willReturn(authentication);

			// when
			UserLoginResponse userLoginResponse = userService.login(loginRequest);

			// then
			assertThat(userLoginResponse.getAccessToken()).isEqualTo(accessToken);
			assertThat(userLoginResponse.getRefreshToken()).isEqualTo(refreshToken);
		}

		@Test
		@DisplayName("로그인 테스트 실패 - 사용자 없음")
		void login_Fail_NotFoundUser () {
		    // given
			UserLoginRequest loginRequest = new UserLoginRequest("testuser", "Password1!");

			given(userRepository.findByUsername("testuser")).willReturn(Optional.empty());

		    // when
			UserException userException = assertThrows(UserException.class, () -> userService.login(loginRequest));

			// then
			assertThat(userException.getResponseCode().getMessage()).isEqualTo("해당 유저를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("로그인 테스트 실패 - 비밀번호 불일치")
		void login_Fail_InvalidPassword () {
		    // given
			User user = createUser("testuser", "테스트 유저", "Password1!");
			ReflectionTestUtils.setField(user, "id", 1L);

			UserLoginRequest loginRequest = new UserLoginRequest("testuser", "Password1!");

			given(userRepository.findByUsername("testuser")).willReturn(Optional.of(user));
			given(passwordEncoder.matches("Password1!", user.getPassword())).willReturn(false);

		    // when
			UserException userException = assertThrows(UserException.class, () -> userService.login(loginRequest));

			// then
			assertThat(userException.getResponseCode().getMessage()).isEqualTo("아이디 또는 비밀번호가 올바르지 않습니다.");
		}
	}

}
