# Spring Batch - Kata FOO BAR QUIX

This application implements the FOO BAR QUIX kata as a Spring Batch process, with parallel processing capabilities and persistent job repository using PostgreSQL.

## Description

The FOO BAR QUIX kata transforms numbers (between 0 and 100) into strings according to the following rules:

- If the number is divisible by 3 or contains 3, the result contains "FOO"
- If the number is divisible by 5 or contains 5, the result contains "BAR"
- If the number contains 7, the result contains "QUIX"
- The "divisible by" rule has higher priority than the "contains" rule
- The content is analyzed from left to right
- If none of the rules apply, return the input number as a string

## Architecture

This application is built with:

- **Spring Boot**: Application framework
- **Spring Batch**: For batch processing
- **PostgreSQL**: For persisting job execution metadata
- **Docker**: For containerization and easy deployment

The application uses Spring Batch's parallel processing capabilities via a thread pool task executor to improve performance.

## Prerequisites

- Java 17+
- Docker and Docker Compose
- Maven

## Configuration

Configuration is managed through environment variables:

- `DB_HOST`: PostgreSQL host (default: postgres)
- `DB_NAME`: Database name
- `DB_USER`: Database username
- `DB_PASSWORD`: Database password
- `APP_FILE_INPUT_PATH`: Path to the input file (default: /app/input/inputFile.txt)
- `APP_FILE_OUTPUT_PATH`: Path to the output file (default: /app/output/outputFile.txt)

## Running the Application

### Using Docker Compose

1. Create a `.env` file with the required environment variables:
```
DB_HOST=postgres
DB_NAME=yourdatabase
DB_USER=youruser
DB_PASSWORD=yourpassword
```

2. Launch with Docker Compose:
```bash
docker-compose up -d
```

### Building from Source

1. Clone the repository
```bash
git https://github.com/cofiche/foo-bar.git
cd foobarkquix-batch
```

2. Build with Maven
```bash
mvn clean package
```

3. Run with Docker Compose
```bash
docker-compose up -d
```

## Input and Output

The application reads numbers from the input file located at the path specified by `APP_FILE_INPUT_PATH` and writes the transformed results to the output file at `APP_FILE_OUTPUT_PATH`.

## Fault Tolerance and Restart Capability

This batch application is designed with fault tolerance in mind:

- All job and step execution states are persisted in the PostgreSQL database
- In case of a failure, the job can be restarted and will continue from where it left off
- Step executions are tracked individually, so only failed steps need to be reprocessed

## Parallel Processing

The application leverages Spring Batch's parallel processing capabilities:

- Items are processed in parallel using a thread pool task executor
- The degree of parallelism can be configured through the application properties
- Chunk-based processing ensures efficient resource utilization

## Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── io/wahman/kata/
│   │   │       ├── config/          # Batch configuration
│   │   │       ├── processor/       # Item processors
│   │   │       ├── reader/          # Item readers
│   │   │       ├── service/         # Business logic
│   │   │       └── writer/          # Item writers
│   │   │       └── controller/      # Rest endpoints
│   │   └── resources/
│   │       ├── application.properties      # Application configuration
│   │       ├── input/                      # Input files
│   │       └── output/                     # Output files
│   └── test/                               # Unit and integration tests
├── Dockerfile                              # Docker image definition
├── docker-compose.yml                      # Docker Compose configuration
└── pom.xml                                 # Maven project definition
```


