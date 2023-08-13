package cat.soft.src.parking.model.parking;

import java.time.ZonedDateTime;

import cat.soft.src.parking.model.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PostReportReq {
	private Integer suspect;

	public Report toEntity() {
		return Report.builder()
			.suspect(suspect)
			.time(ZonedDateTime.now())
			.build();
	}
}
