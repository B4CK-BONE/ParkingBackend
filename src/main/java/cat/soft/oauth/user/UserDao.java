package cat.soft.oauth.user;


import cat.soft.oauth.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public User insertUser(User user) {
        String insertUserQuery = "insert into user (email,provider,provider_id) values (?,?,?)";
        Object[] insertUserParams = new Object[]{user.getEmail(), user.getProvider(), user.getProvider_id()};

        this.jdbcTemplate.update(insertUserQuery, insertUserParams);

        String lastInsertEmail = this.jdbcTemplate.queryForObject("select last_insert_email()", String.class);

        user.setEmail(lastInsertEmail);

        return user;
    }

    public User selectByEmail(String email) {
        String selectByEmailQuery = "select email, provider, provider_id from user where email = ?";
        Object[] selectByEmailParams = new Object[]{email};
        try {
            return this.jdbcTemplate.queryForObject(selectByEmailQuery,
                    (rs, rowNum) -> new User(
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("provider"),
                            rs.getString("provider_id")),
                    selectByEmailParams);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select email from user where email = ?)";
        Object[] checkEmailParams = new Object[]{email};
        return this.jdbcTemplate.queryForObject(checkEmailQuery, int.class, checkEmailParams);
    }
}
