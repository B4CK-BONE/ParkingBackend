package cat.soft.src.parking.model.parking;

import java.time.ZonedDateTime;

import cat.soft.src.parking.model.Time;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostAddTimeReq {
	private Integer userIdx;
	private ZonedDateTime time;
	private Integer slot;

	public Time toEntity() {
		return Time.builder()
			.userIdx(userIdx)
			.parkingLotIdx(slot)
			.start(ZonedDateTime.now())
			.end(time)
			.build();
	}
}
