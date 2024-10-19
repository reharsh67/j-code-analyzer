# Java Code Analyzer

The Java Code Analyzer is a tool designed to analyze Java code for various coding standards, including identifying unused variables, improper whitespace and indentation, and other code quality issues. It also offers the ability to beautify Java code based on specific user-defined options.

## Features

- **Unused Variable Detection**: Identifies and removes unused variables from Java files.
- **Whitespace and Indentation Check**: Ensures consistent indentation and proper spacing around operators and keywords.
- **Conditional Statement Analysis**: Checks the usage of conditional statements for proper formatting.
- **Empty Statement Removal**: Identifies and removes empty statements from the code.
- **Import Statement Analysis**: Handles import statements, ensuring they are organized and necessary.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Maven (for dependency management)

### Installation & Working

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/java-code-analyzer.git
2. Navigate to the project directory:
   ```bash
   cd java-code-analyzer
3. Build the project using Maven:
   ```bash
    mvn clean install
4. To run the Java Code Analyzer, you can use the command line. The following command structure is available with options:
    ```bash
    java .\target\j-code-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar path/to/File.java --verbose --all
5. Options
    ```bash
    --verbose: Enable verbose output.
    --all: Apply all analyses (unused variables, whitespace checks, etc.).##
    --conditional: Check conditional statements.
    --whiteIndent: Check whitespace and indentation.
    --var: Analyze unused variables.
    --imports: Analyze import statements.
    --emptyStmt: Remove empty statements.
6. If you prefer to provide inputs manually, you can execute the program without any arguments. The program will prompt you for inputs. In this case you cannot provide options default will be --all.
    ```bash
    java  -jar .\target\j-code-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar
    
## Contributing
Contributions are welcome! Please fork the repository and submit a pull request with your changes.

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments
JavaParser for providing tools for parsing and analyzing Java code.