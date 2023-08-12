package cat.soft.src.parking.model;

import org.hibernate.annotations.ColumnDefault;

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
@Table(name = "User")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idx", nullable = false)
	private Integer idx;

	@Column(name = "room_idx", nullable = false)
	@ColumnDefault("0")
	private Integer roomIdx;

	@Column(name = "email", nullable = false)
	private String email;

	// @Column(name = "car", length = 10)
	// private String car;
	//
	// @Column(name = "phone", length = 11)
	// private String phone;
	//
	// @Column(name = "address", length = 5)
	// private String address;

	@Column(name = "role", nullable = false)
	@ColumnDefault("0")
	private Long role;
}
