package com.rihao.property.conf;

import com.rihao.property.shiro.util.JwtTokenUtil;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Ken
 * @date 2020/5/16
 * @description
 */
@Configuration
@EnableSwagger2
@Profile("dev")
public class Swagger2Config {

    @Bean
    Docket createAuthApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(
                        new ApiInfoBuilder()
                                .title("API接口")
                                .description("登录退出接口文档")
                                .termsOfServiceUrl("")
                                .contact(new Contact("ken", "", "236813207@qq.com"))
                                .version("1.0")
                                .build()
                ).groupName("登录接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.rihao.property.shiro.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(setHeaderToken());
    }

    @Bean
    Docket createAdminApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(
                        new ApiInfoBuilder()
                                .title("API接口")
                                .description("智慧军营管理端接口文档")
                                .termsOfServiceUrl("")
                                .contact(new Contact("ken", "", "236813207@qq.com"))
                                .version("1.0")
                                .build()
                ).groupName("管理端接口")
                .select()
                .apis(regexBasePackage("com.rihao.property.modules(.\\w+)+.controller"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .globalOperationParameters(setHeaderToken())
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }


    public static Predicate<RequestHandler> regexBasePackage(final String basePackage) {
        return input -> declaringClass(input).map(handlerPackage(basePackage)).orElse(true);
    }

    /**
     * 处理包路径配置规则,支持多路径扫描匹配以逗号隔开
     *
     * @param basePackage 扫描包路径
     * @return Function
     */
    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
        return input -> {
            String packageName = input.getPackage().getName();
            return packageName.matches(basePackage);
        };
    }

    /**
     * @param input RequestHandler
     * @return Optional
     */
    private static Optional<Class<?>> declaringClass(RequestHandler input) {
        return Optional.ofNullable(input.declaringClass());
    }


    private List<Parameter> setHeaderToken() {
        List<Parameter> pars = new ArrayList<>();

        // token请求头
        String testTokenValue = "";
        ParameterBuilder tokenPar = new ParameterBuilder();
        Parameter tokenParameter = tokenPar
                .name(JwtTokenUtil.getTokenName())
                .description("Token Request Header")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .defaultValue(testTokenValue)
                .build();
        pars.add(tokenParameter);
        return pars;
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> arrayList = new ArrayList();
        arrayList.add(new ApiKey("Authorization", "token", "header"));
        return arrayList;
    }

    private List<SecurityContext> securityContexts() {
        //设置需要登录认证的路径
        List<SecurityContext> result = new ArrayList<>();
        result.add(getContextByPath("/*/.*"));
        return result;
    }

    private SecurityContext getContextByPath(String pathRegex){
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(pathRegex))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        result.add(new SecurityReference("Authorization", authorizationScopes));
        return result;
    }
}
