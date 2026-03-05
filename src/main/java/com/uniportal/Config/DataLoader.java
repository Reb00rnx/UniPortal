package com.uniportal.Config;

import com.uniportal.Course.ConsultationData;
import com.uniportal.Course.Course;
import com.uniportal.Course.CourseModule;
import com.uniportal.Course.Grade;
import com.uniportal.Enums.*;
import com.uniportal.Repository.*;
import com.uniportal.User.Student;
import com.uniportal.User.Teacher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final GradeRepository gradeRepository;
    private final CourseModuleRepository courseModuleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConsultationRepository consultationRepository;

    @Override
    public void run(String... args) throws Exception {

        if (teacherRepository.count() > 0 || studentRepository.count() > 0) {
            log.info("ℹ️ Database already initialized. Skipping data loading...");
            return;
        }

        log.info("🚀 Starting data initialization...");

        // ==================== TEACHERS ====================
        log.info("📚 Creating teachers...");

        Teacher teacher1 = Teacher.builder()
                .firstName("John")
                .lastName("Smith")
                .email("john.smith@uni.edu")
                .password(passwordEncoder.encode("Teacher123!"))
                .academicTitle(AcademicTitle.dr)
                .department(DepartmentName.COMPUTER_SCIENCE)
                .role(Role.TEACHER)
                .build();

        Teacher teacher2 = Teacher.builder()
                .firstName("Mary")
                .lastName("Johnson")
                .email("mary.johnson@uni.edu")
                .password(passwordEncoder.encode("Teacher123!"))
                .academicTitle(AcademicTitle.prof)
                .department(DepartmentName.MATHEMATICS)
                .role(Role.TEACHER)
                .build();

        Teacher teacher3 = Teacher.builder()
                .firstName("Daniel")
                .lastName("Carter")
                .email("daniel.carter@uni.edu")
                .password(passwordEncoder.encode("Teacher123!"))
                .academicTitle(AcademicTitle.dr)
                .department(DepartmentName.COMPUTER_SCIENCE)
                .role(Role.TEACHER)
                .build();

        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        teacherRepository.save(teacher3);

        // ==================== STUDENTS ====================
        log.info("🎓 Creating students...");

        Student student1 = Student.builder()
                .firstName("Alice")
                .lastName("Brown")
                .email("alice.brown@student.uni.edu")
                .password(passwordEncoder.encode("Student123!"))
                .indexNumber("12345")
                .role(Role.STUDENT)
                .build();

        Student student2 = Student.builder()
                .firstName("Robert")
                .lastName("Wilson")
                .email("robert.wilson@student.uni.edu")
                .password(passwordEncoder.encode("Student123!"))
                .indexNumber("12346")
                .role(Role.STUDENT)
                .build();

        Student student3 = Student.builder()
                .firstName("Kate")
                .lastName("Miller")
                .email("kate.miller@student.uni.edu")
                .password(passwordEncoder.encode("Student123!"))
                .indexNumber("12347")
                .role(Role.STUDENT)
                .build();

        Student student4 = Student.builder()
                .firstName("James")
                .lastName("Taylor")
                .email("james.taylor@student.uni.edu")
                .password(passwordEncoder.encode("Student123!"))
                .indexNumber("12348")
                .role(Role.STUDENT)
                .build();

        Student student5 = Student.builder()
                .firstName("Sophia")
                .lastName("Davis")
                .email("sophia.davis@student.uni.edu")
                .password(passwordEncoder.encode("Student123!"))
                .indexNumber("12349")
                .role(Role.STUDENT)
                .build();

        Student student6 = Student.builder()
                .firstName("Lucas")
                .lastName("Anderson")
                .email("lucas.anderson@student.uni.edu")
                .password(passwordEncoder.encode("Student123!"))
                .indexNumber("12350")
                .role(Role.STUDENT)
                .build();

        Student student7 = Student.builder()
                .firstName("Emma")
                .lastName("Thomas")
                .email("emma.thomas@student.uni.edu")
                .password(passwordEncoder.encode("Student123!"))
                .indexNumber("12351")
                .role(Role.STUDENT)
                .build();

        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        studentRepository.save(student4);
        studentRepository.save(student5);
        studentRepository.save(student6);
        studentRepository.save(student7);

        // ==================== COURSES ====================
        log.info("📖 Creating courses...");

        Set<Student> allStudents = new HashSet<>();
        allStudents.add(student1);
        allStudents.add(student2);
        allStudents.add(student3);
        allStudents.add(student4);
        allStudents.add(student5);
        allStudents.add(student6);
        allStudents.add(student7);

        Set<Student> groupA = new HashSet<>();
        groupA.add(student1);
        groupA.add(student2);
        groupA.add(student3);
        groupA.add(student4);

        Set<Student> groupB = new HashSet<>();
        groupB.add(student3);
        groupB.add(student5);
        groupB.add(student6);
        groupB.add(student7);

        Course course1 = new Course();
        course1.setName("Algorithms and Data Structures");
        course1.setCode("ADS-101");
        course1.setTeacher(teacher1);
        course1.setStudents(allStudents);

        Course course2 = new Course();
        course2.setName("Object-Oriented Programming");
        course2.setCode("OOP-102");
        course2.setTeacher(teacher2);
        course2.setStudents(allStudents);

        Course course3 = new Course();
        course3.setName("Database Systems");
        course3.setCode("DBS-201");
        course3.setTeacher(teacher1);
        course3.setStudents(groupA);

        Course course4 = new Course();
        course4.setName("Web Development Fundamentals");
        course4.setCode("WDF-202");
        course4.setTeacher(teacher3);
        course4.setStudents(groupB);

        Course course5 = new Course();
        course5.setName("Linear Algebra");
        course5.setCode("LA-301");
        course5.setTeacher(teacher2);
        course5.setStudents(groupA);

        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);
        courseRepository.save(course4);
        courseRepository.save(course5);

        // ==================== COURSE MODULES ====================
        log.info("📦 Adding modules to courses...");

        courseModuleRepository.save(new CourseModule(0L, "Introduction & Complexity", "Big O notation, time and space complexity overview.", 1, course1));
        courseModuleRepository.save(new CourseModule(0L, "Sorting Algorithms", "QuickSort, MergeSort, and stability in sorting.", 2, course1));
        courseModuleRepository.save(new CourseModule(0L, "Linear Data Structures", "Deep dive into Linked Lists, Stacks, and Queues.", 3, course1));
        courseModuleRepository.save(new CourseModule(0L, "Trees and Graphs", "Binary trees, BST, BFS and DFS traversal.", 4, course1));

        courseModuleRepository.save(new CourseModule(0L, "Classes and Objects", "Basics of encapsulation, constructors, and modifiers.", 1, course2));
        courseModuleRepository.save(new CourseModule(0L, "Inheritance & Polymorphism", "Extending classes and method overriding techniques.", 2, course2));
        courseModuleRepository.save(new CourseModule(0L, "Interfaces & Abstraction", "Difference between abstract classes and interfaces.", 3, course2));
        courseModuleRepository.save(new CourseModule(0L, "Design Patterns", "Introduction to Singleton, Factory, and Observer patterns.", 4, course2));

        courseModuleRepository.save(new CourseModule(0L, "Relational Model", "Tables, keys, and relationships in relational databases.", 1, course3));
        courseModuleRepository.save(new CourseModule(0L, "SQL Basics", "SELECT, INSERT, UPDATE, DELETE — core SQL operations.", 2, course3));
        courseModuleRepository.save(new CourseModule(0L, "Normalization", "1NF, 2NF, 3NF and why they matter.", 3, course3));

        courseModuleRepository.save(new CourseModule(0L, "HTML & CSS Foundations", "Semantic HTML, box model, Flexbox and Grid.", 1, course4));
        courseModuleRepository.save(new CourseModule(0L, "JavaScript Essentials", "Variables, functions, DOM manipulation and events.", 2, course4));
        courseModuleRepository.save(new CourseModule(0L, "REST APIs & Fetch", "Consuming REST APIs with Fetch and handling async code.", 3, course4));

        courseModuleRepository.save(new CourseModule(0L, "Vectors and Matrices", "Vector spaces, matrix operations and determinants.", 1, course5));
        courseModuleRepository.save(new CourseModule(0L, "Linear Transformations", "Mapping between vector spaces, kernel and image.", 2, course5));
        courseModuleRepository.save(new CourseModule(0L, "Eigenvalues & Eigenvectors", "Characteristic polynomial and diagonalization.", 3, course5));

        // ==================== CONSULTATIONS ====================
        log.info("📅 Creating consultations...");

        ConsultationData cons1 = new ConsultationData();
        cons1.setCon_day(Day.MONDAY);
        cons1.setStartTime("10:00");
        cons1.setEndTime("11:30");
        cons1.setRoom("Room 101");
        cons1.setTeacher(teacher1);

        ConsultationData cons2 = new ConsultationData();
        cons2.setCon_day(Day.WEDNESDAY);
        cons2.setStartTime("14:00");
        cons2.setEndTime("15:30");
        cons2.setRoom("Virtual Room A");
        cons2.setTeacher(teacher2);

        ConsultationData cons3 = new ConsultationData();
        cons3.setCon_day(Day.THURSDAY);
        cons3.setStartTime("09:00");
        cons3.setEndTime("10:30");
        cons3.setRoom("Room 204");
        cons3.setTeacher(teacher3);

        consultationRepository.save(cons1);
        consultationRepository.save(cons2);
        consultationRepository.save(cons3);

        // ==================== GRADES ====================
        log.info("📝 Adding grades...");

        gradeRepository.save(new Grade(GradeValue.FIVE,       student1, course1, "Excellent work on Task 1"));
        gradeRepository.save(new Grade(GradeValue.FOUR_HALF,  student1, course1, "Midterm Exam result"));
        gradeRepository.save(new Grade(GradeValue.THREE_HALF, student1, course1, "Quiz about complexity"));

        gradeRepository.save(new Grade(GradeValue.FOUR_HALF,  student2, course1, "Very good project presentation"));
        gradeRepository.save(new Grade(GradeValue.FOUR,       student2, course1, "Midterm Exam"));
        gradeRepository.save(new Grade(GradeValue.FIVE,       student2, course1, "Extra task — graph traversal"));

        gradeRepository.save(new Grade(GradeValue.FOUR,       student3, course1, "Steady progress throughout the module"));
        gradeRepository.save(new Grade(GradeValue.THREE,      student3, course1, "Sorting algorithms quiz"));

        gradeRepository.save(new Grade(GradeValue.THREE_HALF, student4, course1, "Midterm — decent understanding"));
        gradeRepository.save(new Grade(GradeValue.FOUR,       student4, course1, "Homework: Linked List implementation"));
        gradeRepository.save(new Grade(GradeValue.FOUR_HALF,  student4, course1, "Final project"));

        gradeRepository.save(new Grade(GradeValue.FIVE,       student5, course1, "Outstanding performance"));
        gradeRepository.save(new Grade(GradeValue.FIVE,       student5, course1, "Midterm Exam — perfect score"));

        gradeRepository.save(new Grade(GradeValue.THREE,      student6, course1, "Needs improvement on recursion"));
        gradeRepository.save(new Grade(GradeValue.THREE_HALF, student6, course1, "Retake quiz — better result"));

        gradeRepository.save(new Grade(GradeValue.FOUR,       student7, course1, "Good understanding of trees"));
        gradeRepository.save(new Grade(GradeValue.FOUR_HALF,  student7, course1, "Final project submission"));

        gradeRepository.save(new Grade(GradeValue.THREE_HALF, student1, course2, "Final project – need more encapsulation"));
        gradeRepository.save(new Grade(GradeValue.FOUR,       student1, course2, "Design patterns assignment"));

        gradeRepository.save(new Grade(GradeValue.FIVE,       student2, course2, "Perfect understanding of Polymorphism"));
        gradeRepository.save(new Grade(GradeValue.FOUR_HALF,  student2, course2, "Midterm"));

        gradeRepository.save(new Grade(GradeValue.FOUR,       student3, course2, "Class participation and activity"));
        gradeRepository.save(new Grade(GradeValue.THREE_HALF, student3, course2, "Interface task — partially done"));

        gradeRepository.save(new Grade(GradeValue.FOUR_HALF,  student4, course2, "Solid OOP principles in project"));
        gradeRepository.save(new Grade(GradeValue.FOUR,       student4, course2, "Midterm Exam"));

        gradeRepository.save(new Grade(GradeValue.FIVE,       student5, course2, "Best project in the group"));
        gradeRepository.save(new Grade(GradeValue.FIVE,       student5, course2, "Bonus task completed"));

        gradeRepository.save(new Grade(GradeValue.THREE,      student6, course2, "Struggled with abstract classes"));
        gradeRepository.save(new Grade(GradeValue.FOUR,       student6, course2, "Improved — second assignment"));

        gradeRepository.save(new Grade(GradeValue.FOUR_HALF,  student7, course2, "Good grasp of inheritance"));
        gradeRepository.save(new Grade(GradeValue.FOUR,       student7, course2, "Midterm"));

        gradeRepository.save(new Grade(GradeValue.FIVE,       student1, course3, "SQL project — advanced queries"));
        gradeRepository.save(new Grade(GradeValue.FOUR_HALF,  student1, course3, "Normalization task"));
        gradeRepository.save(new Grade(GradeValue.FOUR,       student1, course3, "Midterm Exam"));

        gradeRepository.save(new Grade(GradeValue.FOUR,       student2, course3, "Relational model homework"));
        gradeRepository.save(new Grade(GradeValue.THREE_HALF, student2, course3, "Quiz — joins and subqueries"));
        gradeRepository.save(new Grade(GradeValue.FOUR_HALF,  student2, course3, "Final project"));

        gradeRepository.save(new Grade(GradeValue.THREE,      student3, course3, "Needs more practice with SQL"));
        gradeRepository.save(new Grade(GradeValue.THREE_HALF, student3, course3, "Midterm"));

        gradeRepository.save(new Grade(GradeValue.FOUR_HALF,  student4, course3, "Very good ER diagram"));
        gradeRepository.save(new Grade(GradeValue.FOUR,       student4, course3, "SQL advanced task"));
        gradeRepository.save(new Grade(GradeValue.FIVE,       student4, course3, "Final exam"));

        gradeRepository.save(new Grade(GradeValue.FOUR,       student3, course4, "HTML/CSS landing page project"));
        gradeRepository.save(new Grade(GradeValue.FOUR_HALF,  student3, course4, "JavaScript task — DOM events"));

        gradeRepository.save(new Grade(GradeValue.FIVE,       student5, course4, "Best frontend project in group"));
        gradeRepository.save(new Grade(GradeValue.FOUR_HALF,  student5, course4, "REST API integration task"));
        gradeRepository.save(new Grade(GradeValue.FIVE,       student5, course4, "Midterm"));

        gradeRepository.save(new Grade(GradeValue.THREE_HALF, student6, course4, "Incomplete CSS task"));
        gradeRepository.save(new Grade(GradeValue.FOUR,       student6, course4, "JavaScript — better performance"));

        gradeRepository.save(new Grade(GradeValue.FOUR_HALF,  student7, course4, "Clean and semantic HTML"));
        gradeRepository.save(new Grade(GradeValue.FOUR,       student7, course4, "Midterm Exam"));
        gradeRepository.save(new Grade(GradeValue.FIVE,       student7, course4, "Final project — impressive work"));

        gradeRepository.save(new Grade(GradeValue.FOUR_HALF,  student1, course5, "Vectors homework"));
        gradeRepository.save(new Grade(GradeValue.FOUR,       student1, course5, "Midterm"));
        gradeRepository.save(new Grade(GradeValue.THREE_HALF, student1, course5, "Eigenvalues quiz — tough topic"));

        gradeRepository.save(new Grade(GradeValue.THREE,      student2, course5, "Struggled with transformations"));
        gradeRepository.save(new Grade(GradeValue.THREE_HALF, student2, course5, "Retake — improved result"));

        gradeRepository.save(new Grade(GradeValue.FIVE,       student3, course5, "Outstanding — extra credit task"));
        gradeRepository.save(new Grade(GradeValue.FOUR_HALF,  student3, course5, "Midterm Exam"));
        gradeRepository.save(new Grade(GradeValue.FIVE,       student3, course5, "Final Exam"));

        gradeRepository.save(new Grade(GradeValue.FOUR,       student4, course5, "Good matrix operations"));
        gradeRepository.save(new Grade(GradeValue.FOUR_HALF,  student4, course5, "Linear transformations project"));
        gradeRepository.save(new Grade(GradeValue.FOUR,       student4, course5, "Final Exam"));

        log.info("🎉 Full data initialization completed!");
    }
}