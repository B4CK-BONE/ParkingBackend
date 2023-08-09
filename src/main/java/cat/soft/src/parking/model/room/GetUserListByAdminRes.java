package cat.soft.src.parking.model.room;

import java.util.List;

import cat.soft.src.parking.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GetUserListByAdminRes {
	private List<User> newUser;
	private List<User> oldUser;
}
