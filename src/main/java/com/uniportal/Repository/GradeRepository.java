package com.uniportal.Repository;

import com.uniportal.Course.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade,Long> {

    List<Grade> findByStudentId(Long studentId);

    List<Grade> findByStudentIdAndCourseId(Long studentId, Long courseId);
}
