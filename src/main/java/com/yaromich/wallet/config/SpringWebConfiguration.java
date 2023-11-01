package com.yaromich.wallet.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan("com.yaromich.wallet")
@PropertySource(value="classpath:application.yaml", factory = YamlPropertySourceFactory.class)
@EnableWebMvc
@EnableSwagger2
public class SpringWebConfiguration implements WebMvcConfigurer {

    private final Environment environment;

    @Autowired
    public SpringWebConfiguration(Environment environment) {
        this.environment = environment;

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .indentOutput(true);
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUsername(environment.getRequiredProperty("spring.datasource.username"));
        dataSource.setUrl(environment.getRequiredProperty("spring.datasource.url"));
        dataSource.setPassword(environment.getRequiredProperty("spring.datasource.password"));
        dataSource.setSchema(environment.getRequiredProperty("spring.datasource.default-schema"));
        dataSource.setDriverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"));
        System.out.println(dataSource);
        return dataSource;
    }

    @Bean
    public SpringLiquibase springLiquibase() {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setChangeLog(environment.getRequiredProperty("spring.liquibase.change-log"));
        springLiquibase.setLiquibaseSchema(environment.getRequiredProperty("spring.liquibase.liquibase-schema"));
        springLiquibase.setDataSource(dataSource());
        return springLiquibase;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yaromich.wallet.infrastructure.in.controllers"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Wallet Service")
                .description("Documentation")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
