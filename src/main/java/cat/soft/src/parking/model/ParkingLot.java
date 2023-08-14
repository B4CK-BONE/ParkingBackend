package cat.soft.src.parking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

	@Column(name = "height", nullable = false)
	private String height;

	@Column(name = "bottom", nullable = false)
	private String bottom;

	@Column(name = "width", nullable = false)
	private String width;

	@Column(name = "right_side", nullable = false)
	private String right;

	@Column(name = "direction", nullable = false)
	private String direction;
}
