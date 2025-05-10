package ui;

import model.Student;
import service.StudentService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class StudentGUI extends JFrame {
    private final StudentService studentService;

    public StudentGUI(StudentService studentService) {
        this.studentService = studentService;

        // Set frame properties
        setTitle("Student Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));

        // Components
        JLabel lblName = new JLabel("Name: ");
        JTextField txtName = new JTextField();
        JLabel lblEmail = new JLabel("Email: ");
        JTextField txtEmail = new JTextField();
        JLabel lblPhoneNumber = new JLabel("Phone Number: ");
        JTextField txtPhoneNumber = new JTextField();
        JLabel lblDateOfBirth = new JLabel("Date of Birth (YYYY-MM-DD): ");
        JTextField txtDateOfBirth = new JTextField();
        JLabel lblAddress = new JLabel("Address: ");
        JTextField txtAddress = new JTextField();

        JButton btnAdd = new JButton("Add Student");
        JButton btnViewAll = new JButton("View All Students");

        // Add components to panel
        panel.add(lblName);
        panel.add(txtName);
        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblPhoneNumber);
        panel.add(txtPhoneNumber);
        panel.add(lblDateOfBirth);
        panel.add(txtDateOfBirth);
        panel.add(lblAddress);
        panel.add(txtAddress);

        // Buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(btnAdd);
        buttonsPanel.add(btnViewAll);

        add(panel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        // Button Actions
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = txtName.getText();
                    String email = txtEmail.getText();
                    String phoneNumber = txtPhoneNumber.getText();
                    String dateOfBirth = txtDateOfBirth.getText();
                    String address = txtAddress.getText();

                    Student student = new Student(0, name, email, phoneNumber, dateOfBirth, address);
                    studentService.addStudent(student);

                    JOptionPane.showMessageDialog(null, "Student added successfully!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });

        btnViewAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    java.util.List<Student> students = studentService.getAllStudents();

                    // Display all students in a new window
                    JFrame viewFrame = new JFrame("All Students");
                    viewFrame.setSize(500, 300);
                    viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    String[] columnNames = {"ID", "Name", "Email", "Phone", "DOB", "Address"};
                    String[][] data = new String[students.size()][6];

                    for (int i = 0; i < students.size(); i++) {
                        Student s = students.get(i);
                        data[i][0] = String.valueOf(s.getStudentId());
                        data[i][1] = s.getName();
                        data[i][2] = s.getEmail();
                        data[i][3] = s.getPhoneNumber();
                        data[i][4] = s.getDateOfBirth();
                        data[i][5] = s.getAddress();
                    }

                    JTable table = new JTable(data, columnNames);
                    JScrollPane scrollPane = new JScrollPane(table);

                    viewFrame.add(scrollPane, BorderLayout.CENTER);
                    viewFrame.setVisible(true);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });
    }
}
