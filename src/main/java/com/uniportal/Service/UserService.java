package com.uniportal.Service;


import com.uniportal.Exceptions.BusinessLogicException;
import com.uniportal.Exceptions.ConflictException;
import com.uniportal.Exceptions.ResourceNotFoundException;
import com.uniportal.Repository.CourseRepository;
import com.uniportal.Repository.StudentRepository;
import com.uniportal.Repository.TeacherRepository;
import com.uniportal.Repository.UserRepository;
import com.uniportal.Course.Course;
import com.uniportal.User.Dto.StudentRequestDto;
import com.uniportal.User.Dto.StudentResponseDto;
import com.uniportal.User.Dto.TeacherRequestDto;
import com.uniportal.User.Dto.TeacherResponseDto;
import com.uniportal.User.Student;
import com.uniportal.User.Teacher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class UserService {

    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;
    private UserRepository userRepository;
    private CourseRepository courseRepository;

    public UserService(StudentRepository studentRepository,
                       TeacherRepository teacherRepository,
                       UserRepository userRepository,
                       CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public StudentResponseDto createStudent(StudentRequestDto requestDto){


        Student student = new Student(
                requestDto.firstName(),
                requestDto.lastName(),
                generateEmail(requestDto.firstName(), requestDto.lastName()),
                requestDto.password(),
                indexGenerator());

        Student savedStudent = studentRepository.save(student);
        return mapToResponse(savedStudent);
    }

    @Transactional
    public TeacherResponseDto createTeacher(TeacherRequestDto requestDto){
        String generatedEmail = generateEmail(requestDto.firstName(), requestDto.lastName());

        if (userRepository.existsByEmail(generatedEmail)) {
            throw new ConflictException("User with this email already exists: " + generatedEmail);
        }

        Teacher teacher = new Teacher(
                requestDto.firstName(),
                requestDto.lastName(),
                generatedEmail,
                requestDto.password(),
                requestDto.academicTitle(),
                requestDto.departmentName());

        Teacher savedTeacher = teacherRepository.save(teacher);
        return mapToResponse(savedTeacher);
    }






    @Transactional
    public void deleteStudent(Long id){
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

    List<Course> courses = courseRepository.findAllByStudentsContaining(student);
    courses.forEach(course -> course.getStudents().remove(student));

    studentRepository.delete(student);
    }

    @Transactional
    public void deleteTeacher(Long id){
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

    List<Course> courses = courseRepository.findAllByTeacher(teacher);
    courses.forEach(course-> course.setTeacher(null));
    teacherRepository.delete(teacher);
    }


    //private mothods------------------------------------------------------

    private String generateEmail(String firstName,String lastName){
        String base = firstName.toLowerCase() + "." + lastName.toLowerCase();
    String email = base + "@uniportal.com";
    Random random = new Random();
    int attempts = 0;


    while (userRepository.existsByEmail(email)) {
        if (attempts >= 5) {
            throw new ConflictException("User with this email already exists");
        }
        int number = random.nextInt(100);
        email = base + number + "@uniportal.com";
        attempts++;
    }
    return email;
    }

    private String indexGenerator(){
        Random random = new Random();
        String indexNumber = String.valueOf(100000 + random.nextInt(900000));
        while(studentRepository.existsByIndexNumber(indexNumber)){
            indexNumber = String.valueOf(100000 + random.nextInt(900000));
        }
        return indexNumber;
    }

    private StudentResponseDto mapToResponse(Student student) {
    return new StudentResponseDto(
        student.getId(),
        student.getFirstName(),
        student.getLastName(),
        student.getEmail(),
        student.getIndexNumber()
    );
    }

    private TeacherResponseDto mapToResponse(Teacher teacher) {
    return new TeacherResponseDto(
        teacher.getId(),
        teacher.getFirstName(),
        teacher.getLastName(),
        teacher.getEmail(),
        teacher.getAcademicTitle(),
        teacher.getDepartment()
    );
}



}
