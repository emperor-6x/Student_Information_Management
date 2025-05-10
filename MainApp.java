package main;

import dao.StudentDAO;
import service.StudentService;
import ui.StudentGUI;

import java.sql.SQLException;

// Import all required classes
import java.util.List; // For List usage with StudentService and StudentDAO

public class MainApp {
    public static void main(String[] args) {
        // Database connection details
        String dbUrl = "jdbc:mysql://localhost:3306/studentmanager"; // Replace with your DB URL
        String username = "root"; // Replace with your DB username
        String password = "ritesh09"; // Replace with your DB password

        try {
            // Create StudentDAO object
            StudentDAO studentDAO = new StudentDAO(dbUrl, username, password);

            // Create StudentService object with StudentDAO
            StudentService studentService = new StudentService(studentDAO);

            // Launch GUI and connect with the StudentService
            StudentGUI studentGUI = new StudentGUI(studentService);
            studentGUI.setVisible(true);
        } catch (Exception e) {
            // Handle any exceptions that arise (like SQL or connection errors)
            e.printStackTrace();
        }
    }
}
