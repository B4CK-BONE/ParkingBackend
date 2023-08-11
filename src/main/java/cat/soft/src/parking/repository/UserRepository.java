package cat.soft.src.parking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import cat.soft.oauth.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String Email);
}

