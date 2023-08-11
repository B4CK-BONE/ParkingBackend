package cat.soft.src.parking.model.parking;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PostReportRes {
	private ZonedDateTime time;
}
