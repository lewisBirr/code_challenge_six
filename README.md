# Simple Trading Application
This is a simple trading application.

Being a first draft, it uses an in-memory H2 database. Neither the schema, nor the data are explicitly created.

## Required tools and libraries
1. IDE as IntelliJ
2. JDK 17
3. At least Maven 3.6.3

Install both the JDK (define the environment variable JAVA_HOME appropriately), then Maven (define the environment variable M2_HOME appropriately).

## Build the code
Execute in IDE (IntelliJ)

- New project
- Get Version Control
- paste following URL: https://github.com/lewisBirr/code_challenge_six
- clone it
- Build src/main/java/name.lattuada.trading/TradingApplication

In this way, a new jar file will be created in the target folder, e.g. target/trading-0.0.2-SNAPSHOT.jar.

## Run the code

- run  src/main/java/name.lattuada.trading/TradingApplication

## Test the Code

- run src/test/java/name.lattuada.tradin.tests/CucumberTest

## Verify database content
When the application is running, you can check the database content with a browser: H2 Console.

Here the settings to access the H2 console (values referenced in application.properties):

JDBC URL: jdbc:h2:mem:trading (see entry spring.datasource.url )
Password: password (see entry spring.datasource.password)

## Debugging
SQL statements are not logged by default. To turn them on, set the entry spring.jpa.show-sql to true in file application.properties.
H2 console is active by default. To turn it off, remove the entry spring.h2.console.enabled from application.properties.
#API
Once the application is started, you can check all the available APIs and models by using the Swagger interface.

## Improvements
implemented Methods in

src/test/java/name.lattuada.tradin.tests/TradeSteps

- oneSecurityAndTwoUsers
- createOrder
