package nbc.nbcsubject.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "user_name", nullable = false)
	private String username;

	@Column(name = "nick_name", nullable = false)
	private String nickName;

	@Column(name = "user_password", nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_role", nullable = false)
	private UserRole userRole;

	@Builder
	public User(String username, String nickName, String password, UserRole userRole) {
		this.username = username;
		this.nickName = nickName;
		this.password = password;
		this.userRole = userRole;
	}

	public void updateRole (UserRole userRole) {
		this.userRole = userRole;
	}

}
