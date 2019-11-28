package com.leverx.servlet.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

@Slf4j
public class ServletListener implements ServletRequestListener, ServletContextListener {
    
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        ServletRequest request = sre.getServletRequest();
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
        ServletContext context = sce.getServletContext();
        log.info("Context was initialized");
        log.info("Attributes: {}", context.getAttributeNames());
        log.info("Server info: {}", context.getServerInfo());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Context was destroyed");
    }
}
