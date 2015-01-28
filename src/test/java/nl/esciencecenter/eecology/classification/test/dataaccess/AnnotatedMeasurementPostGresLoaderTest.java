package nl.esciencecenter.eecology.classification.test.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Ignore;
import org.junit.Test;

public class AnnotatedMeasurementPostGresLoaderTest {

    @Ignore
    @Test
    public void load_nonExistentPath_exceptionThrown() {
        // Arrange
        Connection connect = null;
        Statement statement = null;
        ResultSet resultSet = null;

        String userName = "username";
        String passWord = "password";
        String databaseHost = "jdbc:postgresql://db.e-ecology.sara.nl/eecology?sslfactory=org.postgresql.ssl.NonValidatingFactory&ssl=true&user="
                + userName + "&password=" + passWord;

        // Act
        try {
            Connection connection = DriverManager.getConnection(databaseHost);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Assert

    }
}
