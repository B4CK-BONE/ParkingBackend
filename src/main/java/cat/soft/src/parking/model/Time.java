package cat.soft.src.parking.model;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "Time")
public class Time {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idx", nullable = false)
	private Long idx;

	@Column(name = "user_idx", nullable = false)
	private Long userIdx;

	@Column(name = "room_idx", nullable = false)
	private Long roomIdx;

	@Column(name = "parking_lot_idx", nullable = false)
	private Long parkingLotIdx;

	@CreationTimestamp
	@Column(name = "start", nullable = false)
	private ZonedDateTime start;

	@Column(name = "end", nullable = false)
	private ZonedDateTime end;

	@Builder
	public Time(Long userIdx, Long parkingLotIdx, ZonedDateTime start,
		ZonedDateTime end) {
		this.userIdx = userIdx;
		this.parkingLotIdx = parkingLotIdx;
		this.start = start;
		this.end = end;
	}
}
