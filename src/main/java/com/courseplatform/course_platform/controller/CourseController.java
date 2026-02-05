package com.courseplatform.course_platform.controller;


import com.courseplatform.course_platform.model.Course;
import com.courseplatform.course_platform.repository.CourseRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/courses")
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @GetMapping("/courses/{id}")
    public Course getCourse(@PathVariable String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    // search for keywords
    // URL: GET /api/search?q=velocity
    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam("q") String query) {
        List<Course> results = courseRepository.searchCourses(query);
        return Map.of(
                "query", query,
                "results", results
        );
    }
}
