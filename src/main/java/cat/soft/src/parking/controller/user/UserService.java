package cat.soft.src.parking.controller.user;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import cat.soft.src.oauth.auth.jwt.JwtTokenProvider;
import cat.soft.src.parking.model.User;
import cat.soft.src.parking.model.UserInfo;
import cat.soft.src.parking.model.user.GetUserInfoRes;
import cat.soft.src.parking.model.user.PutUserInfoReq;
import cat.soft.src.parking.model.user.PutUserInfoRes;
import cat.soft.src.parking.repository.UserInfoRepository;
import cat.soft.src.parking.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserInfoRepository userInfoRepository;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	public PutUserInfoRes updateUserInfo(@PathVariable Integer id, @RequestBody PutUserInfoReq userInfoReq,
		String token) {
		User user = userRepository.findUsersByEmail(jwtTokenProvider.getEmail(token));
		if (user == null) {
			return new PutUserInfoRes(0);
		}
		if (!Objects.equals(id, user.getIdx()))
			return new PutUserInfoRes(0);
		UserInfo userInfo = userInfoRepository.findById(user.getIdx()).orElse(null);
		if (userInfo == null) {
			return new PutUserInfoRes(0);
		}
		userInfo.setCar(userInfoReq.getCar());
		userInfo.setPhone(userInfoReq.getPhone());
		userInfo.setAddress(userInfoReq.getAddress());
		userInfo.setKakao(userInfoReq.getKakao());
		if (user.getRole() == -1) {
			user.setRole(0L);
			userRepository.save(user);
		}
		userInfoRepository.save(userInfo);
		return new PutUserInfoRes(user.getIdx());
	}

	public GetUserInfoRes getUserInfo(Integer id, String token) {
		User user = userRepository.findUsersByEmail(jwtTokenProvider.getEmail(token));
		if (user == null) {
			return null;
		}
		if (!Objects.equals(user.getIdx(), id))
			return null;
		UserInfo userInfo = userInfoRepository.findById(user.getIdx()).orElse(null);
		if (userInfo == null) {
			return null;
		}
		return new GetUserInfoRes(userInfo.getCar(), userInfo.getPhone(), userInfo.getAddress(), userInfo.getKakao());
	}
}
