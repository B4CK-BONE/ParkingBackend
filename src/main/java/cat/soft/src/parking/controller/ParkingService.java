package cat.soft.src.parking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import cat.soft.src.parking.model.TestRes;
import cat.soft.src.parking.repository.ParkingRepository;

@Service
public class ParkingService {

	@Autowired
	private ParkingRepository parkingRepository;

	public TestRes testText(@PathVariable("test") String test){
		return new TestRes("test text" + test);
	}
}
