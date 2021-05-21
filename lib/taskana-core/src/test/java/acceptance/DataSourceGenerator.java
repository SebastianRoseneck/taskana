package acceptance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The DataSourceGenerator provides the proper datasource for all Java-Integration tests.
 *
 * <p>If the file {user.home}/taskanaUnitTest.properties is present, the Datasource is created
 * according to the properties jdbcDriver, jdbcUrl, dbUserName and dbPassword. If any of these
 * properties is missing, or the file doesn't exist, the default Datasource for h2 in-memory db is
 * created.
 *
 * <p>Additionally the property schemaName can be defined. If that property is missing, or the file
 * doesn't exist the schemaName TASKANA will be used.
 */
public final class DataSourceGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceGenerator.class);
  private static final DataSource DATA_SOURCE;
  private static final String SCHEMA_NAME;

  static {
    String propertiesFileName = System.getProperty("user.home") + "/taskanaUnitTest.properties";
    File f = new File(propertiesFileName);
    if (f.exists() && !f.isDirectory()) {
      DATA_SOURCE = createDataSourceFromProperties(propertiesFileName);
      SCHEMA_NAME = getSchemaNameFromPropertiesObject(propertiesFileName);
    } else {
      DATA_SOURCE = createDataSourceForH2();
      SCHEMA_NAME = "TASKANA";
    }
  }

  private DataSourceGenerator() {}

  public static DataSource getDataSource() {
    return DATA_SOURCE;
  }

  public static String getSchemaName() {
    return SCHEMA_NAME;
  }

  private static DataSource createDataSourceFromProperties(String propertiesFileName) {
    DataSource ds;
    try (InputStream input = new FileInputStream(propertiesFileName)) {
      Properties prop = new Properties();
      prop.load(input);
      boolean propertiesFileIsComplete = true;
      String warningMessage = "";
      String jdbcDriver = prop.getProperty("jdbcDriver");
      if (jdbcDriver == null || jdbcDriver.length() == 0) {
        propertiesFileIsComplete = false;
        warningMessage += ", jdbcDriver property missing";
      }
      String jdbcUrl = prop.getProperty("jdbcUrl");
      if (jdbcUrl == null || jdbcUrl.length() == 0) {
        propertiesFileIsComplete = false;
        warningMessage += ", jdbcUrl property missing";
      }
      String dbUserName = prop.getProperty("dbUserName");
      if (dbUserName == null || dbUserName.length() == 0) {
        propertiesFileIsComplete = false;
        warningMessage += ", dbUserName property missing";
      }
      String dbPassword = prop.getProperty("dbPassword");
      if (dbPassword == null || dbPassword.length() == 0) {
        propertiesFileIsComplete = false;
        warningMessage += ", dbPassword property missing";
      }

      if (propertiesFileIsComplete) {
        ds =
            new PooledDataSource(
                Thread.currentThread().getContextClassLoader(),
                jdbcDriver,
                jdbcUrl,
                dbUserName,
                dbPassword);
        ((PooledDataSource) ds)
            .forceCloseAll(); // otherwise the MyBatis pool is not initialized correctly
      } else {
        LOGGER.warn("propertiesFile " + propertiesFileName + " is incomplete" + warningMessage);
        LOGGER.warn("Using default Datasource for Test");
        ds = createDataSourceForH2();
      }

    } catch (IOException e) {
      LOGGER.warn("createDataSourceFromProperties caught Exception " + e);
      LOGGER.warn("Using default Datasource for Test");
      ds = createDataSourceForH2();
    }

    return ds;
  }

  private static String getSchemaNameFromPropertiesObject(String propertiesFileName) {
    String schemaName = "TASKANA";
    try (InputStream input = new FileInputStream(propertiesFileName)) {
      Properties prop = new Properties();
      prop.load(input);
      boolean propertiesFileIsComplete = true;
      String warningMessage = "";
      schemaName = prop.getProperty("schemaName");
      if (schemaName == null || schemaName.length() == 0) {
        propertiesFileIsComplete = false;
        warningMessage += ", schemaName property missing";
      }

      if (!propertiesFileIsComplete) {
        LOGGER.warn("propertiesFile " + propertiesFileName + " is incomplete" + warningMessage);
        LOGGER.warn("Using default Datasource for Test");
        schemaName = "TASKANA";
      }

    } catch (FileNotFoundException e) {
      LOGGER.warn("getSchemaNameFromPropertiesObject caught Exception " + e);
      LOGGER.warn("Using default schemaName for Test");
    } catch (IOException e) {
      LOGGER.warn("createDataSourceFromProperties caught Exception " + e);
      LOGGER.warn("Using default Datasource for Test");
    }

    return schemaName;
  }

  private static DataSource createDataSourceForH2() {
    String jdbcDriver = "org.h2.Driver";
    String jdbcUrl =
        "jdbc:h2:mem:taskana;IGNORECASE=TRUE;LOCK_MODE=0;"
            + "INIT=CREATE SCHEMA IF NOT EXISTS TASKANA\\;"
            + "SET COLLATION DEFAULT_de_DE ";
    String dbUserName = "sa";
    String dbPassword = "sa";
    PooledDataSource ds =
        new PooledDataSource(
            Thread.currentThread().getContextClassLoader(),
            jdbcDriver,
            jdbcUrl,
            dbUserName,
            dbPassword);
    ds.setPoolTimeToWait(50);
    ds.forceCloseAll(); // otherwise the MyBatis pool is not initialized correctly

    return ds;
  }
}
