package cat.soft.src.parking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.soft.src.parking.model.ParkingLot;

public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {
	ParkingLot findBySlotAndRoomIdx(Long slot, Long roomIdx);

	List<ParkingLot> findAllByRoomIdx(Long roomIdx);
}
