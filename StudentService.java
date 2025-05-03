package service;

import dao.StudentDAO;
import model.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentService {
    private final StudentDAO studentDAO;

    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    public void addStudent(Student student) throws SQLException {
        // Add Validations Here if Necessary
        studentDAO.addStudent(student);
    }
}
