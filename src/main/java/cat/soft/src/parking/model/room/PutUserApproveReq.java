package cat.soft.src.parking.model.room;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PutUserApproveReq {
	@NotNull(message = "승인할 유저를 선택하세요.")
	@Min(value = 1, message = "승인할 유저 정보를 확인하세요.")
	@Max(value = Integer.MAX_VALUE, message = "승인할 유저 정보를 확인하세요.")
	private Integer userIdx;

	@NotNull(message = "승인할 유저 역할을 선택하세요.")
	@Min(value = 0, message = "승인할 유저 정보를 확인하세요.")
	@Max(value = 1, message = "승인할 유저 정보를 확인하세요.")
	private Integer role;
}
