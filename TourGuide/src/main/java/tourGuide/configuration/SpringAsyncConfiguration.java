package tourGuide.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

/**
 * Spring configuration
 * Allow overriding and configure the executor used by @Async methods
 *
 * @author jonathan GOUVEIA
 * @version 1.0
 */
@EnableAsync
@Configuration
public class SpringAsyncConfiguration implements AsyncConfigurer {

    /**
     * getAsyncExecutor. Method that initialize ThreadPoolTaskExecutor
     *
     * @return executor
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1000);
        executor.setMaxPoolSize(1000);
        executor.initialize();
        return executor;
    }
}