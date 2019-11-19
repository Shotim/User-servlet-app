package com.leverx.servlet.listener;

import org.slf4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import static org.slf4j.LoggerFactory.getLogger;

public class ServletListener implements ServletRequestListener, ServletContextListener {

    private static final Logger LOGGER = getLogger(ServletListener.class);

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        ServletRequest request = sre.getServletRequest();
        LOGGER.info("Request initialized");
        LOGGER.info("Protocol: {}", request.getProtocol());
        LOGGER.info("Context type: {}", request.getContentType());
        LOGGER.info("Remote address: {}", request.getLocalAddr());
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        LOGGER.info("Request was destroyed");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        LOGGER.info("Context was initialized");
        LOGGER.info("Attributes: {}", context.getAttributeNames());
        LOGGER.info("Server info: {}", context.getServerInfo());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Context was destroyed");
    }
}
