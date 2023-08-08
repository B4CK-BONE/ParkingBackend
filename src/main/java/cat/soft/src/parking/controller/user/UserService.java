package cat.soft.src.parking.controller.user;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import cat.soft.src.parking.model.User;
import cat.soft.src.parking.model.user.PutUserInfoReq;
import cat.soft.src.parking.model.user.PutUserInfoRes;
import cat.soft.src.parking.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public PutUserInfoRes updateUserInfo(@PathVariable Integer id, @RequestBody PutUserInfoReq userInfoReq) {
		User user;
		try {
			user = userRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			return new PutUserInfoRes(0);
		}
		user.setCar(userInfoReq.getCar());
		user.setPhone(userInfoReq.getPhone());
		user.setAddress(userInfoReq.getAddress());
		userRepository.save(user);
		return new PutUserInfoRes(user.getIdx());
	}
}
