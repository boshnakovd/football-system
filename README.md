Task Summary: CSV Data Processing Algorithm

Overview
This project involves creating an algorithm to load, parse, and process data from CSV files into a PostgreSQL database using a Spring Boot application. The system is designed to handle football-related data such as teams, players, matches, and records. It uses a structured approach to manage asynchronous loading of data from CSV files into the database and provides robust error handling throughout the process.

Key Components
1. CSV File Processing:
The core functionality revolves around reading and processing data from four types of CSV files:

teams.csv - Contains information about football teams.
players.csv - Contains data about players and their respective teams.
matches.csv - Contains information about matches played between teams, including scores.
records.csv - Contains individual player statistics for each match.
2. REST API:
The system provides RESTful endpoints to trigger the loading of data from CSV files. The API supports the following operations:

Loading teams
Loading players
Loading matches
Loading records
These endpoints allow external systems to trigger data loading tasks asynchronously, ensuring the system remains responsive even when processing large datasets.

3. Asynchronous Loading:
The CSV loading process is implemented using asynchronous tasks to allow the system to handle large files and avoid blocking the main thread. This improves performance and scalability.

4. Error Handling:
The system has robust error handling to capture and log issues during CSV parsing or database operations. Each record is validated, and errors are logged with specific details to assist in debugging.

5. PostgreSQL Integration:
The application uses Spring Data JPA for database interaction, with PostgreSQL as the backend database. The data from CSV files is stored in normalized tables representing teams, players, matches, and records.

Algorithm Summary
File Reading: Each CSV file is read using a buffered reader. The file path is configured via an application property, ensuring flexibility in locating the files.

Data Validation: Each row of data is validated for structure and correctness (e.g., ensuring scores are valid, team/player IDs exist, and records have the correct format).

Asynchronous Processing: Data loading is handled asynchronously using Spring's CompletableFuture. This allows for non-blocking execution, which is important when loading large datasets.

Database Operations: Valid records are parsed and stored in the appropriate database tables. Data is handled using Spring Data JPA, with @ManyToOne and @OneToMany relationships properly mapped between entities like teams, players, and matches.

Logging and Error Handling: Errors such as invalid CSV format, missing fields, or data integrity issues (e.g., missing foreign keys) are caught, logged, and skipped without crashing the system.

How the Algorithm Works
Step 1: The algorithm starts by reading the CSV files using Java's BufferedReader.
Step 2: Each file type (team, player, match, record) is processed based on its structure.
Step 3: For each line in the CSV file, the system parses the fields and validates the data.
Step 4: If the data is valid, it is saved to the PostgreSQL database using JPA repositories.
Step 5: If an error occurs, the system logs the error and moves on to the next line.
This design ensures that the system remains flexible, scalable, and resilient in the face of data anomalies.

