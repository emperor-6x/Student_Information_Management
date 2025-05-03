package dao;

import model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private Connection connection;

    // Establish connection to the MySQL Database
    public StudentDAO(String dbUrl, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(dbUrl, username, password);
    }

    // Add a New Student
    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO STUDENTS (STUDENT_ID, NAME, EMAIL, PHONE_NUMBER, DATE_OF_BIRTH, ADDRESS) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, student.getStudentId());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getPhoneNumber());
            ps.setDate(5, Date.valueOf(student.getDateOfBirth()));
            ps.setString(6, student.getAddress());
            ps.executeUpdate(); 
        }
    }

    // Retrieve All Students
    public List<Student> getAllStudents() throws SQLException {
        String sql = "SELECT * FROM STUDENTS";
        List<Student> students = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("STUDENT_ID"),
                        rs.getString("NAME"),
                        rs.getString("EMAIL"),
                        rs.getString("PHONE_NUMBER"),
                        rs.getDate("DATE_OF_BIRTH").toString(),
                        rs.getString("ADDRESS")
                ));
            }
        }
        return students;
    }
}
