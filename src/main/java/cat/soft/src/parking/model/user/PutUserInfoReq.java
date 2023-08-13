package cat.soft.src.parking.model.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PutUserInfoReq {
	@NotBlank(message = "차 번호를 필수로 입력해야 합니다.")
	@Pattern(regexp = "^\\d{2,3}[가-힣]\\d{4}$", message = "차 번호를 확인하세요.")
	private String car;

	@NotBlank(message = "전화 번호를 필수로 입력해야 합니다.")
	@Pattern(regexp = "^\\d{11}$", message = "전화 번호는 11자리 숫자 입니다.")
	private String phone;

	@NotBlank(message = "호실 번호를 필수로 입력해야 합니다.")
	@Pattern(regexp = "^\\d{1,5}$", message = "호실 번호는 1~5자리 숫자 입니다.")
	private String address;

	@Pattern(regexp = "^[a-zA-Z0-9]{8}$", message = "오픈채팅 프로필은 영문+숫자 8자리 입니다.")
	private String kakao;
}
