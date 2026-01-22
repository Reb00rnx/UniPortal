package com.uniportal.Course;


import com.uniportal.User.Student;
import com.uniportal.User.Teacher;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String code;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToMany
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "course")
    private Set<Grade> grades = new HashSet<>();

    public Course(String name, String code, Teacher teacher, Set<Student> students) {
    this.name = name;
    this.code = code;
    this.teacher = teacher;
    this.students = students;
    this.grades = new HashSet<>();
}

    public void addStudent(Student student){
        if(this.students ==null){
            this.students = new HashSet<>();
        }
        this.students.add(student);
    }

    public void addTeacher(Teacher teacher){
        this.teacher = teacher;
    }


}
