package cat.soft.src.parking.model.room;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetJoinRoomReq {
	@NotNull(message = "방 번호를 입력하세요.")
	@Min(value = 0, message = "방 번호를 확인하세요")
	@Max(value = Integer.MAX_VALUE, message = "방 번호를 확인하세요.")
	private Long room_id;
}
