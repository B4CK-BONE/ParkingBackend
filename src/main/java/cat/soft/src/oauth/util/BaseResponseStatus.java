package cat.soft.src.oauth.util;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
	/**
	 * 1000 : 요청 성공
	 */
	SUCCESS(true, 1000, "요청에 성공하였습니다."),

	/**
	 * 2000 : Request 오류
	 */
	// Common
	REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
	EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
	INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
	EXPIRED_JWT(false, 2003, "만료된 JWT입니다."),
	INVALID_USER_JWT(false, 2004, "권한이 없는 유저의 접근입니다."),
	INVALID_AUTH(false, 2005, "유효하지 않은 회원 정보입니다."),
	DIFFERENT_REFRESH_TOKEN(false, 2006, "유저 정보와 일치하지 않는 refresh token입니다."),

	// users
	USERS_EMPTY_USER_EMAIL(false, 2011, "유저 이메일 값을 확인해주세요."),

	INVALID_PROVIDER(false, 2013, "올바르지 않은 provider. (예: google)"),

	USERS_CATEGORY_NOT_EXISTS(false, 2014, "해당하는 유저와 일치하는 카테고리가 없습니다."),

	// [POST] /users
	POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
	POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
	POST_USERS_EXISTS_EMAIL(false, 2017, "중복된 이메일입니다."),

	/**
	 * 3000 : Response 오류
	 */
	// Common
	RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

	// [POST] /users
	FAILED_TO_LOGIN(false, 3014, "없는 아이디거나 비밀번호가 틀렸습니다."),

	/**
	 * 4000 : Database, Server 오류
	 */
	DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
	SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

	DELETE_USER_FAIL(false, 4008, "유저 정보 삭제에 실패하였습니다."),

	PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
	PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

	// 5000 :
	UNKNOWN(false, 5000, "사이트 관리자에게 문의하세요."),
	ALREADY_ALLOWED(false, 5001, "이미 승인된 유저 입니다."),
	ALLOW_WAITING(false, 5002, "방장의 승인을 대기중 입니다."),
	ALLOW_DENIED(false, 5003, "승인이 거절 되었습니다."),
	UNKNOWN2(false, 5004, "사이트 관리자에게 문의하세요."),
	UNKNOWN3(false, 5005, "사이트 관리자에게 문의하세요."),
	UNKNOWN4(false, 5006, "사이트 관리자에게 문의하세요."),
	USING_LOT(false, 5007, "주차 공간이 이미 사용중 입니다."),
	SAME_USER(false, 5008, "자기 자신을 신고할 수 없습니다."),
	UNKNOWN7(false, 5009, "사이트 관리자에게 문의하세요."),
	UNKNOWN8(false, 5010, "사이트 관리자에게 문의하세요."),
	UNKNOWN9(false, 5011, "사이트 관리자에게 문의하세요."),
	ALREADY_REPORT(false, 5012, "24시간에 한 번만 신고 가능합니다.");
	// 6000 : 필요시 만들어서 쓰세요

	private final boolean isSuccess;
	private final int code;
	private final String message;

	private BaseResponseStatus(boolean isSuccess, int code, String message) {
		this.isSuccess = isSuccess;
		this.code = code;
		this.message = message;
	}
}
