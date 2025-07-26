package nbc.nbcsubject.domain.admin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nbc.nbcsubject.domain.admin.dto.response.UpdateUserRoleResponse;
import nbc.nbcsubject.domain.user.constants.UserExceptionCode;
import nbc.nbcsubject.domain.user.entity.User;
import nbc.nbcsubject.domain.user.entity.UserRole;
import nbc.nbcsubject.domain.user.exception.UserException;
import nbc.nbcsubject.domain.user.repository.UserRepository;
import nbc.nbcsubject.domain.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    @Transactional
    public UpdateUserRoleResponse updateUserRole(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserExceptionCode.USER_NOT_FOUND));

        user.updateRole(UserRole.ROLE_ADMIN);

        UpdateUserRoleResponse updateUserRoleResponse = UpdateUserRoleResponse.builder()
                .username(user.getUsername())
                .nickname(user.getNickName())
                .roles(List.of(user.getUserRole().name()))
                .build();

        return updateUserRoleResponse;
    }


}
