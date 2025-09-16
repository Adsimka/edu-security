package security.service;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Optional;

public class JdbcUserDetailsService extends MappingSqlQuery<UserDetails> implements UserDetailsService {

    private static final String SQL_SELECT_ALL_USERS = """
        SELECT u.username, up.password, array_agg(ua.authority) AS authorities FROM users u 
        LEFT JOIN user_password up ON u.id = up.user_id 
        LEFT JOIN user_authority ua ON u.id = ua.user_id 
        WHERE u.username = :username
        GROUP BY u.id, up.id
    """;

    public JdbcUserDetailsService(DataSource dataSource) {
        super(dataSource, SQL_SELECT_ALL_USERS);
        this.declareParameter(new SqlParameter("username", Types.VARCHAR));
        this.compile();
    }

    @Override
    protected UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .authorities(((String[]) rs.getArray("authorities").getArray()))
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(this.findObjectByNamedParam(Map.of("username", username)))
                .orElseThrow(() -> new UsernameNotFoundException("Username %s not found".formatted(username)));
    }
}
