package com.uniportal.Repository;

import com.uniportal.Course.ConsultationData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsultationRepository extends JpaRepository<ConsultationData,Long> {

    Optional<ConsultationData> findByTeacherId(long teacherId);
}
