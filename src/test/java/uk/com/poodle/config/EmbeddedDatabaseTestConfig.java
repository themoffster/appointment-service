package uk.com.poodle.config;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.util.function.Consumer;

@TestConfiguration
public class EmbeddedDatabaseTestConfig {

    @Bean
    Consumer<EmbeddedPostgres.Builder> overrideEmbeddedPostgresWorkingDirectoryConsumer() {
        return builder -> {
            var workingDirectory = new File(System.getProperty("user.home") + "/tmp/embedded-pg");
            builder.setOverrideWorkingDirectory(workingDirectory);
        };
    }
}
