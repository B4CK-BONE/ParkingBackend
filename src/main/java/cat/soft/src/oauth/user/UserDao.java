package cat.soft.src.oauth.user;

import static cat.soft.src.oauth.util.BaseResponseStatus.*;

import java.util.Date;
import java.util.Objects;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cat.soft.src.oauth.auth.dto.PostUserAuthRes;
import cat.soft.src.oauth.user.model.User;
import cat.soft.src.oauth.util.BaseException;
import cat.soft.src.oauth.util.EncryptionUtils;

@Repository
public class UserDao {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public UserDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public synchronized User insertUser(User user) throws BaseException {
		String insertUserQuery = "insert into User (email, pw) values (?,?)";
		Object[] insertUserParams = new Object[] {user.getEmail(), EncryptionUtils.encryptSHA256(user.getEmail())};
		try {
			this.jdbcTemplate.update(insertUserQuery, insertUserParams);

			String lastInsertEmail = this.jdbcTemplate.queryForObject("select last_insert_id()", String.class);

			user.setEmail(lastInsertEmail);

			return user;
		} catch (EmptyResultDataAccessException e){
			throw new BaseException(DATABASE_ERROR);
		}
	}

	public Integer selectIdxByEmail(String email) throws BaseException {
		String selectIdxByEmailQuery = "select idx from User where email=?";
		Object[] selectIdxByEmailParams = new Object[] {email};

		try {
			return Objects.requireNonNull(this.jdbcTemplate.queryForObject(selectIdxByEmailQuery,
				(rs, rowNum) ->
					rs.getInt("idx"),
				selectIdxByEmailParams));
		} catch (EmptyResultDataAccessException e) {
			throw new BaseException(DATABASE_ERROR);
		}
	}

	public synchronized void insertUserInfo(String email) throws BaseException {
		String insertUserInfoQuery = "insert into UserInfo (idx) values (?)";
		Object[] insertUserInfoParams = new Object[] {this.selectIdxByEmail(email)};
		try {
			this.jdbcTemplate.update(insertUserInfoQuery, insertUserInfoParams);
		} catch (EmptyResultDataAccessException e){
			throw new BaseException(DATABASE_ERROR);
		}
	}

	public User selectByEmail(String email) throws BaseException {
		String selectByEmailQuery = "select email from User where email = ?";
		Object[] selectByEmailParams = new Object[] {email};
		try {
			return this.jdbcTemplate.queryForObject(selectByEmailQuery,
				(rs, rowNum) -> new User(
					rs.getString("email")),
				selectByEmailParams);
		} catch (EmptyResultDataAccessException e) {
			throw new BaseException(DATABASE_ERROR);
		}
	}

	public String tokenByEmail(String email) throws BaseException {
		String selectByEmailQuery = "select refresh_token from User where email = ?";
		Object[] selectByEmailParams = new Object[] {email};
		try {
			return Objects.requireNonNull(this.jdbcTemplate.queryForObject(selectByEmailQuery,
				(rs, rowNum) ->
					rs.getString("refresh_token"),
				selectByEmailParams)).toString();
		} catch (EmptyResultDataAccessException e) {
			throw new BaseException(DATABASE_ERROR);
		}
	}

	public synchronized void LogoutUser(String email) throws BaseException {
		String LogoutUserQuery = "update User set refresh_token = NULL where email = ?";
		Object[] LogoutUserParams = new Object[] {email};
		try {
			this.jdbcTemplate.update(LogoutUserQuery, LogoutUserParams);
		} catch (EmptyResultDataAccessException e) {
			throw new BaseException(DATABASE_ERROR);
		}
	}

	public int checkEmail(String email) throws BaseException{
		String checkEmailQuery = "select exists(select email from User where email = ?)";
		Object[] checkEmailParams = new Object[] {email};
		try {
			return this.jdbcTemplate.queryForObject(checkEmailQuery, int.class, checkEmailParams);
		} catch (EmptyResultDataAccessException e){
			throw  new BaseException(DATABASE_ERROR);
		}
	}

	public PostUserAuthRes userInfo(String email) throws BaseException {
		String getUserInfoQuery = "select idx, room_idx, role from User where email=?";
		Object[] getUserInfoParams = new Object[] {email};

		try {
			return this.jdbcTemplate.queryForObject(getUserInfoQuery,
				(rs, rowNum) -> new PostUserAuthRes(
					rs.getInt("idx"),
					rs.getInt("room_idx"),
					rs.getInt("role")),
				getUserInfoParams);
		} catch (EmptyResultDataAccessException e) {
			throw  new BaseException(DATABASE_ERROR);
		}
	}

	public synchronized void insertSurvey(String context, String img, String email) throws BaseException{
		String insertSurveyQuery1 = "insert into Survey (contents, img, user_idx, room_idx) values (?,?,(select idx from User where email=?),(select room_idx from User where email=?))";
		Object[] insertSurveyParams1 = new Object[] {context, img, email, email};
		try {
			this.jdbcTemplate.update(insertSurveyQuery1, insertSurveyParams1);
		} catch (EmptyResultDataAccessException e){
			throw  new BaseException(DATABASE_ERROR);
		}
	}

	public synchronized Date selectSurvey(String email) throws BaseException{
		String selectSurveyQuery = "select time from Survey where idx=(select idx from User where email=?)";
		Object[] selectSurveyParams = new Object[] {email};
		try {
			return this.jdbcTemplate.queryForObject(selectSurveyQuery,
					(rs, rowNum) ->
							rs.getDate("time"),
							selectSurveyParams);
		} catch (EmptyResultDataAccessException e){
			throw  new BaseException(DATABASE_ERROR);
		}
	}
}
