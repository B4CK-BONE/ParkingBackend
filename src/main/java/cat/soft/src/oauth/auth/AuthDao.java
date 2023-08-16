package cat.soft.src.oauth.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Objects;

@Repository
public class AuthDao {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public AuthDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public String insertRefreshToken(String useremail, String refreshToken) {
		String insertRefreshTokenQuery = "update User set refresh_token = ? where email=?";
		Object[] insertRefreshTokenParams = new Object[] {refreshToken, useremail};

		this.jdbcTemplate.update(insertRefreshTokenQuery, insertRefreshTokenParams);

		return useremail;
	}

	public String updateRefreshToken(String useremail, String newRefreshToken) {
		String updateRefreshTokenQuery = "update User set refresh_token = ? where email=?";
		Object[] updateRefreshTokenParams = new Object[] {newRefreshToken, useremail};

		this.jdbcTemplate.update(updateRefreshTokenQuery, updateRefreshTokenParams);

		return newRefreshToken;
	}

	public boolean checkRefreshToken(String email) {
		String checkRefreshTokenQuery = "select refresh_token from User where email = ?";
		Object[] checkRefreshTokenParams = new Object[] {email};
		String result = Objects.requireNonNull(this.jdbcTemplate.queryForObject(checkRefreshTokenQuery,
				(rs, rowNum) ->
						rs.getString("refresh_token"),
				checkRefreshTokenParams)).toString();

		return !result.equals("NULL");
	}

	public boolean checkUser(String useremail) {
		String checkUserQuery = "select exists(select email from User where email=?)";

		int result = this.jdbcTemplate.queryForObject(checkUserQuery, int.class, useremail);

		return result == 1;
	}
}
