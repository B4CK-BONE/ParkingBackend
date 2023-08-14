package cat.soft.src.oauth.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.relation.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	private String email;
	private Role role;

	public User(String email) {
		this.email = email;
	}
}
