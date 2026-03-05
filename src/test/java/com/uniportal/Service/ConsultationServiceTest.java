package com.uniportal.Service;

import com.uniportal.Course.ConsultationData;
import com.uniportal.Course.Dto.ConsultationRequestDto;
import com.uniportal.Course.Dto.ConsultationResponseDto;
import com.uniportal.Enums.AcademicTitle;
import com.uniportal.Enums.Day;
import com.uniportal.Enums.DepartmentName;
import com.uniportal.Exceptions.ResourceNotFoundException;
import com.uniportal.Repository.ConsultationRepository;
import com.uniportal.Repository.TeacherRepository;
import com.uniportal.User.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultationServiceTest {

    @Mock
    private  ConsultationRepository consultationRepository;
    @Mock
    private  TeacherRepository teacherRepository;

    @InjectMocks
    private ConsultationService consultationService;

    @Test
    void canSaveData() {
        //given
        Teacher testTeacher = createTestTeacher();
        ConsultationRequestDto requestDto = createRequest();
        ConsultationData testData = createData();

        when(teacherRepository.findById(requestDto.teacherId())).thenReturn(Optional.of(testTeacher));
        when(consultationRepository.findByTeacherId(requestDto.teacherId())).thenReturn(Optional.empty());
        when(consultationRepository.save(any(ConsultationData.class))).thenReturn(testData);

        //when
        ConsultationResponseDto responseDto = consultationService.saveData(requestDto);

        //then
        verify(teacherRepository).findById(requestDto.teacherId());
        verify(consultationRepository).save(any(ConsultationData.class));
        verify(consultationRepository).findByTeacherId(requestDto.teacherId());

        assertThat(requestDto.con_day()).isEqualTo(responseDto.con_day());
        assertThat(requestDto.startTime()).isEqualTo(responseDto.startTime());
        assertThat(requestDto.endTime()).isEqualTo(responseDto.endTime());
        assertThat(requestDto.room()).isEqualTo(responseDto.room());
    }

    @Test
    void canSaveData_ShouldThrowIllegalStateException_WhenConsultationDataIsPresent() {
        //given
        Teacher testTeacher = createTestTeacher();
        ConsultationRequestDto requestDto = createRequest();
        ConsultationData testData = createData();


        when(consultationRepository.findByTeacherId(requestDto.teacherId())).thenReturn(Optional.of(testData));
        //when
        //then
        assertThatThrownBy(()->consultationService.saveData(requestDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Teacher already has consultation data. Use update instead.");
        verify(consultationRepository).findByTeacherId(requestDto.teacherId());

        verify(teacherRepository, never()).findById(anyLong());
     verify(consultationRepository, never()).save(any(ConsultationData.class));


    }

    @Test
    void canSaveData_ShouldThrowResourceNotFoundException_WhenTeacherNotFound() {
        //given
        Teacher testTeacher = createTestTeacher();
        ConsultationRequestDto requestDto = createRequest();
        ConsultationData testData = createData();

        when(teacherRepository.findById(requestDto.teacherId())).thenReturn(Optional.empty());
        when(consultationRepository.findByTeacherId(requestDto.teacherId())).thenReturn(Optional.empty());
        //when
        assertThatThrownBy(()->consultationService.saveData(requestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Teacher not found");

        //then
        verify(teacherRepository).findById(requestDto.teacherId());
        verify(consultationRepository,never()).save(any(ConsultationData.class));
        verify(consultationRepository).findByTeacherId(requestDto.teacherId());
    }


    @Test
    void canUpdateData() {
        //given
        Long givenId = createTestTeacher().getId();
        ConsultationData testData = createData();
        ConsultationRequestDto requestDto = createRequest();

        when(consultationRepository.findByTeacherId(givenId)).thenReturn(Optional.of(testData));
        when(consultationRepository.save(any(ConsultationData.class))).thenReturn(testData);
        //when
        ConsultationResponseDto responseDto = consultationService.updateData(requestDto,givenId);
        //then
        verify(consultationRepository).findByTeacherId(givenId);
        verify(consultationRepository).save(any(ConsultationData.class));

        assertThat(requestDto.con_day()).isEqualTo(responseDto.con_day());
        assertThat(requestDto.startTime()).isEqualTo(responseDto.startTime());
        assertThat(requestDto.endTime()).isEqualTo(responseDto.endTime());
        assertThat(requestDto.room()).isEqualTo(responseDto.room());

    }

    @Test
    void canUpdateData_throwResourceNotFoundException_whenConsultationDataNotFound() {
        //given
        Long givenId = createTestTeacher().getId();
        ConsultationData testData = createData();
        ConsultationRequestDto requestDto = createRequest();

        when(consultationRepository.findByTeacherId(givenId)).thenReturn(Optional.empty());
        //when
        assertThatThrownBy(()->consultationService.updateData(requestDto,givenId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Consultation data not found");
        //then
        verify(consultationRepository).findByTeacherId(givenId);
        verify(consultationRepository,never()).save(any(ConsultationData.class));
    }

    @Test
    void canGetConsultationData() {
        //given
        Long givenId = createTestTeacher().getId();
        ConsultationData testData = createData();
        when(consultationRepository.findByTeacherId(givenId)).thenReturn(Optional.of(testData));
        //when
        ConsultationResponseDto responseDto = consultationService.getConsultationData(givenId);
        //then
        verify(consultationRepository).findByTeacherId(givenId);
        assertThat(responseDto.con_day()).isEqualTo(testData.getCon_day());
        assertThat(responseDto.startTime()).isEqualTo(testData.getStartTime());
        assertThat(responseDto.endTime()).isEqualTo(testData.getEndTime());
        assertThat(responseDto.room()).isEqualTo(testData.getRoom());




    }



     @Test
    void canGetConsultationData_throwResourceNotFoundException_whenTeacherNotFound() {
        //given
        Long givenId = createTestTeacher().getId();
        ConsultationData testData = createData();
        when(consultationRepository.findByTeacherId(givenId)).thenReturn(Optional.empty());
        //when
        assertThatThrownBy(()->consultationService.getConsultationData(givenId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No consultation data found with this teacher id");
        //then
        verify(consultationRepository).findByTeacherId(givenId);
    }


    //private methods
    private Teacher createTestTeacher() {
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
    private ConsultationRequestDto createRequest(){
            ConsultationRequestDto requestDto = new ConsultationRequestDto(Day.FRIDAY,
                    "10:00",
                    "12:00",
                    "A-210",
                    2L);
            return requestDto;
    }

    private ConsultationData createData(){
            ConsultationData testData = new ConsultationData(
                1L,
                Day.FRIDAY,
                "10:00",
                "12:00",
                "A-210",
                createTestTeacher()
        );
            return testData;
    }






}


