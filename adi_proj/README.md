# TrackNest

A desktop Java application for reporting, searching, editing and recovering lost
items on a college campus. It uses **Java Swing** for the interface and **SQLite**
for storage, so no database server is needed.

## Features

- User registration and login.
- Report a lost item with item name, place, date, time and contact number.
- View every lost item in a searchable table.
- Search by item name or place.
- Edit or delete only the items you reported.
- Mark an item as recovered.
- The SQLite database and tables are created automatically on first run.

## Technologies

| Part        | Technology                          |
|-------------|-------------------------------------|
| Language    | Java 17 or newer                    |
| Interface   | Java Swing                          |
| Database    | SQLite                              |
| JDBC driver | sqlite-jdbc 3.54.1.0 (in `lib/`)    |
| Build       | Plain `javac` / `java` (no Maven)   |

## Project Structure

```text
adi_proj/
├── lib/
│   └── sqlite-jdbc-3.54.1.0.jar   # SQLite JDBC driver
├── src/lostfound/
│   ├── Main.java                  # entry point
│   ├── Database.java              # connection + table creation
│   ├── Theme.java                 # shared colours and UI helpers
│   ├── User.java                  # user model + user database operations
│   ├── Item.java                  # item model + item database operations
│   ├── LoginFrame.java            # login screen
│   ├── RegisterFrame.java         # registration screen
│   ├── DashboardFrame.java        # list, search, edit, delete, logout
│   ├── ReportItemFrame.java       # report a lost item
│   └── EditItemFrame.java         # edit an existing item
├── lostfound.db                   # SQLite database (created automatically)
├── .gitignore
└── README.md
```

## Prerequisites

- **JDK 17 or newer**. Check with:

  ```bash
  java -version
  javac -version
  ```

- The SQLite JDBC driver is already included in `lib/`. If it is missing,
  download it from Maven Central:

  <https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.54.1.0/sqlite-jdbc-3.54.1.0.jar>

## Compile and Run

Run these commands from the project root so `lostfound.db` is created there.

**Windows (PowerShell or Command Prompt):**

```bat
javac -cp "lib\sqlite-jdbc-3.54.1.0.jar" -d out src\lostfound\*.java
java -cp "out;lib\sqlite-jdbc-3.54.1.0.jar" lostfound.Main
```

**macOS / Linux:**

```bash
javac -cp "lib/sqlite-jdbc-3.54.1.0.jar" -d out src/lostfound/*.java
java -cp "out:lib/sqlite-jdbc-3.54.1.0.jar" lostfound.Main
```

The `out/` folder is build output and is ignored by Git.

## How to Use

1. Start the program and click **Create New Account**.
2. Register with your full name, a username and a password.
3. Log in with the same username and password.
4. Use **Report Lost Item** to add a new report.
5. Search, edit, delete or mark your own items as recovered from the dashboard.

## Database

The application uses a single SQLite file, `lostfound.db`, in the project root.
It is created automatically the first time the program starts. Two tables are
used:

- `users` – `user_id`, `full_name`, `username`, `password`
- `items` – `item_id`, `item_name`, `place`, `lost_date`, `lost_time`,
  `contact`, `user_id`

Deleting `lostfound.db` resets the application to an empty database.

## Notes

- Passwords are stored as plain text because this is a learning project. Do not
  use real passwords.
- This project intentionally avoids Maven and other build tools; compiling with
  `javac` is enough.
