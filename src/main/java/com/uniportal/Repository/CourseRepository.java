package com.uniportal.Repository;

import com.uniportal.Course.Course;
import com.uniportal.User.Student;
import com.uniportal.User.Teacher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course,Long> {
    Boolean existsByCode(String code);

    @EntityGraph(attributePaths = {"teacher"})
    List<Course> readAllBy();


    List<Course> findAllByStudentsContaining(Student student);

    boolean existsByTeacher(Teacher teacher);

    @Query("SELECT c FROM  Course c left join fetch  c.students s left join fetch c.grades g where c.id = :courseId")
    Optional<Course> findCourseWithStudentsAndGrades(@Param("courseId") Long courseId);

}
