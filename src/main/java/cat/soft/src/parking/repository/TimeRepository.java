package cat.soft.src.parking.repository;

import java.time.ZonedDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.soft.src.parking.model.Time;

public interface TimeRepository extends JpaRepository<Time, Integer> {
	Time findTimeByParkingLotIdxAndEndAfter(Integer parkingLotIdx, ZonedDateTime time);

	Time findTimeByUserIdxAndEndAfter(Integer userIdx, ZonedDateTime time);
}
