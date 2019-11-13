package com.leverx.servlet.listener;

import org.slf4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

import static org.slf4j.LoggerFactory.getLogger;

@WebListener(value = "userServletListener")
public class ServletListener implements ServletRequestListener, ServletContextListener {

    private Logger logger = getLogger(ServletListener.class);

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
        ServletContext context = sce.getServletContext();
        logger.info("Context was initialized");
        logger.info("Attributes: {}", context.getAttributeNames());
        logger.info("Server info: {}", context.getServerInfo());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Context was destroyed");
    }
}
