package cat.soft.src.parking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import cat.soft.src.parking.model.User;
import jakarta.persistence.LockModeType;

public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findUsersByRoomIdxAndRole(Long roomIdx, Long role);

	User findUserByEmail(String email);

	@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	User findByEmail(String email);
}

