package nbc.nbcsubject.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nbc.nbcsubject.domain.user.dto.request.UserLoginRequest;
import nbc.nbcsubject.domain.user.dto.request.UserSignupRequest;
import nbc.nbcsubject.domain.user.dto.response.UserLoginResponse;
import nbc.nbcsubject.domain.user.dto.response.UserSignupResponse;
import nbc.nbcsubject.domain.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<UserSignupResponse> signUp(
		@RequestBody UserSignupRequest request
	) {
		UserSignupResponse userSignupResponse = userService.signUp(request);
		return ResponseEntity.ok(userSignupResponse);
	}

	@PostMapping("/login")
	public ResponseEntity<UserLoginResponse> postUserLoginResponse(
		@RequestBody UserLoginRequest request
	) {
		UserLoginResponse loginResponse = userService.login(request);
		return ResponseEntity.ok(loginResponse);
	}

}
