package cat.soft.src.oauth.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomAdminVerify {
	private Integer roomIdx;
	private Integer role;
}
