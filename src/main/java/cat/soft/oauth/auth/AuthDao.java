package cat.soft.oauth.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AuthDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String insertRefreshToken(String useremail, String refreshToken) {
        String insertRefreshTokenQuery = "insert into token(user_email, refresh_token) values (?,?)";
        Object[] insertRefreshTokenParams = new Object[]{useremail, refreshToken};

        this.jdbcTemplate.update(insertRefreshTokenQuery, insertRefreshTokenParams);

        return useremail;
    }

    public String updateRefreshToken(String useremail, String newRefreshToken) {
        String updateRefreshTokenQuery = "update token set refresh_token = ? where user_email=?";
        Object[] updateRefreshTokenParams = new Object[]{newRefreshToken,useremail};

        this.jdbcTemplate.update(updateRefreshTokenQuery, updateRefreshTokenParams);

        return newRefreshToken;
    }

    public boolean checkRefreshToken(String token) {
        String checkRefreshTokenQuery = "select exists(select refresh_token from token where refresh_token = ?)";

        int result = this.jdbcTemplate.queryForObject(checkRefreshTokenQuery,int.class,token);

        return result == 1;
    }

    public boolean checkUser(String useremail) {
        String checkUserQuery = "select exists(select useremail from token where useremail=?)";

        int result = this.jdbcTemplate.queryForObject(checkUserQuery, int.class, useremail);

        return result == 1;
    }
}
