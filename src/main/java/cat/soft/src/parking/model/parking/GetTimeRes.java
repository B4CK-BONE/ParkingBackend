package cat.soft.src.parking.model.parking;

import java.time.format.DateTimeFormatter;

import cat.soft.src.parking.model.ParkingLot;
import cat.soft.src.parking.model.Time;
import cat.soft.src.parking.model.UserInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTimeRes {

	private String height;
	private String bottom;
	private String width;
	private String right;
	private String direction;
	private String endDate;
	private String endTime;
	private String car;

	private Integer userIdx;
	private Integer slot;
	private Boolean use;

	private String kakao;

	public GetTimeRes(UserInfo userInfo, ParkingLot parkingLot, Time time) {
		this.height = parkingLot.getHeight();
		this.bottom = parkingLot.getBottom();
		this.width = parkingLot.getWidth();
		this.right = parkingLot.getRight();
		this.direction = parkingLot.getDirection();
		this.slot = parkingLot.getSlot();
		if (time != null) {
			this.endDate = time.getEnd().format(DateTimeFormatter.ofPattern("MM/dd"));
			this.endTime = time.getEnd().format(DateTimeFormatter.ofPattern("HH:mm"));
			this.use = true;
			this.car = userInfo.getCar();
			this.userIdx = userInfo.getIdx();
			this.kakao = userInfo.getKakao();
		} else {
			this.endDate = null;
			this.endTime = null;
			this.use = false;
			this.car = null;
			this.userIdx = null;
			this.kakao = null;
		}
	}
}
