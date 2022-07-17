package uk.com.poodle.rest;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@Test
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = {"data/cleanup-data.sql", "data/load-data.sql"})
public @interface TestWithData {

}
