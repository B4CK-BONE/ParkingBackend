package cat.soft.src.parking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ParkingLot")
public class ParkingLot {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idx", nullable = false)
	private Integer idx;
	
	@Column(name = "room_idx", nullable = false)
	private Integer roomIdx;

	@Column(name = "slot", nullable = false)
	private Integer slot;

	@Column(name = "top", nullable = false)
	private String top;

	@Column(name = "bottom", nullable = false)
	private String bottom;

	@Column(name = "left_side", nullable = false)
	private String left;

	@Column(name = "right_side", nullable = false)
	private String right;

	@Column(name = "direction", nullable = false)
	private String direction;
}
