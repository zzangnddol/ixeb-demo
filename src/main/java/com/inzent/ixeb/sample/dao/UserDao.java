package com.inzent.ixeb.sample.dao;

import com.inzent.ixeb.sample.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDao extends JdbcDaoSupport {
    @Inject
    private DataSource dataSource;

    @PostConstruct
    public void postConstruct() {
        setDataSource(dataSource);
    }

    public User get(String id) {
        String sql = "SELECT * FROM USERFILE WHERE USER_ID = ?";
        return getJdbcTemplate().queryForObject(sql, new Object[]{id}, new UserRowMapper());
    }

    public User find(User user) {
        String sql = "SELECT * FROM USERFILE WHERE USER_NM = ? AND CELL_PHONE = ? AND EMAIL_ADDR = ?";
        return getJdbcTemplate().queryForObject(sql,
                new Object[]{user.getName(), user.getCellPhone(), user.getEmail()},
                new UserRowMapper());
    }

    protected static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User user = new User();
            user.setId(rs.getString("USER_ID"));
            user.setName(rs.getString("USER_NM"));
            user.setEmail(rs.getString("EMAIL_ADDR"));
            user.setCellPhone(rs.getString("CELL_PHONE"));
            user.setPassword(rs.getString("USER_PW"));
            return user;
        }
    }
}
