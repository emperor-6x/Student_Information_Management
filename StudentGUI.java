package ui;

import model.Student;
import service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class StudentGUI extends Frame {
    private final StudentService studentService;

    public StudentGUI(StudentService studentService) {
        this.studentService = studentService;

        // Set Frame properties
        setTitle("Student Management System");
        setSize(700, 550);
        setLayout(null);

        // Label and Text Field for Student ID
        Label idLabel = new Label("Student ID:");
        idLabel.setBounds(50, 50, 100, 25);
        add(idLabel);

        TextField idField = new TextField();
        idField.setBounds(160, 50, 200, 25);
        add(idField);

        // Label and Text Field for Name
        Label nameLabel = new Label("Name:");
        nameLabel.setBounds(50, 90, 100, 25);
        add(nameLabel);

        TextField nameField = new TextField();
        nameField.setBounds(160, 90, 200, 25);
        add(nameField);

        // Label and Text Field for Email
        Label emailLabel = new Label("Email:");
        emailLabel.setBounds(50, 130, 100, 25);
        add(emailLabel);

        TextField emailField = new TextField();
        emailField.setBounds(160, 130, 200, 25);
        add(emailField);

        // Label and Text Field for Phone Number
        Label phoneLabel = new Label("Phone Number:");
        phoneLabel.setBounds(50, 170, 100, 25);
        add(phoneLabel);

        TextField phoneField = new TextField();
        phoneField.setBounds(160, 170, 200, 25);
        add(phoneField);

        // Label and Text Field for Date of Birth
        Label dobLabel = new Label("Date of Birth:");
        dobLabel.setBounds(50, 210, 100, 25);
        add(dobLabel);

        TextField dobField = new TextField();
        dobField.setBounds(160, 210, 200, 25);
        add(dobField);

        // Label and Text Field for Address
        Label addressLabel = new Label("Address:");
        addressLabel.setBounds(50, 250, 100, 25);
        add(addressLabel);

        TextField addressField = new TextField();
        addressField.setBounds(160, 250, 200, 25);
        add(addressField);

        // Add Student Button
        Button addButton = new Button("Add Student");
        addButton.setBounds(50, 300, 120, 30);
        add(addButton);
        addButton.addActionListener(e -> {
            try {
                int studentId = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String email = emailField.getText();
                String phoneNumber = phoneField.getText();
                String dateOfBirth = dobField.getText();
                String address = addressField.getText();

                Student student = new Student(studentId, name, email, phoneNumber, dateOfBirth, address);
                studentService.addStudent(student);
                showMessage("Student added successfully!");
            } catch (NumberFormatException nfe) {
                showMessage("Please enter a valid Student ID!");
            } catch (SQLException sqlException) {
                showMessage("Error adding student: " + sqlException.getMessage());
            }
        });

        // Delete Student Button
        Button deleteButton = new Button("Delete Student");
        deleteButton.setBounds(190, 300, 120, 30);
        add(deleteButton);
        deleteButton.addActionListener(e -> {
            try {
                String input = showInputDialog("Enter Student ID to delete:");
                if (input != null) {
                    int studentId = Integer.parseInt(input);
                    studentService.deleteStudent(studentId);
                    showMessage("Student deleted successfully!");
                }
            } catch (NumberFormatException nfe) {
                showMessage("Please enter a valid Student ID!");
            } catch (SQLException sqlException) {
                showMessage("Error deleting student: " + sqlException.getMessage());
            }
        });

        // Show All Students Button
        Button showButton = new Button("Show All Students");
        showButton.setBounds(330, 300, 150, 30);
        add(showButton);
        showButton.addActionListener(e -> {
            try {
                // New JFrame to display the table of students
                JFrame tableFrame = new JFrame("All Students");
                tableFrame.setSize(900, 500);
                tableFrame.setLayout(new BorderLayout());

                // Add JTextField for searching by name
                JTextField searchField = new JTextField("Search by Name...");
                searchField.setHorizontalAlignment(JTextField.LEFT);
                tableFrame.add(searchField, BorderLayout.NORTH);

                // Load all students from the service and display in JTable
                String[] columnHeaders = {"Student ID", "Name", "Email", "Phone", "DOB", "Address"};
                List<Student> students = studentService.getAllStudents();
                Object[][] data = prepareStudentTableData(students);

                DefaultTableModel tableModel = new DefaultTableModel(data, columnHeaders);
                JTable table = new JTable(tableModel);
                table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                // Add JTable to JScrollPane for scrollable view
                JScrollPane scrollPane = new JScrollPane(table);
                tableFrame.add(scrollPane, BorderLayout.CENTER);

                // Add KeyListener to searchField for search functionality
                searchField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        String query = searchField.getText().toLowerCase();
                        List<Student> filteredStudents = students.stream()
                                .filter(student -> student.getName().toLowerCase().contains(query))
                                .collect(Collectors.toList());
                        updateJTable(tableModel, filteredStudents);
                    }
                });

                // Add "Update Selected Student" button
                JButton updateButton = new JButton("Update Selected Student");
                updateButton.addActionListener(e1 -> {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1) {
                        showMessage("Please select a student to update.");
                        return;
                    }

                    try {
                        int studentId = (int) tableModel.getValueAt(selectedRow, 0); // Get Student ID
                        Student student = studentService.getStudentById(studentId);
                        if (student != null) {
                            String newName = showInputDialog("Enter New Name:");
                            String newEmail = showInputDialog("Enter New Email:");
                            String newPhone = showInputDialog("Enter New Phone Number:");
                            String newDob = showInputDialog("Enter New Date of Birth:");
                            String newAddress = showInputDialog("Enter New Address:");

                            student.setName(newName != null ? newName : student.getName());
                            student.setEmail(newEmail != null ? newEmail : student.getEmail());
                            student.setPhoneNumber(newPhone != null ? newPhone : student.getPhoneNumber());
                            student.setDateOfBirth(newDob != null ? newDob : student.getDateOfBirth());
                            student.setAddress(newAddress != null ? newAddress : student.getAddress());

                            studentService.updateStudent(student);
                            showMessage("Student updated successfully!");
                            // Refresh table with latest data
                            updateJTable(tableModel, studentService.getAllStudents());
                        }
                    } catch (SQLException ex) {
                        showMessage("Error occurred while updating student: " + ex.getMessage());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
                tableFrame.add(updateButton, BorderLayout.SOUTH);

                tableFrame.setVisible(true);

            } catch (SQLException sqlException) {
                showMessage("Error retrieving students: " + sqlException.getMessage());
            }
        });

        // Close button behavior
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // Set Frame visibility
        setVisible(true);
    }

    /**
     * Prepares student data in 2D array format for JTable.
     */
    private Object[][] prepareStudentTableData(List<Student> students) {
        Object[][] data = new Object[students.size()][6];
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            data[i][0] = student.getStudentId();
            data[i][1] = student.getName();
            data[i][2] = student.getEmail();
            data[i][3] = student.getPhoneNumber();
            data[i][4] = student.getDateOfBirth();
            data[i][5] = student.getAddress();
        }
        return data;
    }

    /**
     * Updates the JTable with the given list of students.
     */
    private void updateJTable(DefaultTableModel tableModel, List<Student> students) {
        tableModel.setRowCount(0); // Clear existing rows
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                    student.getStudentId(),
                    student.getName(),
                    student.getEmail(),
                    student.getPhoneNumber(),
                    student.getDateOfBirth(),
                    student.getAddress()
            });
        }
    }

    /**
     * Displays a simple message dialog.
     */
    private void showMessage(String message) {
        Dialog dialog = new Dialog(this, "Message", true);
        dialog.setSize(300, 150);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(message));
        Button okButton = new Button("OK");
        okButton.addActionListener(e -> dialog.dispose());
        dialog.add(okButton);
        dialog.setVisible(true);
    }

    /**
     * Displays a custom input dialog and returns the input value.
     */
    private String showInputDialog(String message) {
        Dialog dialog = new Dialog(this, "Input", true);
        dialog.setSize(300, 150);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(message));
        TextField inputField = new TextField(20);
        dialog.add(inputField);
        Button okButton = new Button("OK");
        final String[] input = {null};
        okButton.addActionListener(e -> {
            input[0] = inputField.getText();
            dialog.dispose();
        });
        dialog.add(okButton);
        dialog.setVisible(true);
        return input[0];
    }
}
