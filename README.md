# Java Password Demo

A JavaFX desktop application for demonstrating how password handling practices evolve from insecure to robust. Students can inspect the provided SQL Server DDL scripts to see four different storage strategies and run the application to interact with the final, hardened implementation that uses PBKDF2 with per-user salt and stretching.

## Prerequisites
- Java 21 (or any compatible JDK with JavaFX modules)
- Maven 3.9+
- Microsoft SQL Server instance (local or remote)
- Ability to execute the SQL scripts in `src/main/resources/sql`

## Project Layout
- `src/main/java/se/lu/ics/App.java` – JavaFX entry point that shows the login dialog.
- `src/main/java/se/lu/ics/controllers` – UI controllers. `LoginDialogController` calls into the data layer to authenticate users.
- `src/main/java/se/lu/ics/data` – Data access classes. `UserDao` verifies passwords using PBKDF2; `ConnectionHandler` loads SQL Server credentials from a properties file.
- `src/main/resources/sql` – Four DDL scripts that illustrate successive password storage strategies (plaintext, SHA-256, salted SHA-256, PBKDF2 with salt and stretching).
- `src/main/resources/fxml/LoginDialog.fxml` – JavaFX layout for the login screen.

## Database Setup
1. Connect to your SQL Server instance.
2. Run `src/main/resources/sql/ddl_script_sha256_salt_stretch.sql` to create the `AppUser` table and seed users whose passwords are pre-hashed with PBKDF2 (100,000 iterations). The seeded usernames/passwords are:
   - `mary.sue` / `Cloudy#Morn22`
   - `gary.stu` / `Spring@202420242024`
   - `phoebe.winters` / `Cloudy#Morn22`
   - `sam.rodgers` / `Skyline!Run#77Run#66`
   - `david.brown` / `Ultra!Complex#Password123`
3. (Optional) Execute the other scripts for classroom discussion. They are not compatible with the running application without modifying `UserDao`, but they illustrate the progressive improvements:
   - `ddl_script.sql` – Plaintext passwords with a complexity constraint.
   - `ddl_script_sha256.sql` – Raw SHA-256 hashes (no salt, no stretching).
   - `ddl_script_sha256_salt.sql` – SHA-256 hashes with per-user salt, still single iteration.

## Application Configuration
Create a classpath properties file at `src/main/resources/config/config.properties` with your SQL Server settings:

```
database.server.name=localhost
database.server.port=1433
database.name=PasswordDemo
database.user.name=your_user
database.user.password=YourStrong!Passw0rd
```

Adjust the values to match your environment. The file is loaded by `ConnectionHandler` when the app starts.

## Build & Run
1. Install dependencies and compile:
   ```bash
   mvn clean compile
   ```
2. Launch the JavaFX app (from your IDE, run `se.lu.ics.App`). If you want to run from the command line, ensure the JavaFX modules are on the module path. One option is to use the JavaFX Maven plugin; another is to run your IDE configuration.
3. Enter a username/password from the seeded list to test successful logins. Use an incorrect password to trigger the failure path.

## Learning Checklist
- Compare each DDL script to see how the schema evolves when you add hashing, salting, and stretching.
- Trace how `UserDao` pulls the salt and iteration count from the database and recomputes the PBKDF2 hash during login.
- Review how the app reads database credentials from `config.properties` and consider how secrets should be handled in production.
- Experiment with the iteration count to observe how stronger stretching impacts login time.

## Troubleshooting
- **`Could not connect to the server` alert:** Verify that SQL Server is reachable and that `config.properties` contains the correct host, port, database, username, and password.
- **`Login failed` despite correct credentials:** Ensure you executed the PBKDF2 DDL script and that the seeded passwords match what you are entering. Also confirm the database collation preserves case sensitivity as expected.
- **Module path errors:** If running outside an IDE, include the JavaFX modules in the `java` command or configure the JavaFX Maven plugin (see [OpenJFX documentation](https://openjfx.io/openjfx-docs/)).
