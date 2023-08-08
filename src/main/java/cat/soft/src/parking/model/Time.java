package cat.soft.src.parking.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Time")
public class Time {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idx", nullable = false)
	private Integer idx;

	@Column(name = "user_idx", nullable = false)
	private Integer userIdx;

	@Column(name = "room_idx", nullable = false)
	private Integer roomIdx;

	@Column(name = "parking_lot_idx", nullable = false)
	private Integer parkingLotIdx;

	@CreationTimestamp
	@Column(name = "start", nullable = false)
	private LocalDateTime start;

	@Column(name = "end", nullable = false)
	private LocalDateTime end;
}
