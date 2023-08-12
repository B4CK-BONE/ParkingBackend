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
@Setter
@Getter
@Table(name = "User")
public class UserInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idx", nullable = false)
	private Integer idx;

	@Column(name = "car", length = 10)
	private String car;

	@Column(name = "phone", length = 11)
	private String phone;

	@Column(name = "address", length = 5)
	private String address;

	@Column(name = "kakao", length = 8)
	private String kakao;

	private Integer reportCount;
}
