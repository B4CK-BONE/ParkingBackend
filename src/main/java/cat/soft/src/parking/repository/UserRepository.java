package cat.soft.src.parking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.soft.src.parking.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findUsersByRoomIdxAndRole(Long roomIdx, Long role);

	User findUsersByEmail(String email);
}

