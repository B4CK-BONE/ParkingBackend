package cat.soft.src.parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.soft.src.parking.model.ParkingLot;

public interface ParkingRepository extends JpaRepository<ParkingLot, Integer> {
}
