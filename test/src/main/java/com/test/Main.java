package com.test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.time.LocalDate;


public class Main {
    private static int year = LocalDate.now().getYear();
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqltest", "root", "test");
        
        // School management system
        System.out.println("Welcome to the School Management System");
        //Options
        int options; 
        do{
            System.out.println("Please select an option:");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("0. Exit");
            options = sc.nextInt();
            switch(options) {
                case 1 -> createStudent(connection);
                case 2 -> viewStudents(connection);
                case 3 -> updateStudent(connection);
                case 4 -> deleteStudent(connection);
                case 0 -> System.out.println("Exiting the system.");
                default -> System.out.println("Invalid option. Please try again.");
            }
        }while(options != 0);

    }
    //mySQL connection
    

    public static void createStudent(Connection connection) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter student name:");
        String name = sc.nextLine();
        System.out.println("Enter student age:");
        int age = sc.nextInt();
        sc.nextLine(); 
        System.out.println("Enter student's course: ");
        String course = sc.nextLine();
        int ra = Student.generateRA(year); 
        while (verifyRAExists(connection, ra)) {
            ra = Student.generateRA(year);
        }

        String sql = "INSERT INTO sqltest (ra, name, age, course) VALUES (?, ?, ?, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, ra);
            pstmt.setString(2, name);
            pstmt.setInt(3, age);
            pstmt.setString(4, course);
            pstmt.executeUpdate();
        }catch (SQLException e) {
            System.out.println("Error inserting student into database: " + e.getMessage());
        }

        Student student = new Student(name, age, ra, course);
        System.out.println("Student added: " + name + ", Age: " + age + ", Course: " + course + ", RA: " + ra);
    }
    public static boolean verifyRAExists(Connection connection ,int ra) {
        String raExists = "SELECT 1 FROM sqltest WHERE ra = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(raExists)){
            pstmt.setInt(1, ra);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); 
        } catch (SQLException e) {
            System.out.println("Error checking RA existence: " + e.getMessage());
        }
        return false;
    }
    public static void viewStudents(Connection connection){
        try (Statement stmt = connection.createStatement()){
            String studentExists = "SELECT ra, name, age, course FROM sqltest";
             // Check if there are any students in the database
            ResultSet rs = stmt.executeQuery(studentExists);
            if (!rs.isBeforeFirst()) {
                System.out.println("No students found.");
            }else{
                System.out.println("///////////////////////////////////////////////////List of Students://///////////////////////////////////////////");
                while (rs.next()) {
                    System.out.println("RA: " + rs.getInt("ra") +
                                       ", Name: " + rs.getString("name") +
                                       ", Age: " + rs.getInt("age") +
                                       ", Course: " + rs.getString("course"));
                }
                System.out.println("////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving students: " + e.getMessage());
        }

    }
    public static void updateStudent(Connection connection){
        Scanner sc = new Scanner (System.in);
        System.out.println("Enter the RA of the student you want to update:");
        int ra = sc.nextInt();
        sc.nextLine(); // Consume newline

        if (!verifyRAExists(connection, ra)) {
            System.out.println("Student with RA " + ra + " does not exist.");
            return;
        }
        System.out.println("Enter new name for the student:");
        String name = sc.nextLine();
        System.out.println("Enter new age for the student:");
        int age = sc.nextInt();
        sc.nextLine(); // Consume newline
        System.out.println("Enter new course for the student:");
        String course = sc.nextLine();

        String updateStudent = "UPDATE sqltest SET name = ?, age = ?, course = ? WHERE ra = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateStudent)){
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, course);
            pstmt.setInt(4, ra);
            pstmt.executeUpdate();

            System.out.println("Student updated successfully.");
        }catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }
    public static void deleteStudent(Connection connection) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the RA of the student you want to delete:");
        int ra = sc.nextInt();
        if (!verifyRAExists(connection, ra)) {
            System.out.println("Student with RA " + ra + " does not exist.");
            return;
        }

        String deleteStudent = "DELETE FROM sqltest WHERE ra = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(deleteStudent)){
            pstmt.setInt(1, ra);
            pstmt.executeUpdate();
            System.out.println("Student deleted successfully.");
        }catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }
}