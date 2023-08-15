package cat.soft.src.parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.soft.src.parking.model.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
