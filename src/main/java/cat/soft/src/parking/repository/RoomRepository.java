package cat.soft.src.parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.soft.src.parking.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
