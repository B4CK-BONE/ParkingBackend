package cat.soft.src.parking.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PutUserInfoReq {
	private String car;
	private String phone;
	private String address;
	private String kakao;
}
