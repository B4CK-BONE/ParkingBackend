package cat.soft.src.oauth.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends Exception {
	private BaseResponseStatus status;

	public BaseException(BaseResponseStatus baseResponseStatus) {
	}
}
