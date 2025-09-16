# Dumpy Bot

A JavaFX-based task management chatbot application that helps you organize your
todos, deadlines, and events with a friendly conversational interface.

<!--toc:start-->
- [Dumpy Bot](#dumpy-bot)
  - [Motivation](#motivation)
  - [Features](#features)
  - [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
  - [Usage](#usage)
    - [Basic Commands](#basic-commands)
    - [Date Formats](#date-formats)
  - [Project Structure](#project-structure)
  - [Development](#development)
    - [Building](#building)
    - [Testing](#testing)
    - [Code Style](#code-style)
    - [Creating Distribution](#creating-distribution)
  - [Storage](#storage)
  - [Contributing](#contributing)
  - [License](#license)
  - [Acknowledgments](#acknowledgments)
<!--toc:end-->

## Motivation

This was created as an assignment for a software engineering course in NUS (CS2103T).
In this era where LLMs are integrated into almost every application,
I named it "Dumpy" to reflect its simplicity and wishes to showcase even a
simple bot can be fun and useful.

## Features

- **Task Management**: Create, view, mark, and delete tasks
- **Multiple Task Types**:
  - **Todo**: Simple tasks without dates
  - **Deadline**: Tasks with due dates
  - **Event**: Tasks with start and end times
- **Search Functionality**: Fuzzy find tasks through the find command
- **Persistent Storage**: Tasks are automatically saved and loaded
- **GUI Interface**: Modern JavaFX-based graphical user interface

## Getting Started

### Prerequisites

- JavaFX 17.0.14.fx-zulu (included in dependencies)

### Installation

1. Clone the repository:

    ```bash
    git clone <repository-url>
    cd ip
    ```

2. Build the project:

    ```bash
    ./gradlew build
    ```

3. Run the application:

    ```bash
    ./gradlew run
    ```

Alternatively, you can run the generated JAR file:

```bash
java -jar build/libs/dumpy.jar
```

## Usage

### Basic Commands

- **Add Tasks**:

  use `todo`, `deadline`, or `event` and create tasks interactively.

- **List Tasks**: `list`

  ```sh
  list
  ```

- **Toggle task done status**: `mark <task_number>`

  ```sh
  mark 1
  ```

- **Delete Task**: `delete <task_number>`

  ```sh
  delete 2
  ```

- **Find Tasks**: `find <keyword>`

  ```sh
  find meeting
  ```

- **Exit**: `exit`

### Date Formats

The application supports this date and time formats:

- `YYYY-MM-DD HH:MM` (e.g., 2023-12-15 14:30)

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   ├── components/
│   │   │   ├── Dumpy.java           # Main application logic
│   │   │   ├── CommandRouter.java   # Command parsing and routing
│   │   │   ├── Todo.java           # Todo-specific functionality
│   │   │   └── task/               # Task-related classes
│   │   │       ├── Task.java       # Base task class
│   │   │       ├── TaskType.java   # Task type enumeration
│   │   │       ├── DeadlineTask.java
│   │   │       └── EventTask.java
│   │   ├── ui/                     # User interface components
│   │   │   ├── MainWindow.java
│   │   │   └── DialogBox.java
│   │   ├── utilities/              # Utility classes
│   │   │   ├── Data.java           # Data persistence
│   │   │   ├── DateTime.java       # Date/time utilities
│   │   │   ├── IO.java             # Input/output utilities
│   │   │   ├── EventBus.java       # Event handling
│   │   │   └── EventListener.java
│   │   ├── exceptions/
│   │   │   └── TaskNotFoundException.java
│   │   └── Main.java               # Application entry point
│   └── resources/
│       ├── view/                   # FXML files
│       │   ├── MainWindow.fxml
│       │   └── DialogBox.fxml
│       └── styles/
│           └── main.css            # Application styling
```

## Development

### Building

```bash
./gradlew build
```

### Testing

```bash
./gradlew test
```

### Code Style

The project uses Checkstyle for code quality. Run checks with:

```bash
./gradlew checkstyleMain
```

### Creating Distribution

Generate a shadow JAR with all dependencies:

```bash
./gradlew shadowJar
```

## Storage

Tasks are automatically saved to `./data/dumpy.txt` in a human-readable format.
The application will create this directory and file automatically on first run.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests and checkstyle
5. Submit a pull request

## License

This project is part of a software engineering course assignment.

## Acknowledgments

- Built with JavaFX for the graphical interface
- Uses Gradle for build management
- Follows Java coding standards with Checkstyle
