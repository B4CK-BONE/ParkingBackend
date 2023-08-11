package cat.soft.src.parking.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UsersController {

	@Autowired
	private UsersService usersService;

	@GetMapping("/a")
	public String testRes() {
		return "hi";
	}
}
