package com.br.product_api.config.requestUtils;

import com.br.product_api.config.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestUtil {

    public static HttpServletRequest getActualRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes())
                    .getRequest();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new ValidationException("The present request cannot be processed.");
        }
    }
}
