package cat.soft.src.parking.repository;

import java.time.ZonedDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.soft.src.parking.model.Time;

public interface TimeRepository extends JpaRepository<Time, Long> {

	Time findTimeByParkingLotIdxAndRoomIdxAndEndAfter(Long parkingLotIdx, Long roomIdx, ZonedDateTime time);

	Time findTimeByUserIdxAndEndAfter(Long userIdx, ZonedDateTime time);
}
