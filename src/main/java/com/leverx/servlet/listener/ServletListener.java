package com.leverx.servlet.listener;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import static com.leverx.envvar.loader.DBEnvironmentVariableLoader.getDBProperties;
import static javax.persistence.Persistence.createEntityManagerFactory;

@Slf4j
public class ServletListener implements ServletRequestListener, ServletContextListener {

    public static final String PERSISTENCE_UNIT_NAME = "Persistence";
    private static EntityManagerFactory entityManagerFactory;

    public static synchronized EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

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
        var context = sce.getServletContext();
        entityManagerFactory = createEntityManagerFactory(PERSISTENCE_UNIT_NAME, getDBProperties());
        log.info("Context was initialized");
        log.info("Attributes: {}", context.getAttributeNames());
        log.info("Server info: {}", context.getServerInfo());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        entityManagerFactory.close();
        log.info("Context was destroyed");
    }
}
