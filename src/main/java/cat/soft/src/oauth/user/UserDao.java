package cat.soft.src.oauth.user;

import cat.soft.src.oauth.auth.dto.PostUserAuthRes;
import cat.soft.src.oauth.user.model.User;
import cat.soft.src.oauth.util.EncryptionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Objects;

@Repository
public class UserDao {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public UserDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public User insertUser(User user) {
		String insertUserQuery = "insert into User (email, pw) values (?,?)";
		Object[] insertUserParams = new Object[] {user.getEmail(), EncryptionUtils.encryptSHA256(user.getEmail())};

		this.jdbcTemplate.update(insertUserQuery, insertUserParams);

		String lastInsertEmail = this.jdbcTemplate.queryForObject("select last_insert_id()", String.class);

		user.setEmail(lastInsertEmail);

		return user;
	}

	public User selectByEmail(String email) {
		String selectByEmailQuery = "select email from User where email = ?";
		Object[] selectByEmailParams = new Object[] {email};
		try {
			return this.jdbcTemplate.queryForObject(selectByEmailQuery,
				(rs, rowNum) -> new User(
					rs.getString("email")),
				selectByEmailParams);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public String tokenByEmail(String email) {
		String selectByEmailQuery = "select refresh_token from User where email = ?";
		Object[] selectByEmailParams = new Object[] {email};
		try {
			return Objects.requireNonNull(this.jdbcTemplate.queryForObject(selectByEmailQuery,
					(rs, rowNum) ->
							rs.getString("refresh_token"),
					selectByEmailParams)).toString();
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public int checkEmail(String email) {
		String checkEmailQuery = "select exists(select email from User where email = ?)";
		Object[] checkEmailParams = new Object[] {email};
		return this.jdbcTemplate.queryForObject(checkEmailQuery, int.class, checkEmailParams);
	}

	public PostUserAuthRes userInfo(String email) {
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
			return null;
		}
	}
}
