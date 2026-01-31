package com.uniportal.Service;

import com.uniportal.Course.Course;
import com.uniportal.Enums.AcademicTitle;
import com.uniportal.Enums.DepartmentName;
import com.uniportal.Exceptions.ConflictException;
import com.uniportal.Repository.CourseRepository;
import com.uniportal.Repository.StudentRepository;
import com.uniportal.Repository.TeacherRepository;
import com.uniportal.Repository.UserRepository;
import com.uniportal.User.Dto.StudentRequestDto;
import com.uniportal.User.Dto.StudentResponseDto;
import com.uniportal.User.Dto.TeacherRequestDto;
import com.uniportal.User.Dto.TeacherResponseDto;
import com.uniportal.User.Student;
import com.uniportal.User.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void canCreateStudent() {
        // given
    StudentRequestDto requestDto = new StudentRequestDto(
            "Jan",
            "Kowalski",
            "password123");
    String expectedEmail = "jan.kowalski@uniportal.com";

        Student savedStudent = new Student(
                requestDto.firstName(),
                requestDto.lastName(),
                expectedEmail,
                requestDto.password(),
                "1");
        savedStudent.setId(1L);

    // when
    when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);
    StudentResponseDto result = userService.createStudent(requestDto);

    // then
    verify(studentRepository).save(any(Student.class));

        assertEquals(savedStudent.getId(),result.id());
        assertEquals(savedStudent.getFirstName(),result.firstName());
        assertEquals(savedStudent.getLastName(),result.lastName());
        assertEquals(savedStudent.getEmail(),result.email());
        assertEquals(savedStudent.getIndexNumber(),result.indexNumber());


    }

     @Test
    void createStudent_ShouldThrowConflictException_WhenEmailAlreadyExists() {
        // given
    StudentRequestDto requestDto = new StudentRequestDto(
            "Jan",
            "Kowalski",
            "password123");
    String existingEmail = "jan.kowalski@uniportal.com";

    // when
         when(userRepository.existsByEmail(anyString())).thenReturn(true);
         assertThatThrownBy(()->userService.createStudent(requestDto))
                 .isInstanceOf(ConflictException.class).hasMessage("User with this email already exists");

    // then
    verify(userRepository,times(6)).existsByEmail(anyString());
    verify(studentRepository,never()).save(any());



    }



    @Test
    void canCreateTeacher() {
        //given
            TeacherRequestDto requestDto = new TeacherRequestDto("Adam",
                    "Kulas",
                    "password123",
                    AcademicTitle.dr,
                    DepartmentName.COMPUTER_SCIENCE);
        String expectedEmail = "adam.kulas@uniportal.com";
        Teacher savedTeacher = new Teacher(requestDto.firstName(),
                    requestDto.lastName(),
                    expectedEmail,
                    requestDto.password(),
                    requestDto.academicTitle(),
                    requestDto.departmentName());
        savedTeacher.setId(1L);
        //when
        when(teacherRepository.save(any(Teacher.class))).thenReturn(savedTeacher);
        TeacherResponseDto result = userService.createTeacher(requestDto);

        //then
        verify(teacherRepository).save(any(Teacher.class));

        assertEquals(savedTeacher.getId(),result.id());
        assertEquals(savedTeacher.getFirstName(),result.firstName());
        assertEquals(savedTeacher.getLastName(),result.lastName());
        assertEquals(savedTeacher.getEmail(),result.email());
        assertEquals(savedTeacher.getAcademicTitle(),result.academicTitle());
        assertEquals(savedTeacher.getDepartment(),result.department());


    }


     @Test
    void createTeacher_ShouldThrowConflictException_WhenEmailAlreadyExists() {
        //given
            TeacherRequestDto requestDto = new TeacherRequestDto("Adam",
                    "Kulas",
                    "password123",
                    AcademicTitle.dr,
                    DepartmentName.COMPUTER_SCIENCE);
        String expectedEmail = "adam.kulas@uniportal.com";
        // when
         when(userRepository.existsByEmail(anyString())).thenReturn(true);
         assertThatThrownBy(()->userService.createTeacher(requestDto))
                 .isInstanceOf(ConflictException.class).hasMessage("User with this email already exists");

    // then
    verify(userRepository,times(6)).existsByEmail(anyString());
    verify(teacherRepository,never()).save(any());




    }

    @Test
    void canDeleteStudent() {
        //given
        Long idToDelete = 1L;
        Student savedStudent = new Student(
            "Jan",
            "Kowalski",
            "jan.kowalski@uniportal.com",
            "password123",
                "123456");
        savedStudent.setId(1L);

        Set<Student> students = new HashSet<>();
        students.add(savedStudent);
        Course course = new Course("Mathematics","MAT-1", null, students);

        //when
        when(studentRepository
                .findById(idToDelete))
                .thenReturn(Optional.of(savedStudent));
        when(courseRepository.findAllByStudentsContaining(savedStudent)).thenReturn(List.of(course));
        userService.deleteStudent(idToDelete);

        //then

        verify(studentRepository).findById(savedStudent.getId());
        verify(courseRepository).findAllByStudentsContaining(savedStudent);
        verify(studentRepository).delete(savedStudent);

        assertFalse(course.getStudents().contains(savedStudent));


    }

    @Test
    void canDeleteTeacher() {
        //given
        Teacher savedTeacher = new Teacher("Adam",
                "Kulas",
                "adam.kulas@uniportal.com",
                "password123",
                AcademicTitle.dr,
                DepartmentName.COMPUTER_SCIENCE);
        savedTeacher.setId(2L);
        Long idToDelete = 2L;
        Course course = new Course("Mathematics", "MAT-1", savedTeacher, new HashSet<>());
        course.setTeacher(savedTeacher);

        //when
        when(teacherRepository.findById(idToDelete)).thenReturn(Optional.of(savedTeacher));
        when(courseRepository.findAllByTeacher(savedTeacher)).thenReturn(List.of(course));
        userService.deleteTeacher(idToDelete);

        //then
        verify(teacherRepository).delete(savedTeacher);
        verify(courseRepository).findAllByTeacher(savedTeacher);

        assertNull(course.getTeacher());

    }


    private Student testStudent(){
        Student savedStudent = new Student(
            "Jan",
            "Kowalski",
            "jan.kowalski@uniportal.com",
            "password123",
                "123456");
        savedStudent.setId(1L);

        return savedStudent;
    }

    private Teacher testTeacher() {
    Teacher teacher = new Teacher(
        "Adam",
        "Kulas",
        "adam.kulas@uniportal.com",
        "password123",
        AcademicTitle.dr,
        DepartmentName.COMPUTER_SCIENCE
    );
    teacher.setId(2L);
    return teacher;
}


}