package org.opensource.energy.vpp_backend.aspect;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for retrying methods that perform database operations prone to transient failures.
 * <p>
 * Automatically retries the annotated method up to 3 times (default)
 * with a backoff delay of 2000 milliseconds between attempts.
 * <p>
 * Retryable exceptions include common transient issues such as:
 * <ul>
 *     <li>{@link org.springframework.dao.ConcurrencyFailureException}</li>
 *     <li>{@link org.springframework.dao.CannotAcquireLockException}</li>
 *     <li>{@link org.springframework.dao.TransientDataAccessResourceException}</li>
 *     <li>{@link org.springframework.dao.RecoverableDataAccessException}</li>
 *     <li>{@link org.hibernate.exception.JDBCConnectionException}</li>
 *     <li>{@link java.sql.SQLTransientException}</li>
 *     <li>{@link java.sql.SQLTimeoutException}</li>
 * </ul>
 *
 * <strong>Important:</strong> This annotation relies on Spring AOP proxies.
 * Therefore, the annotated method must be called from <em>outside its own class</em>
 * (i.e., no self-invocation) for the retry mechanism to work.
 *
 * <p>Example of correct usage:
 * <pre>
 * {@code
 * @Service
 * public class SomeService {
 *     @RetryOnDatabaseFailure
 *     public void updateSomething() {
 *         // database update logic
 *     }
 * }
 *
 * @Component
 * public class SomeCaller {
 *     @Autowired
 *     private SomeService someService;
 *
 *     public void doWork() {
 *         someService.updateSomething(); // ✅ proxy-based call
 *     }
 * }
 * }
 * </pre>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Retryable(
        retryFor = {
                org.springframework.dao.ConcurrencyFailureException.class,
                org.springframework.dao.CannotAcquireLockException.class,
                org.springframework.dao.TransientDataAccessResourceException.class,
                org.springframework.dao.RecoverableDataAccessException.class,
                org.hibernate.exception.JDBCConnectionException.class,
                java.sql.SQLTransientException.class,
                java.sql.SQLTimeoutException.class
        },
        backoff = @Backoff(delay = 2000)
)
public @interface RetryOnDatabaseFailure {
}