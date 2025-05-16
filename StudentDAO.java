package dao;

import model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private final String dbUrl;
    private final String username;
    private final String password;

    public StudentDAO(String dbUrl, String username, String password) {
        this.dbUrl = dbUrl;
        this.username = username;
        this.password = password;
    }

    public void addStudent(Student student) throws SQLException {
        String query = "INSERT INTO students (student_id, name, email, phone_number, date_of_birth, address) VALUES (?,?,?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, student.getStudentId());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getPhoneNumber());
            stmt.setString(5, student.getDateOfBirth());
            stmt.setString(6, student.getAddress());

            stmt.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String dateOfBirth = rs.getString("date_of_birth");
                String address = rs.getString("address");

                students.add(new Student(studentId, name, email, phoneNumber, dateOfBirth, address));
            }
        }

        return students;
    }

    public void deleteStudent(int studentId) throws SQLException {
        String query = "DELETE FROM students WHERE student_id = ?";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, studentId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No student found with ID: " + studentId);
            }
        }
    }

    public Student findStudentById(int studentId) {
        return null;
    }
}
