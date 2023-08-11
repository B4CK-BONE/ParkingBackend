package cat.soft.src.parking.model;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "Report")
public class Report {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idx", nullable = false)
	private Integer idx;

	@Column(name = "victim", nullable = false)
	private Integer victim;

	@Column(name = "suspect", nullable = false)
	private Integer suspect;

	@Column(name = "time", nullable = false)
	private ZonedDateTime time;

	@Builder
	public Report(Integer victim, Integer suspect, ZonedDateTime time) {
		this.victim = victim;
		this.suspect = suspect;
		this.time = time;
	}
}
