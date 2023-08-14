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

	@Column(name = "pw", nullable = false)
	private String pw;

	@Column(name = "refresh_token", columnDefinition = "TEXT")
	private String refreshToken;

	@Column(name = "role", nullable = false)
	@ColumnDefault("-1")
	private Long role;
}
