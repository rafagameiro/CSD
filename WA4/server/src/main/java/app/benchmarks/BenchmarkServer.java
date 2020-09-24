package app.benchmarks;

import app.MainApplication;
import app.WalletController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = "app", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {MainApplication.class, WalletController.class})})
public class BenchmarkServer implements CommandLineRunner {

    @Autowired
    private BenchmarkService benchmarkService;

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(BenchmarkServer.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        Thread.sleep(30000);
        benchmarkService.execute();
    }
}
