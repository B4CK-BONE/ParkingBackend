package cat.soft.src.parking.model;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "Report")
public class Report {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idx", nullable = false)
	private Long idx;

	@Column(name = "victim", nullable = false)
	private Long victim;

	@Column(name = "suspect", nullable = false)
	private Long suspect;

	@Column(name = "time", nullable = false)
	private ZonedDateTime time;

	@Builder
	public Report(Long victim, Long suspect, ZonedDateTime time) {
		this.victim = victim;
		this.suspect = suspect;
		this.time = time;
	}
}
