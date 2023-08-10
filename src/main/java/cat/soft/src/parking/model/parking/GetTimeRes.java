package cat.soft.src.parking.model.parking;

import java.time.ZonedDateTime;

import cat.soft.src.parking.model.ParkingLot;
import cat.soft.src.parking.model.Time;
import cat.soft.src.parking.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTimeRes {

	private String top;
	private String bottom;
	private String left;
	private String right;
	private String direction;
	private ZonedDateTime start;
	private ZonedDateTime end;
	private String car;
	private Integer slot;
	private Boolean use;

	public GetTimeRes(User user, ParkingLot parkingLot, Time time) {
		this.top = parkingLot.getTop();
		this.bottom = parkingLot.getBottom();
		this.left = parkingLot.getLeft();
		this.right = parkingLot.getRight();
		this.direction = parkingLot.getDirection();
		this.slot = parkingLot.getSlot();
		if (time != null) {
			this.start = time.getStart();
			this.end = time.getEnd();
			this.use = true;
			this.car = user.getCar();
		} else {
			this.start = null;
			this.end = null;
			this.use = false;
			this.car = null;
		}
	}
}
