package cat.soft.src.parking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "User")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idx", nullable = false)
	private Long idx;

	@Column(name = "room_idx", nullable = false)
	private String roomIdx;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "car", length = 10)
	private String car;

	@Column(name = "phone", length = 11)
	private String phone;

	@Column(name = "address", length = 5)
	private String address;

	@Column(name = "role", nullable = false)
	private Long role;
}
