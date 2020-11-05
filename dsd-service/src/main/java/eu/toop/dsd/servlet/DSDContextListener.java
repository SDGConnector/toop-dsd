package eu.toop.dsd.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DSDContextListener implements ServletContextListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(DSDContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOGGER.info("DSD Starting UP");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LOGGER.info("DSD Shutdown");
    }
}