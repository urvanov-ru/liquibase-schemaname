package ru.urvanov.javaexamples.liquibaseschemaname;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

public class Main {

    public static void main(String[] args)
            throws Exception {
        
        // When I call liquibaseUpdate with test-schema it uses test-schema.
        // But when I call liquibaseUpdate with test_schema then we have to 
        // options:
        // If test-schema exists, then it uses it.
        // otherwise it uses test_schema as expected.
        
        liquibaseUpdate("test-schema");
        liquibaseUpdate("test_schema");
    }

    public static void liquibaseUpdate(String schemaName)
            throws Exception {
        String url = "jdbc:postgresql://localhost:5432/testdb";
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "root");
        try (Connection connection = DriverManager.getConnection(url, props)) {

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(
                            new JdbcConnection(connection));

            database.setLiquibaseSchemaName(schemaName);
            database.setDefaultSchemaName(schemaName);

            try (Liquibase liquibase = new liquibase.Liquibase(
                    "dbchangelog.xml", new ClassLoaderResourceAccessor(),
                    database)) {

                liquibase.update(new Contexts(), new LabelExpression());
            }
        }
    }
}
