package app;

import app.benchmarks.BenchmarkServer;
import app.benchmarks.BenchmarkService;
import app.benchmarks.ByzantineReplica;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.*;
import org.apache.catalina.Host;
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
    }

}
