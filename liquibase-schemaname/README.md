Liquibase Schema Name example
-----------------------------

Run  the project twice. 

At first run it creates databasechangelog in both test-schema and test_schema 

At second run it failes, because it uses liquibase.snapshot.JdbcDatabaseSnapshot. 
JdbcDatabaseSnapshot tries to find tables using:
```
return extract(databaseMetaData.getTables(catalog, schema, ((table == null) ?
SQL_FILTER_MATCH_ALL : table), new String[]{"TABLE"}));
```

The code above returns two rows and liquibase selects the first one. But the first
row is only for test-schema, not for test_schema. 

Liquibase uses java.sql.DatabaseMetadata#getTables. The method accepts schemaPattern
and uses it in SQL LIKE. Symbol "_" in LIKE substitues any other symbol. We have
test-schema and test_schema in our database, and both of them are eligible for the pattern.
