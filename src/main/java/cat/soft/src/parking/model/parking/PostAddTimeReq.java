package cat.soft.src.parking.model.parking;

import java.time.ZonedDateTime;

import cat.soft.src.parking.model.Time;
import jakarta.validation.constraints.Future;
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
public class PostAddTimeReq {
	@NotNull(message = "시간을 입력하세요")
	@Future(message = "시간을 확인하세요")
	private ZonedDateTime time;
	@NotNull(message = "주차할 공간을 선택하세요.")
	@Min(value = 1, message = "주차 공간 위치를 확인하세요.")
	@Max(value = Integer.MAX_VALUE, message = "주차 공간 위치를 확인하세요.")
	private Integer slot;

	public Time toEntity(int userIdx) {
		return Time.builder()
			.parkingLotIdx(slot)
			.userIdx(userIdx)
			.start(ZonedDateTime.now())
			.end(time)
			.build();
	}
}
