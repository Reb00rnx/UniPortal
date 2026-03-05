package com.uniportal.Service;


import com.uniportal.Course.ConsultationData;
import com.uniportal.Course.Dto.ConsultationRequestDto;
import com.uniportal.Course.Dto.ConsultationResponseDto;
import com.uniportal.Exceptions.ResourceNotFoundException;
import com.uniportal.Repository.ConsultationRepository;
import com.uniportal.Repository.TeacherRepository;
import com.uniportal.User.Teacher;
import org.springframework.stereotype.Service;

@Service
public class ConsultationService {


    private final ConsultationRepository consultationRepository;
    private final TeacherRepository teacherRepository;

    public ConsultationService(ConsultationRepository consultationRepository, TeacherRepository teacherRepository) {
        this.consultationRepository = consultationRepository;
        this.teacherRepository = teacherRepository;
    }


    public ConsultationResponseDto saveData(ConsultationRequestDto request) {
    if (consultationRepository.findByTeacherId(request.teacherId()).isPresent()) {
        throw new IllegalStateException("Teacher already has consultation data. Use update instead.");
    }

    Teacher teacher = teacherRepository.findById(request.teacherId())
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

    ConsultationData consultationData = new ConsultationData();
    consultationData.setCon_day(request.con_day());
    consultationData.setStartTime(request.startTime());
    consultationData.setEndTime(request.endTime());
    consultationData.setRoom(request.room());
    consultationData.setTeacher(teacher);

    ConsultationData savedData = consultationRepository.save(consultationData);

    return mapToResponse(savedData);
}




    public ConsultationResponseDto updateData(ConsultationRequestDto request, Long teacherId){
        ConsultationData consultationData = consultationRepository
                .findByTeacherId(teacherId)
                .orElseThrow(()->new ResourceNotFoundException("Consultation data not found"));
        consultationData.setCon_day(request.con_day());
        consultationData.setStartTime(request.startTime());
        consultationData.setEndTime(request.endTime());
        consultationData.setRoom(request.room());

        ConsultationData savedData = consultationRepository.save(consultationData);

        return mapToResponse(savedData);
    }


    public ConsultationResponseDto getConsultationData(Long teacherId){
        ConsultationData consultationData = consultationRepository
                .findByTeacherId(teacherId)
                .orElseThrow(()->new ResourceNotFoundException("No consultation data found with this teacher id"));
        return mapToResponse(consultationData);
    }


    private ConsultationResponseDto mapToResponse(ConsultationData entity) {
    return new ConsultationResponseDto(
        entity.getId(),
        entity.getCon_day(),
        entity.getStartTime(),
        entity.getEndTime(),
        entity.getRoom()
    );
}

}
