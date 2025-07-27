package nbc.nbcsubject.domain.admin.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import nbc.nbcsubject.common.config.SecurityConfig;
import nbc.nbcsubject.common.exception.handler.CustomAccessDeniedHandler;
import nbc.nbcsubject.common.security.jwt.JwtTokenProvider;
import nbc.nbcsubject.domain.admin.dto.response.UpdateUserRoleResponse;
import nbc.nbcsubject.domain.admin.service.AdminService;
import nbc.nbcsubject.domain.user.entity.User;
import nbc.nbcsubject.domain.user.entity.UserRole;

@WebMvcTest(AdminController.class)
@Import({
	SecurityConfig.class,
	AdminControllerTest.AdminControllerTestConfig.class,
	CustomAccessDeniedHandler.class,
})
class AdminControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private AdminService adminService;

	private User createUser(String username, String nickname, String password, UserRole userRole) {
		return User.builder()
			.username(username)
			.nickname(nickname)
			.password(password)
			.userRole(userRole)
			.build();
	}

	@TestConfiguration
	static class AdminControllerTestConfig {
		@Bean
		public AdminService adminService() {
			return mock(AdminService.class);
		}

		@Bean
		public JwtTokenProvider jwtTokenProvider() {
			return mock(JwtTokenProvider.class);
		}
	}

	@Nested
	@DisplayName("유저 권한 상승 테스트")
	class UpdateUserRoleTest {

		@Test
		@WithMockUser(roles = "ADMIN")
		@DisplayName("유저 권한 상승 테스트 - 성공")
		void updateUserRole_Success() throws Exception {
			// given
			Long userId = 1L;
			UpdateUserRoleResponse roleResponse = UpdateUserRoleResponse.builder()
				.username("testuser")
				.nickname("테스트 유저")
				.roles(List.of("ROLE_ADMIN"))
				.build();

			given(adminService.updateUserRole(userId)).willReturn(roleResponse);

			// when & then
			mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("testuser"));
		}

		@Test
		@WithMockUser(roles = "USER")
		@DisplayName("유저 권한 상승 테스트 실패 - 권한 부족")
		void updateUserRole_Fail_AccessDenied() throws Exception {
			// given
			Long userId = 1L;

			// when & then
			mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isForbidden());
		}

	}

}
