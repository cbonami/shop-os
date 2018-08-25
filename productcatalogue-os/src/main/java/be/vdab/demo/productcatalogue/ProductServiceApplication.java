package be.vdab.demo.productcatalogue;

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

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnablePrometheusMetrics
public class ProductServiceApplication  {


    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
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
                regex("/v1/products.*"),
                regex("/v1/*")::apply
        );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ProductCatalogue REST API")
                .description("A sample REST API")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
                .version("2.0")
                .build();
    }

}