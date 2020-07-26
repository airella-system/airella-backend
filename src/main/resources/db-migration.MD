To generate full SQL:
1. Rename liquibase-init to liquibase
2. Run Maven Package goal
3. Run Maven liquibase:diff goal
4. SQL will be saved in liquibase-diffChangeLog.postgresql.sql

To generate migration:
1. Ensure that database is running locally
2. Ensure that database is up-to-date with latest schema
3. Run Maven Package goal
4. Run Maven liquibase:diff goal
5. SQL with migration will be saved in liquibase-diffChangeLog.postgresql.sql
6. Rename this file to and copy to db/changelog/changes