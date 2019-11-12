package com.company.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class UserServletListener implements ServletRequestListener, ServletContextListener {

    private Logger logger = LoggerFactory.getLogger(UserServletListener.class);

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        ServletRequest request = sre.getServletRequest();
        logger.info("Request initialized");
        logger.info("Protocol: {}", request.getProtocol());
        logger.info("Context type: {}", request.getContentType());
        logger.info("Remote address: {}", request.getLocalAddr());
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        logger.info("Request was destroyed");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        logger.info("Context was initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Context was destroyed");
    }
}
