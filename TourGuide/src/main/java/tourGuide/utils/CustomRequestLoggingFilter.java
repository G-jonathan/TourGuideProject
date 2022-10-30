package tourGuide.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class allows you to Monitors the requests made on the application controllers
 *
 * @author jonathan GOUVEIA
 * @version 1.0
 */
@Component
public class CustomRequestLoggingFilter extends GenericFilterBean {
    private final Logger LOGGER = LoggerFactory.getLogger(CustomRequestLoggingFilter.class);

    /**
     * When a request is made, log the request then log the response
     *
     * @param servletRequest  Contain client request information
     * @param servletResponse Contain request response
     * @param chain           Invocation chain of a filtered request
     * @throws IOException      Signals that an I/O exception
     * @throws ServletException Signals a HTML servlet exception
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest currentRequest = (HttpServletRequest) servletRequest;
        final HttpServletResponse currentResponse = (HttpServletResponse) servletResponse;
        StringBuffer requestURL = currentRequest.getRequestURL();
        LOGGER.info("[CONTROLLER] Request URL: {}", requestURL);
        try {
            chain.doFilter(currentRequest, servletResponse);
        } finally {
            int status = currentResponse.getStatus();
            LOGGER.info("[CONTROLLER] Response status: {}", status);
        }

    }
}