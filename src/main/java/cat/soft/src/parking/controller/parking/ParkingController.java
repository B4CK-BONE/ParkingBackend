package cat.soft.src.parking.controller.parking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.soft.src.oauth.auth.jwt.JwtTokenProvider;
import cat.soft.src.oauth.util.BaseResponse;
import cat.soft.src.oauth.util.BaseResponseStatus;
import cat.soft.src.parking.model.parking.GetTimeRes;
import cat.soft.src.parking.model.parking.PostAddTimeReq;
import cat.soft.src.parking.model.parking.PostAddTimeRes;
import cat.soft.src.parking.model.parking.PostReportReq;
import cat.soft.src.parking.model.parking.PostReportRes;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

	@Autowired
	private ParkingService parkingService;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public BaseResponse<MethodArgumentNotValidException> handleValidationExceptions(
		MethodArgumentNotValidException ex) {

		return new BaseResponse<>(ex);
	}

	@GetMapping("")
	public BaseResponse<List<GetTimeRes>> getTime(@RequestHeader("Authorization") String token) {
		jwtTokenProvider.verifySignature(token);
		List<GetTimeRes> getTimeRes = parkingService.getTime(token);
		if (getTimeRes == null) {
			return new BaseResponse<>(BaseResponseStatus.DATABASE_ERROR);
		}
		return new BaseResponse<>(getTimeRes);
	}

	@PostMapping("/time")
	public BaseResponse<PostAddTimeRes> addTime(@Valid @RequestBody PostAddTimeReq req,
		@RequestHeader("Authorization") String token) {
		jwtTokenProvider.verifySignature(token);
		PostAddTimeRes postAddTimeRes = parkingService.addTime(req, token);
		if (postAddTimeRes == null) {
			return new BaseResponse<>(BaseResponseStatus.DATABASE_ERROR);
		}
		return new BaseResponse<>(postAddTimeRes);
	}

	@PostMapping("/report")
	public BaseResponse<PostReportRes> report(@Valid @RequestBody PostReportReq req,
		@RequestHeader("Authorization") String token) {
		jwtTokenProvider.verifySignature(token);
		PostReportRes postReportRes = parkingService.report(req, token);
		if (postReportRes == null) {
			return new BaseResponse<>(BaseResponseStatus.DATABASE_ERROR);
		}
		return new BaseResponse<>(postReportRes);
	}
}
