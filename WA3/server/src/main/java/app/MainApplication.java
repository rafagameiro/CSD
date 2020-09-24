package app;

import app.benchmarks.BenchmarkServer;
import app.benchmarks.BenchmarkService;
import app.benchmarks.ByzantineReplica;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {BenchmarkServer.class, BenchmarkService.class, ByzantineReplica.class})})
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
        System.out.println();
    }

}
