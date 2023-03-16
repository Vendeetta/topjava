package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final Validator validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations != null && violations.size() > 0) {
            throw new ConstraintViolationException(violations);
        }

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password,
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {

            return null;
        }
        int userId = user.getId();
        Set<Role> roles = jdbcTemplate.query("SELECT * FROM user_role WHERE user_id = ?", rs -> {
            Set<Role> result = new HashSet<>();
            while (rs.next()) {
                Role role = Role.valueOf(rs.getString("role"));
                result.add(role);
            }
            return result;
        }, userId);
        roles = roles == null ? Collections.emptySet() : roles;
        Set<Role> newRoles = user.getRoles() == null ? Collections.emptySet() : user.getRoles();

        if (roles.size() > newRoles.size()) {
            roles.removeAll(newRoles);
            List<Role> roleToDelete = new ArrayList<>(roles);
            jdbcTemplate.batchUpdate("DELETE FROM user_role WHERE user_id = ? and role = ?",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, userId);
                            ps.setString(2, String.valueOf(roleToDelete.get(i)));
                        }

                        @Override
                        public int getBatchSize() {
                            return roleToDelete.size();
                        }
                    });
        } else if (roles.size() < newRoles.size()) {
            newRoles.removeAll(roles);
            List<Role> roleToInsert = new ArrayList<>(newRoles);
            jdbcTemplate.batchUpdate("INSERT INTO user_role VALUES (?, ?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, userId);
                    ps.setString(2, String.valueOf(roleToInsert.get(i)));
                }

                @Override
                public int getBatchSize() {
                    return roleToInsert.size();
                }
            });
        } else if (!roles.removeAll(newRoles)) {
            List<Role> roleToUpdate = new ArrayList<>(newRoles);
            jdbcTemplate.batchUpdate("UPDATE user_role SET role=? where user_id=?",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setString(1, String.valueOf(roleToUpdate.get(i)));
                            ps.setInt(2, userId);
                        }

                        @Override
                        public int getBatchSize() {
                            return roleToUpdate.size();
                        }
                    });
        }
        return user;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_role ur ON u.id = ur.user_id " +
                "WHERE u.id=?", JdbcUserRepository::extractData, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_role ur ON u.id = ur.user_id " +
                "WHERE u.email=?", JdbcUserRepository::extractData, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_role ur ON u.id = ur.user_id " +
                "ORDER BY u.name, u.email", JdbcUserRepository::extractData);
    }

    private static List<User> extractData(ResultSet rs) throws SQLException {
        Map<Integer, User> userMap = new LinkedHashMap<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            User user = userMap.get(id);
            if (user == null) {
                user = new User();
                user.setId(id);
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setRegistered(rs.getDate("registered"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
            }
            Set<Role> roles = user.getRoles();
            if (roles == null) {
                roles = new HashSet<>();
            }
            String role = rs.getString("role");
            if (role != null) {
                roles.add(Role.valueOf(role));
            }
            user.setRoles(roles);
            userMap.put(id, user);
        }
        return new ArrayList<>(userMap.values());
    }
}
