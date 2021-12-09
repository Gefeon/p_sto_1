package com.javamentor.qa.platform.api.initializer;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;

public class FlywayMigrationStrategyImplTest implements FlywayMigrationStrategy {
    @Override
    public void migrate(Flyway flyway) {

        flyway.migrate();
    }
}
