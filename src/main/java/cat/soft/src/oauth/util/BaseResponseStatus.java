package cat.soft.src.oauth.util;

import static cat.soft.src.oauth.util.Constant.*;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
	SUCCESS(true, 1000, "요청에 성공하였습니다."),
	EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
	INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
	INVALID_USER_JWT(false, 2004, "권한이 없는 유저의 접근입니다."),
	INVALID_AUTH(false, 2005, "유효하지 않은 회원 정보입니다."),
	USERS_EMPTY_USER_EMAIL(false, 2011, "유저 이메일 값을 확인해주세요."),
	POST_USERS_EXISTS_EMAIL(false, 2017, "중복된 이메일입니다."),
	DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
	DELETE_USER_FAIL(false, 4008, "유저 정보 삭제에 실패하였습니다."),
	UNKNOWN(false, 5000, ASK_ADMIN),
	ALREADY_ALLOWED(false, 5001, "이미 승인된 유저 입니다."),
	ALLOW_WAITING(false, 5002, "방장의 승인을 대기중 입니다."),
	ALLOW_DENIED(false, 5003, "승인이 거절 되었습니다."),
	UNKNOWN2(false, 5004, ASK_ADMIN),
	UNKNOWN3(false, 5005, ASK_ADMIN),
	UNKNOWN4(false, 5006, ASK_ADMIN),
	USING_LOT(false, 5007, "주차 공간이 이미 사용중 입니다."),
	SAME_USER(false, 5008, "자기 자신을 신고할 수 없습니다."),
	UNKNOWN7(false, 5009, ASK_ADMIN),
	UNKNOWN8(false, 5010, ASK_ADMIN),
	UNKNOWN9(false, 5011, ASK_ADMIN),
	ALREADY_REPORT(false, 5012, "24시간에 한 번만 신고 가능합니다."),
	USING_USER(false, 5013, "이미 주차를 했습니다."),
	UNKNOWN10(false, 5014, ASK_ADMIN),
	NOT_USING(false, 5015, "주차를 하지 않았습니다."),
	UNKNOWN11(false, 5016, ASK_ADMIN),
	UNKNOWN12(false, 5017, ASK_ADMIN),
	UNKNOWN13(false, 5018, ASK_ADMIN),
	UNKNOWN14(false, 5019, ASK_ADMIN),
	UNKNOWN15(false, 5020, ASK_ADMIN),
	UNKNOWN16(false, 5021, ASK_ADMIN),
	NO_ROOM(false, 5022, "존재하지 않는 방입니다."),
	NO_PAGE_AUTH(false, 5023, "방장만 사용가능 합니다."),
	UNKNOWN18(false, 5024, ASK_ADMIN),
	UNKNOWN19(false, 5025, ASK_ADMIN),
	UNKNOWN20(false, 5026, ASK_ADMIN),
	UNKNOWN21(false, 5027, ASK_ADMIN),
	UNKNOWN22(false, 5028, ASK_ADMIN),
	UNKNOWN23(false, 5029, ASK_ADMIN),
	UNKNOWN24(false, 5030, ASK_ADMIN),
	SURVEY_DAY(false, 5031, "설문은 하루에 한 번만 가능 합니다.");;

	private final boolean isSuccess;
	private final int code;
	private final String message;

	private BaseResponseStatus(boolean isSuccess, int code, String message) {
		this.isSuccess = isSuccess;
		this.code = code;
		this.message = message;
	}
}
