import java.util.List; // Unused import
import java.util.ArrayList; // Unused import

public class SampleClass2 {

    private int count; // Unused variable

    public void exampleMethod() {
        // Unused variable
        int unusedVariable = 5;

        if (true) { // This is a simple if statement
            System.out.println("This is a true statement.");
        } else if (false) { // This is an unnecessary else if
            System.out.println("This should never print.");
        }

        // Nested if statement
        if (count > 0) {
            if (count < 10) {
                System.out.println("Count is between 1 and 9.");
            } else {
                System.out.println("Count is 10 or more.");
            }
        }

        // Whitespace issues
        String   name = "Test"; // Extra spaces around assignment
        String anotherName="Sample"; // No space around assignment

        // Indentation issue
        if (true) {
            System.out.println("This line is not indented correctly."); // Incorrect indentation
        }

        // Method call with unnecessary parentheses
        int result = (5 + 5);

        // Empty block
        if (false) {
        }

        // Print statement
        System.out.println("Example method completed."); // This line is fine
    }

    public static void main(String[] args) {
        SampleClass sc = new SampleClass();
        sc.exampleMethod();
    }
}