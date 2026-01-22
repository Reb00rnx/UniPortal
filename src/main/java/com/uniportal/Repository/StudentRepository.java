package com.uniportal.Repository;

import com.uniportal.User.Student;
import com.uniportal.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Boolean existsByIndexNumber(String indexNumber);
}
