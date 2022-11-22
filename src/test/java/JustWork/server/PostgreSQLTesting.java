package JustWork.server;

import org.flywaydb.core.Flyway;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreSQLTesting {
    public static final PostgreSQLContainer<?> postgresqlContainer;

    static {
        postgresqlContainer = new PostgreSQLContainer<>("postgres:11.8")
                .withDatabaseName("test-db")
                .withUsername("it")
                .withPassword("it");
        postgresqlContainer.start();

        final var flyway = Flyway.configure()
                .locations("classpath:/db/flyway/db/migration")
                .dataSource(
                        postgresqlContainer.getJdbcUrl(),
                        postgresqlContainer.getUsername(),
                        postgresqlContainer.getPassword())
                .load();
        flyway.migrate();
    }

    public static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            final var jdbcUrl = postgresqlContainer.getJdbcUrl()
                    + "?nullNamePatternMatchesAll=true&useSSL=false"
                    + "&useUnicode=yes&characterEncoding=UTF-8";
            TestPropertyValues.of(
                    "spring.datasource.url=" + jdbcUrl,
                    "spring.datasource.username=" + postgresqlContainer.getUsername(),
                    "spring.datasource.password=" + postgresqlContainer.getPassword(),
                    "spring.datasource.driver-class-name=" + "org.postgresql.Driver",
                    "spring.jpa.hibernate.ddl-auto=validate"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
