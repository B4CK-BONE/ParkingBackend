package cat.soft.src.parking.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.soft.src.parking.repository.UserRepository;

@Service
public class UsersService {

	@Autowired
	private UserRepository userRepository;

}
