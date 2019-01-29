package io.github.itsjohno.codetest.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "io.github.itsjohno.controller")
public class WebConfig {
}
