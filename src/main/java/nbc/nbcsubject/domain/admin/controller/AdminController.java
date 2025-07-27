package nbc.nbcsubject.domain.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nbc.nbcsubject.domain.admin.dto.response.UpdateUserRoleResponse;
import nbc.nbcsubject.domain.admin.service.AdminService;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

	private final AdminService adminService;

	@PatchMapping("/users/{userId}/roles")
	public ResponseEntity<UpdateUserRoleResponse> updateUserRole(
		@PathVariable("userId") Long userId
	) {
		UpdateUserRoleResponse updateUserRoleResponse = adminService.updateUserRole(userId);
		return ResponseEntity.ok(updateUserRoleResponse);
	}

}
