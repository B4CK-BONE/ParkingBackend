package cat.soft.src.parking.controller.user;

import java.time.ZonedDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import cat.soft.src.oauth.auth.jwt.JwtTokenProvider;
import cat.soft.src.parking.model.Time;
import cat.soft.src.parking.model.User;
import cat.soft.src.parking.model.UserInfo;
import cat.soft.src.parking.model.user.GetUserInfoRes;
import cat.soft.src.parking.model.user.PutUserInfoReq;
import cat.soft.src.parking.model.user.PutUserInfoRes;
import cat.soft.src.parking.repository.TimeRepository;
import cat.soft.src.parking.repository.UserInfoRepository;
import cat.soft.src.parking.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserInfoRepository userInfoRepository;
	@Autowired
	private TimeRepository timeRepository;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	public PutUserInfoRes updateUserInfo(@PathVariable Long id, @RequestBody PutUserInfoReq userInfoReq,
		String token) {
		User user = userRepository.findUsersByEmail(jwtTokenProvider.getEmail(token));
		if (user == null) {
			return null;
		}
		if (!Objects.equals(id, user.getIdx()))
			return new PutUserInfoRes(0L);
		UserInfo userInfo = userInfoRepository.findById(user.getIdx()).orElse(null);
		if (userInfo == null) {
			return null;
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

	public GetUserInfoRes getUserInfo(Long id, String token) {
		User user = userRepository.findUsersByEmail(jwtTokenProvider.getEmail(token));
		if (user == null) {
			return null;
		}
		if (!Objects.equals(user.getIdx(), id))
			return new GetUserInfoRes(null, null, null, null, null);
		UserInfo userInfo = userInfoRepository.findById(user.getIdx()).orElse(null);
		if (userInfo == null) {
			return null;
		}
		Time endTime = timeRepository.findTimeByUserIdxAndEndAfter(user.getIdx(), ZonedDateTime.now());
		if (endTime == null) {
			return new GetUserInfoRes(userInfo.getCar(), userInfo.getPhone(), userInfo.getAddress(),
				userInfo.getKakao(), null);
		}
		return new GetUserInfoRes(userInfo.getCar(), userInfo.getPhone(), userInfo.getAddress(), userInfo.getKakao(),
			endTime.getEnd());
	}
}
