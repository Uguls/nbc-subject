package nbc.nbcsubject.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nbc.nbcsubject.domain.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findBynickName(String nickName);

    boolean existsByNickName(String nickName);

    Optional<User> findByUsername(String username);
}
