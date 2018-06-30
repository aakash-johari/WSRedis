package com.quaffle.ws;

import ch.qos.logback.access.tomcat.LogbackValve;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

@SpringBootApplication
@EnableAutoConfiguration
public class WSSpringApplication {
    public static void main(String[] args) {

        SpringApplication.run(WSSpringApplication.class, args);
    }

    @Bean(name = "TeeFilter")
    public Filter teeFilter() {
        return new ch.qos.logback.access.servlet.TeeFilter();
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        LogbackValve logbackValve = new LogbackValve();

        logbackValve.setFilename("logback.xml");
        tomcat.addContextValves(logbackValve);

        return tomcat;
    }
}
