package com.leverx.servlet.filter;

import org.slf4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.slf4j.LoggerFactory.getLogger;


public class ServletFilter implements Filter {

    private static final Logger LOGGER = getLogger(ServletFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        LOGGER.debug("Filter was created");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding(UTF_8.name());
        servletResponse.setCharacterEncoding(UTF_8.name());
        servletResponse.setContentType(APPLICATION_JSON);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        LOGGER.debug("Filter was destroyed");
    }
}
