package cat.soft.src.parking.model.user;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GetUserInfoRes {
	private String car;
	private String phone;
	private String address;
	private String kakao;
	private ZonedDateTime endTime;
}
