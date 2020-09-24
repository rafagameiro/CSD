package app;

import app.interceptors.BasicAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableRedisRepositories
@Configuration
public class AppConfig implements WebMvcConfigurer {


    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    /*@Value("${spring.redis.cluster.nodes}")
    private List<String> nodes;

    @Value("${spring.redis.cluster.max-redirects}")
    private int maxRedirects;*/

/*
    @Value("${spring.redis.sentinel.master}")
    private String master;

    @Value("${spring.redis.sentinel.nodes}")
    private List<String> nodes;*/

    private BasicAuthenticationInterceptor interceptor;

    @Autowired
    public AppConfig(BasicAuthenticationInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        /*RedisClusterConfiguration config = new RedisClusterConfiguration(nodes);
        config.setMaxRedirects(maxRedirects);
        return new LettuceConnectionFactory(config);*/
        return new LettuceConnectionFactory(host,port);

        /*RedisSentinelConfiguration config = new RedisSentinelConfiguration().master(master);
        nodes.forEach((s) -> {
            String[] hostPort = s.split(":");
            config.sentinel(hostPort[0], Integer.parseInt(hostPort[1]));
        });

        return new LettuceConnectionFactory(config);*/
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

}
