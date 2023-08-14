package cat.soft.src.parking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.soft.src.parking.model.ParkingLot;

public interface ParkingLotRepository extends JpaRepository<ParkingLot, Integer> {
	ParkingLot findBySlotAndRoomIdx(Integer slot, Integer roomIdx);

	List<ParkingLot> findAllByRoomIdx(Integer roomIdx);
}
