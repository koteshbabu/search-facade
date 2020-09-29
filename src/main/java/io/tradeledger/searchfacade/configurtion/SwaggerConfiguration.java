package io.tradeledger.searchfacade.configurtion;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Component
@EnableSwagger2
public class SwaggerConfiguration {


    @Bean
    public Docket configureSwagger() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.tradeledger.searchfacade"))
                .build()
                .apiInfo(getApiDetails());

    }

    private ApiInfo getApiDetails() {
        return new ApiInfo(
                "Search Facade Application",
                "Search Facade API For Searching Resources",
                "1.0",
                "",
                null,
                null,
                null,
                Collections.emptyList());
    }

}
