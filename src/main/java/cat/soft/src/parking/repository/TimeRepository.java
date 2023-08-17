package cat.soft.src.parking.repository;

import java.time.ZonedDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import cat.soft.src.parking.model.Time;
import jakarta.persistence.LockModeType;

public interface TimeRepository extends JpaRepository<Time, Long> {

	Time findTimeByParkingLotIdxAndRoomIdxAndEndAfter(Long parkingLotIdx, Long roomIdx, ZonedDateTime time);

	@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	Time findByUserIdxAndEndAfter(Long userIdx, ZonedDateTime time);

	Time findTimeByUserIdxAndEndAfter(Long userIdx, ZonedDateTime time);
}
