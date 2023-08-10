package cat.soft.src.parking.controller.parking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.soft.config.BaseResponse;
import cat.soft.config.BaseResponseStatus;
import cat.soft.src.parking.model.parking.PostAddTimeReq;
import cat.soft.src.parking.model.parking.PostAddTimeRes;
import cat.soft.src.parking.model.parking.TestRes;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

	@Autowired
	private ParkingService parkingService;

	@GetMapping(value = {"/test/{test}", "/test/", "/test"})
	public BaseResponse<TestRes> testRes(@PathVariable(required = false) String test) {
		if (test == null) {
			return new BaseResponse<>(BaseResponseStatus.DATABASE_ERROR);
		}
		TestRes testRes = parkingService.testText(test);
		return new BaseResponse<>(testRes);
	}

	@PostMapping("/time")
	public BaseResponse<PostAddTimeRes> addTime(@RequestBody PostAddTimeReq req) {
		PostAddTimeRes postAddTimeRes = parkingService.addTime(req);
		if (postAddTimeRes == null) {
			return new BaseResponse<>(BaseResponseStatus.DATABASE_ERROR);
		}
		return new BaseResponse<>(postAddTimeRes);
	}
}
