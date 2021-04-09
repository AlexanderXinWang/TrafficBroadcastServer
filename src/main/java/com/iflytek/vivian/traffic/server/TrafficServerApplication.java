package com.iflytek.vivian.traffic.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EntityScan("com.iflytek.vivian.traffic")
@ComponentScan("com.iflytek.vivian.traffic")
@EnableJpaRepositories("com.iflytek.vivian.traffic.server.domain.dao")
@EnableSwagger2
public class TrafficServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrafficServerApplication.class, args);
    }

}
