# DI-Explained

Coursework and handy reference.

## Using This Project
To get started, open either the [GitBook page](https://emberwalker.gitbooks.io/dependency-injection/content/docs/1-introduction.html) or
the individual Markdown files in `docs/`. All sample projects are provided under `samples/` on [GitHub](https://github.com/Emberwalker/DI-Explained).

## Opening a Sample in an IDE
Each sample is a self-contained Gradle project, so these instructions are needed for every new sample you open.

### IntelliJ IDEA (Recommended)
1. If you have the launch window open, you can simply select Open Project. If you already have a project open, the
   option is in File -> Open...
2. Navigate to the directory, and select `build.gradle`, then click Open.
3. If asked, open `build.gradle` as _a project._
4. If the project hasn't been imported before, you'll have the Import Project dialog. Leave everything as default
   **except** for "Create seperate module per source set", which should be **unchecked**.
5. Once IDEA has indexed your project, enable the Toolbar (View -> Toolbar) and select "Run Configurations..." from the
   dropdown box in the Toolbar.
6. Click the '+' icon, select Application. Name the configuration at the top to something memorable. Tick 'Single
   Instance Only'.
7. Set the Main Class to `io.drakon.uod.di_explained.Main` and the Working Directory to the sample folder (default).
8. Optionally, create a second configuration, but use JUnit instead of Application. Tick 'Single Instance Only'. Set
   Test kind to All In Package, the Package to `io.drakon.uod.di_explained.test` and Search For Tests to 'In single
   module'.

### Eclipse (Not Recommended)
While the Gradle scripts are configured to work with Eclipse, things like run configurations may not be included. Use at
your own risk, and expect to troubleshoot.

1. In a shell (CMD, PowerShell, bash, etc) navigate to the sample directory.
2. On Windows, run `.\gradlew.bat eclipse`, on Mac/Linux run `./gradlew eclipse`
3. Open the sample directory in Eclipse.

### Netbreans (Not Supported)
If you _really_ want to use Netbeans, there is a Gradle plugin available for it, but support will _not_ be given for it.
Use at your own risk.

## Built With
* [Spark Java](http://sparkjava.com/) \(micro web framework\)
* [Kotlin](https://kotlinlang.org/) by [Jetbrains](https://www.jetbrains.com/) \(Java-compatible programming language\)
* [Guice](https://github.com/google/guice) by Google \(DI framework\)
* [Xerial SQLite-JDBC](https://github.com/xerial/sqlite-jdbc) \(Java bindings for SQLite database engine\)
* [JUnit](http://junit.org/junit4/) \(testing framework\)
* [Gradle](https://gradle.org/) \(build system\)
* [GitBook](https://www.gitbook.com/) \(manual generation\)
