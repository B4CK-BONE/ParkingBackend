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
	private ZonedDateTime time;
	private Integer slot;

	public Time toEntity() {
		return Time.builder()
			.parkingLotIdx(slot)
			.start(ZonedDateTime.now())
			.end(time)
			.build();
	}
}
