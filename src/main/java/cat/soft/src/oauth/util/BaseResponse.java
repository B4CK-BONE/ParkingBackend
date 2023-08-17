package cat.soft.src.oauth.util;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException;

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

	public BaseResponse(T result) {
		this.isSuccess = BaseResponseStatus.SUCCESS.isSuccess();
		this.message = BaseResponseStatus.SUCCESS.getMessage();
		this.code = BaseResponseStatus.SUCCESS.getCode();
		this.result = result;
	}

	public BaseResponse() {
		this.isSuccess = BaseResponseStatus.SUCCESS.isSuccess();
		this.message = BaseResponseStatus.SUCCESS.getMessage();
		this.code = BaseResponseStatus.SUCCESS.getCode();
	}

	public BaseResponse(BaseResponseStatus status) {
		this.isSuccess = status.isSuccess();
		this.message = status.getMessage();
		this.code = status.getCode();
	}

	public BaseResponse(MethodArgumentNotValidException exception) {
		this.isSuccess = false;
		this.message = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		this.code = 400;
	}

	public BaseResponse(HttpMessageNotReadableException exception) {
		this.isSuccess = false;
		this.message = "사이트 관리자에게 문의하세요.";
		this.code = 401;
	}

	public BaseResponse(MySQLTransactionRollbackException exception) {
		this.isSuccess = false;
		this.message = "처리중 입니다.";
		this.code = 402;
	}
}

