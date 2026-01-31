package com.uniportal.Service;


import com.uniportal.Course.Dto.CourseRequestDto;
import com.uniportal.Course.Dto.CourseResponseDto;
import com.uniportal.Exceptions.ConflictException;
import com.uniportal.User.Dto.StudentResponseDto;
import com.uniportal.User.Dto.TeacherResponseDto;
import com.uniportal.Exceptions.ResourceNotFoundException;
import com.uniportal.Repository.CourseRepository;
import com.uniportal.Repository.StudentRepository;
import com.uniportal.Repository.TeacherRepository;
import com.uniportal.Course.Course;
import com.uniportal.User.Student;
import com.uniportal.User.Teacher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public CourseService(CourseRepository courseRepository,
                         TeacherRepository teacherRepository,
                         StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public CourseResponseDto createCourse(CourseRequestDto requestDto){
        Teacher teacher = teacherRepository
                .findById(requestDto.teacherId())
                .orElseThrow(()->new ResourceNotFoundException("Teacher not found"));

    Course course = new Course();
    course.setName(requestDto.name());
    course.setCode(courseCode(requestDto.name()));
    course.setTeacher(teacher);
    if(requestDto.students()!=null && !requestDto.students().isEmpty()){
        List<Student> students = studentRepository.findAllById(requestDto.students());

        students.forEach(course::addStudent);
    }

    Course savedCourse = courseRepository.save(course);
    return mapToResponse(savedCourse);
    }

    public List<CourseResponseDto> getAllCourses(){
        return courseRepository.readAllBy().stream().map(this::mapToResponse).toList();
    }

    @Transactional
    public CourseResponseDto enrollStudent(Long courseId, Long studentId){
        Course course= findCourseById(courseId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(()->new ResourceNotFoundException("Student id:" + studentId + " not found"));

        if(course.getStudents().contains(student)){
            throw new ConflictException("Student is already enrolled in this course");
        }
        course.addStudent(student);
        return mapToResponse(course);
    }


    @Transactional
    public void deleteCourse(Long courseId){
        if (!courseRepository.existsById(courseId)) {
        throw new ResourceNotFoundException("Course with id: " + courseId + " not found");
    }
    courseRepository.deleteById(courseId);
    }











    //private methods-------------------------------------------
    private String courseCode(String name){
        String nameSub = name.substring(0,3).toUpperCase()+"-";
        int i =1;
        String subCode = nameSub+i;
        while(courseRepository.existsByCode(subCode)){
            i++;
            subCode = nameSub+i;
        }
        return subCode;

    }

    private Course findCourseById(Long courseId){
        Course course= courseRepository
                .findById(courseId)
                .orElseThrow(()->new ResourceNotFoundException("Course id:" + courseId + " not found"));

        return course;
    }


    private CourseResponseDto mapToResponse(Course course) {

    return new CourseResponseDto(
            course.getId(),
        course.getName(),
        course.getCode(),
        new TeacherResponseDto(
            course.getTeacher().getId(),
            course.getTeacher().getFirstName(),
            course.getTeacher().getLastName(),
            course.getTeacher().getEmail(),
            course.getTeacher().getAcademicTitle(),
            course.getTeacher().getDepartment()
        ),
        course.getStudents().stream().map(s->new StudentResponseDto(s.getId(),
                s.getFirstName(),
                s.getLastName(),
                s.getEmail(),
                s.getIndexNumber()))
                .toList()
    );
    }




}
