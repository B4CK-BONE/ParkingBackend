package cat.soft.src.oauth.util;

import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class BaseResponse<T> {
	@JsonProperty("isSuccess")
	private final Boolean isSuccess;
	private final String message;
	private final int code;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T result;

	// 요청에 성공한 경우
	public BaseResponse(T result) {
		this.isSuccess = BaseResponseStatus.SUCCESS.isSuccess();
		this.message = BaseResponseStatus.SUCCESS.getMessage();
		this.code = BaseResponseStatus.SUCCESS.getCode();
		this.result = result;
	}

	// 요청에 실패한 경우
	public BaseResponse(BaseResponseStatus status) {
		this.isSuccess = status.isSuccess();
		this.message = status.getMessage();
		this.code = status.getCode();
	}

	// 요청에 실패한 경우
	public BaseResponse(MethodArgumentNotValidException exception) {
		this.isSuccess = false;
		this.message = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		this.code = 400;
	}
}

