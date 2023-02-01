package main;

import controllers.ApiController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import utils.PricesParser;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackageClasses = {ApiController.class, PricesParser.class})
public class TesoPricesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TesoPricesApplication.class, args);
	}

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1);
		executor.setMaxPoolSize(1);
		executor.setQueueCapacity(1);
		executor.setThreadNamePrefix("ESO-");
		executor.initialize();
		return executor;
	}
}
