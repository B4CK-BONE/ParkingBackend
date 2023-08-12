package cat.soft.src.parking.model.room;

import java.util.List;

import cat.soft.src.parking.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GetUserListByAdminRes {
	private List<UserInfo> newUser;
	private List<UserInfo> oldUser;
}