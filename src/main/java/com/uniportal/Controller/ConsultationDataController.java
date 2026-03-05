package com.uniportal.Controller;


import com.uniportal.Course.ConsultationData;
import com.uniportal.Course.Dto.ConsultationRequestDto;
import com.uniportal.Course.Dto.ConsultationResponseDto;
import com.uniportal.Service.ConsultationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/consultations")
@CrossOrigin(origins = "http://localhost:4200")
public class ConsultationDataController {

    private final ConsultationService consultationService;


    public ConsultationDataController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @PostMapping
    public ResponseEntity<ConsultationResponseDto> saveData(@RequestBody ConsultationRequestDto request){
        ConsultationResponseDto responseDto = consultationService.saveData(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @PatchMapping("/update/{teacherId}")
    public ResponseEntity<ConsultationResponseDto> updateData(@RequestBody ConsultationRequestDto request,@PathVariable Long teacherId){
        ConsultationResponseDto responseDto = consultationService.updateData(request,teacherId);

        return ResponseEntity.ok(responseDto);
    }


    @GetMapping("/{teacherId}")
    public ResponseEntity<ConsultationResponseDto> getData(@PathVariable Long teacherId){
        ConsultationResponseDto responseDto = consultationService.getConsultationData(teacherId);

        return ResponseEntity.ok(responseDto);
    }





}
