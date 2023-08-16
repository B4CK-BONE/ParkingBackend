package cat.soft.src.parking.model.parking;

import java.time.ZonedDateTime;

import cat.soft.src.parking.model.Report;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostReportReq {
	@NotNull(message = "신고할 유저를 선택하세요.")
	@Min(value = 1, message = "신고할 유저 정보를 확인하세요.")
	@Max(value = Integer.MAX_VALUE, message = "신고할 유저 정보를 확인하세요.")
	private Long suspect;

	public Report toEntity(Long victim) {
		return Report.builder()
			.suspect(suspect)
			.victim(victim)
			.time(ZonedDateTime.now())
			.build();
	}
}
