package com.uniportal.Controller;

import com.uniportal.Course.Dto.ModuleRequestDto;
import com.uniportal.Course.Dto.ModuleResponseDto;
import com.uniportal.Service.ModuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:4200")
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @PostMapping("/{courseId}/modules")
    public ResponseEntity<ModuleResponseDto> addModule(
            @PathVariable Long courseId,
            @RequestBody ModuleRequestDto dto) {
        return ResponseEntity.ok(moduleService.addModule(courseId, dto));
    }

    @DeleteMapping("/modules/{moduleId}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long moduleId) {
        moduleService.deleteModule(moduleId);
        return ResponseEntity.noContent().build();
    }
}
