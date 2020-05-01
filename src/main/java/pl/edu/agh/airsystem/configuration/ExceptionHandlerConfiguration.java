package pl.edu.agh.airsystem.configuration;


import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequest;
import pl.edu.agh.airsystem.model.api.response.ErrorResponse;

import java.util.Map;

@Configuration
public class ExceptionHandlerConfiguration {

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {

            @Override
            public Map<String, Object> getErrorAttributes(
                    WebRequest webRequest, boolean includeStackTrace) {

                Map<String, Object> defaultMap = super.getErrorAttributes(webRequest, includeStackTrace);

                System.out.println();

                ErrorResponse errorResponse = new ErrorResponse((int) defaultMap.get("status"),
                        (String)defaultMap.get("error"), (String)defaultMap.get("message"));

                return errorResponse.toMap();
            }

        };
    }
}
