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

    public void addStudent(Student student) throws SQLException {
        studentDAO.addStudent(student);
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    public void deleteStudent(int studentId) throws SQLException {
        studentDAO.deleteStudent(studentId);
    }

    public Student getStudentById(int studentId) throws Exception {
        // Delegate the retrieval logic to the DAO layer
        Student student = studentDAO.findStudentById(studentId);
        if (student == null) {
            throw new Exception("Student with ID " + studentId + " not found.");
        }
        return student;
    }

    public void updateStudent(Student student) {
    }
}
