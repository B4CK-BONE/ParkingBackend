package cat.soft.src.parking.controller.parking;

import static cat.soft.src.oauth.util.Constant.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException;

import cat.soft.src.oauth.auth.jwt.JwtTokenProvider;
import cat.soft.src.oauth.util.BaseException;
import cat.soft.src.oauth.util.BaseResponse;
import cat.soft.src.oauth.util.BaseResponseStatus;
import cat.soft.src.parking.model.parking.DelTimeRes;
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

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public BaseResponse<HttpMessageNotReadableException> handleNotReadableException(
		HttpMessageNotReadableException ex) {
		return new BaseResponse<>(ex);
	}

	@ExceptionHandler(MySQLTransactionRollbackException.class)
	public BaseResponse<MySQLTransactionRollbackException> handleNotReadableException(
		MySQLTransactionRollbackException ex) {
		return new BaseResponse<>(ex);
	}

	@GetMapping("")
	public BaseResponse<List<GetTimeRes>> getTime(@RequestHeader("Authorization") String token) throws BaseException {
		jwtTokenProvider.verifySignature(token);
		List<GetTimeRes> getTimeRes = parkingService.getTime(token);
		if (getTimeRes == null) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN);
		}
		if (getTimeRes.size() == 0) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN2);
		}
		return new BaseResponse<>(getTimeRes);
	}

	@PostMapping("/time")
	public BaseResponse<PostAddTimeRes> addTime(@Valid @RequestBody PostAddTimeReq req,
		@RequestHeader("Authorization") String token) throws BaseException {
		jwtTokenProvider.verifySignature(token);
		PostAddTimeRes postAddTimeRes = parkingService.addTime(req, token);
		if (postAddTimeRes == null) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN3);
		}
		if (postAddTimeRes.getStart().getZone() == USING_USER) {
			return new BaseResponse<>(BaseResponseStatus.USING_USER);
		}
		if (postAddTimeRes.getStart().getZone() == NO_SLOT) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN4);
		}
		if (postAddTimeRes.getStart().getZone() == USING_SLOT) {
			return new BaseResponse<>(BaseResponseStatus.USING_LOT);
		}
		// if (postAddTimeRes.getStart().getZone() == NOT_MINE) {
		// 	return new BaseResponse<>(BaseResponseStatus.NOT_MINE);
		// }
		return new BaseResponse<>(postAddTimeRes);
	}

	@DeleteMapping("/time")
	public BaseResponse<DelTimeRes> deleteTime(@RequestHeader("Authorization") String token) throws BaseException {
		jwtTokenProvider.verifySignature(token);
		DelTimeRes delTimeRes = parkingService.delTime(token);
		if (delTimeRes == null)
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN10);
		if (delTimeRes.getUserIdx() == -1)
			return new BaseResponse<>(BaseResponseStatus.NOT_USING);

		return new BaseResponse<>(delTimeRes);
	}

	@PostMapping("/report")
	public BaseResponse<PostReportRes> report(@Valid @RequestBody PostReportReq req,
		@RequestHeader("Authorization") String token) throws BaseException {
		jwtTokenProvider.verifySignature(token);
		PostReportRes postReportRes = parkingService.report(req, token);
		if (postReportRes == null) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN7);
		}
		if (postReportRes.getTime().getZone() == SAME_USER) {
			return new BaseResponse<>(BaseResponseStatus.SAME_USER);
		}
		if (postReportRes.getTime().getZone() == DIFF_ROOM) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN8);
		}
		if (postReportRes.getTime().getZone() == NO_PARKING) {
			return new BaseResponse<>(BaseResponseStatus.UNKNOWN9);
		}
		if (postReportRes.getTime().getZone() == ALREADY_REPORT) {
			return new BaseResponse<>(BaseResponseStatus.ALREADY_REPORT);
		}
		return new BaseResponse<>(postReportRes);
	}
}
