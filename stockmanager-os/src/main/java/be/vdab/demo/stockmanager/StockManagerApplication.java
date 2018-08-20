package be.vdab.demo.stockmanager;

import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.metrics.export.prometheus.EnablePrometheusMetrics;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnablePrometheusMetrics
@EnableSwagger2
public class StockManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockManagerApplication.class, args);
    }

    @Value("${shift.rest.disableValidator}")
    private boolean disableValidator;

    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .paths(apiPaths())
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {

        UiConfigurationBuilder builder = UiConfigurationBuilder.builder();

        if (disableValidator) {
            builder.validatorUrl("");
        }

        return builder.build();
    }


    private Predicate<String> apiPaths() {
        return or(
                regex("/v1/stocks.*"),
                regex("/v1/*")
        );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("StockManager REST API")
                .description("A sample REST API")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
                .version("2.0")
                .build();
    }
}
