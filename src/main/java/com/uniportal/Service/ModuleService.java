package com.uniportal.Service;

import com.uniportal.Course.Course;
import com.uniportal.Course.CourseModule;
import com.uniportal.Course.Dto.ModuleRequestDto;
import com.uniportal.Course.Dto.ModuleResponseDto;
import com.uniportal.Exceptions.ResourceNotFoundException;
import com.uniportal.Repository.CourseModuleRepository;
import com.uniportal.Repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModuleService {

    private final CourseModuleRepository moduleRepository;
    private final CourseRepository courseRepository;


    public ModuleService(CourseModuleRepository moduleRepository, CourseRepository courseRepository) {
        this.moduleRepository = moduleRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public ModuleResponseDto addModule(Long courseId, ModuleRequestDto dto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        CourseModule module = new CourseModule();
        module.setTitle(dto.title());
        module.setDescription(dto.description());
        module.setOrderIndex(dto.orderIndex());
        module.setCourse(course);

        CourseModule saved = moduleRepository.save(module);
        return mapToResponse(saved);
    }

    @Transactional
    public void deleteModule(Long moduleId) {
        if (!moduleRepository.existsById(moduleId)) {
            throw new ResourceNotFoundException("Module not found");
        }
        moduleRepository.deleteById(moduleId);
    }

    private ModuleResponseDto mapToResponse(CourseModule module) {
        return new ModuleResponseDto(
                module.getId(),
                module.getTitle(),
                module.getDescription(),
                module.getOrderIndex()
        );
    }
}