package faang.school.achievement.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RetryConfigTest {
    @Autowired
    private RetryConfig retryConfig;

    @Autowired
    private RetryProperties retryProperties;

    private RetryOperationsInterceptor retryInterceptor;

    @BeforeEach
    void setUp() {
        retryInterceptor = retryConfig.retryInterceptor(retryProperties);
    }

    @Test
    void testRetryInterceptorCreation() {
        assertNotNull(retryInterceptor);
        assertEquals(3, retryProperties.getMaxAttempts());
        assertEquals(1000L, retryProperties.getInitialDelay());
        assertEquals(2, retryProperties.getMultiplier());
        assertEquals(10000L, retryProperties.getMaxDelay());
    }
}
