package cat.soft.src.oauth.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cat.soft.src.parking.model.ParkingLot;

//TODO: ParkingLot 자동 생성 추후에 동적으로 생성시 제거
public class ParkingLotCreator {
	public static List<ParkingLot> getParkingLotCreator(Long roomIdx) {
		List<ParkingLot> parkingLots = new ArrayList<>();
		List<String> bottom = Arrays.asList("56vh", "50vh", "44vh", "56vh", "50vh", "44vh", "40vh", "51vh", "51vh",
			"40vh", "28vh", "28vh");
		List<String> rigth = Arrays.asList("70%", "70%", "70%", "12%", "12%", "12%", "40%", "55%", "40%",
			"55%", "40%", "55%");
		List<String> height = Arrays.asList("5vh", "5vh", "5vh", "5vh", "5vh", "5vh", "10vh", "10vh", "10vh", "10vh",
			"10vh", "10vh");
		List<String> width = Arrays.asList("20vw", "20vw", "20vw", "20vw", "20vw", "20vw", "5vh", "5vh", "5vh",
			"5vh", "5vh", "5vh");
		for (int i = 0; i < 12; i++) {
			ParkingLot parkingLot = new ParkingLot();
			parkingLot.setHeight(height.get(i));
			parkingLot.setWidth(width.get(i));
			parkingLot.setBottom(bottom.get(i));
			parkingLot.setRight(rigth.get(i));
			parkingLot.setSlot((long)(i + 1));
			parkingLot.setDirection("aa");
			parkingLot.setRoomIdx(roomIdx);
			parkingLots.add(parkingLot);
		}
		return parkingLots;
	}
}
