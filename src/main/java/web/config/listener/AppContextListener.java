package web.config.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("************** Starting up! **************");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("************** Shutting down! **************");
        System.out.println("Destroying Context...");
        System.out.println("Calling MySQL AbandonedConnectionCleanupThread checkedShutdown");

        // ... First close any background tasks which may be using the DB ...
        // ... Then close any DB connection pools ...

        // Now deregister JDBC drivers in this context's ClassLoader:
        // Get the webapp's ClassLoader
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        // Loop through all drivers
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == cl) {
                // This driver was registered by the webapp's ClassLoader, so deregister it:
                try {
                    System.out.println("Deregistering JDBC driver " + driver);
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException ex) {
                    System.out.println("Error deregistering JDBC driver " + driver);
                    ex.printStackTrace();
                }
            } else {
                // driver was not registered by the webapp's ClassLoader and may be in use elsewhere
                System.out.println("Not deregistering JDBC driver " + driver + " as it does not belong to this webapp's ClassLoader");
            }
        }
    }
}
