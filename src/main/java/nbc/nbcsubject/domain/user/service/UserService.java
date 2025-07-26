package nbc.nbcsubject.domain.user.service;

import lombok.RequiredArgsConstructor;
import nbc.nbcsubject.common.security.jwt.JwtTokenProvider;
import nbc.nbcsubject.domain.user.constants.UserExceptionCode;
import nbc.nbcsubject.domain.user.dto.request.UserLoginRequest;
import nbc.nbcsubject.domain.user.dto.request.UserSignupRequest;
import nbc.nbcsubject.domain.user.dto.response.UserLoginResponse;
import nbc.nbcsubject.domain.user.dto.response.UserSignupResponse;
import nbc.nbcsubject.domain.user.entity.User;
import nbc.nbcsubject.domain.user.entity.UserRole;
import nbc.nbcsubject.domain.user.exception.UserException;
import nbc.nbcsubject.domain.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public UserSignupResponse signUp (UserSignupRequest request) {

        if (userRepository.existsByNickName(request.getNickname())) {
            throw new UserException(UserExceptionCode.USER_ALREADY_EXISTS);
        }

        String encoded = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .username(request.getUsername())
                .nickName(request.getNickname())
                .password(encoded)
                .userRole(UserRole.ROLE_USER)
                .build();

        userRepository.save(user);

        UserSignupResponse userSignupResponse = UserSignupResponse.builder()
                .username(user.getUsername())
                .nickname(user.getNickName())
                .roles(List.of(user.getUserRole().name()))
                .build();

        return userSignupResponse;

    }

    public UserLoginResponse login (UserLoginRequest request) {
        System.out.println("login 서비스 호출 \n");
        User user = userRepository.findByUsername((request.getUsername()))
                .orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserException(UserExceptionCode.INVALID_CREDENTIALS);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        String accessToken = jwtTokenProvider.createAccessToken(authentication);

        UserLoginResponse userLoginResponse = UserLoginResponse.builder().token(accessToken).build();

        return userLoginResponse;
    }
}
