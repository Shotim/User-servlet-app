package com.leverx.servlet.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import static com.leverx.config.EntityManagerFactoryConfig.closeEntityManagerFactory;
import static com.leverx.config.EntityManagerFactoryConfig.createEntityManagerFactory;

@Slf4j
public class ServletListener implements ServletRequestListener, ServletContextListener {

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        var request = sre.getServletRequest();
        log.info("Request initialized");
        log.info("Protocol: {}", request.getProtocol());
        log.info("Context type: {}", request.getContentType());
        log.info("Remote address: {}", request.getLocalAddr());
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        log.info("Request was destroyed");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Context initialized.");
        createEntityManagerFactory();
        log.info("EntityManagerFactory is created");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Context destroyed.");
        closeEntityManagerFactory();
        log.info("EntityManagerFactory is closed");
    }
}
