package com.test;

public class Student {
    private String name;
    private int age;
    private int ra;
    private String course;


    public Student(String name, int age, int ra, String course) {
        // Constructor for Student class
        this.name = name;
        this.age = age;
        this.ra = ra;
        this.course = course;
    }


    


    // Getters and Setters for the Student class


    public String getName(){
        return name;
    }

    public int getAge(){
        return age;
    }

    public int getRa(){
        return ra;
    }

    public String getCourse(){
        return course;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAge(int age){
        this.age = age;
    
    }

    public void setRa(int ra){
        this.ra = ra;
    }

    public void setCourse(String course){
        this.course = course;
    }

    @Override
    public String toString() {
        // Returns a string representation of the Student object
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", ra=" + ra +
                ", course='" + course + '\'' +
                '}';
    }
    private static int count = 0;
    public static int generateRA(int year){
        count++;
        return Integer.parseInt(year + String.format("%04d", count));
    }
    

}
