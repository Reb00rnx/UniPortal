package com.uniportal.Service;


import com.uniportal.Enums.Role;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class UserService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(StudentRepository studentRepository,
                       TeacherRepository teacherRepository,
                       UserRepository userRepository,
                       CourseRepository courseRepository,
                       PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Student createStudent(StudentRequestDto requestDto) {
        Student student = Student.builder()
                .firstName(requestDto.firstName())
                .lastName(requestDto.lastName())
                .email(generateEmail(requestDto.firstName(), requestDto.lastName(),Role.STUDENT))
                .password(passwordEncoder.encode(requestDto.password()))
                .role(Role.STUDENT)
                .indexNumber(indexGenerator())
                .build();


        return studentRepository.save(student);
    }

    @Transactional
    public Teacher createTeacher(TeacherRequestDto requestDto) {
        Teacher teacher = Teacher.builder()
                .firstName(requestDto.firstName())
                .lastName(requestDto.lastName())
                .email(generateEmail(requestDto.firstName(), requestDto.lastName(),Role.TEACHER))
                .password(passwordEncoder.encode(requestDto.password()))
                .role(Role.TEACHER)
                .academicTitle(requestDto.academicTitle())
                .department(requestDto.departmentName())
                .build();

        return teacherRepository.save(teacher);
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


    //private methods------------------------------------------------------

    private String generateEmail(String firstName,String lastName, Role role){
        String base = firstName.toLowerCase() + "." + lastName.toLowerCase();

        String template = switch (role){
        case Role.STUDENT-> "@student.uniportal.com";
        case Role.TEACHER-> "@uniportal.com";
        default -> throw new IllegalArgumentException("Unknown role: " + role);
    };
    String email = base + template;

    Random random = new Random();
    int attempts = 0;


    while (userRepository.existsByEmail(email)) {
        if (attempts >= 5) {
            throw new ConflictException("User with this email already exists");
        }
        int number = random.nextInt(100);
        email = base + number + template;
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
