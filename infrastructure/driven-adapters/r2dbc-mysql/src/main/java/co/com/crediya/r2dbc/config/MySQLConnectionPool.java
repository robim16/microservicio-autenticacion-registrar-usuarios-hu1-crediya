package co.com.crediya.r2dbc.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;

import java.time.Duration;

@Configuration
public class MySQLConnectionPool {


    public static final int INITIAL_SIZE = 12;
    public static final int MAX_SIZE = 15;
    public static final int MAX_IDLE_TIME = 30;
    public static final int DEFAULT_PORT = 3306;

    @Bean
    public ConnectionPool getConnectionConfig(MySqlConnectionProperties properties) {
        MySqlConnectionConfiguration dbConfiguration = MySqlConnectionConfiguration.builder()
                .host(properties.host())
                .port(properties.port())
                .database(properties.database())
                .user(properties.username())
                .password(properties.password())
                .build();

        ConnectionFactory connectionFactory = MySqlConnectionFactory.from(dbConfiguration);

        ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration.builder(connectionFactory)
                .initialSize(INITIAL_SIZE)
                .maxSize(MAX_SIZE)
                .maxIdleTime(Duration.ofMinutes(MAX_IDLE_TIME))
                .validationQuery("SELECT 1")
                .build();

        return new ConnectionPool(poolConfiguration);
    }
}