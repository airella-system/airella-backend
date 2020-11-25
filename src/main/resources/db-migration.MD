To generate full SQL:
1. Rename liquibase-init.properties to liquibase.properties
2. Run Maven Package goal with -DskipTests
3. Run Maven liquibase:diff goal
4. SQL will be saved in liquibase-diffChangeLog.postgresql.sql

To generate migration:
1. Ensure that database is running locally
2. Ensure that database is up-to-date with latest schema
3. Run Maven Package goal
4. Remove liquibase-diffChangeLog.postgresql.sql, because otherwise content will be appended
5. Run Maven liquibase:diff goal
6. SQL with migration will be saved in liquibase-diffChangeLog.postgresql.sql
7. Rename this file to and copy to db/changelog/changes
