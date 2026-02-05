package com.courseplatform.course_platform.controller;

import com.courseplatform.course_platform.model.*;
import com.courseplatform.course_platform.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EnrollmentController {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final SubtopicRepository subtopicRepository;
    private final SubtopicProgressRepository progressRepository;

    public EnrollmentController(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository, UserRepository userRepository, SubtopicRepository subtopicRepository, SubtopicProgressRepository progressRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.subtopicRepository = subtopicRepository;
        this.progressRepository = progressRepository;
    }

    // 1. ENROLL IN A COURSE
    @PostMapping("/courses/{courseId}/enroll")
    public ResponseEntity<?> enrollUser(@PathVariable String courseId, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (enrollmentRepository.existsByUserAndCourse(user, course)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Already enrolled"));
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollmentRepository.save(enrollment);

        return ResponseEntity.ok(Map.of("message", "Enrolled successfully", "enrollmentId", enrollment.getId()));
    }

    // 2. MARK SUBTOPIC AS COMPLETE
    @PostMapping("/subtopics/{subtopicId}/complete")
    @Transactional
    public ResponseEntity<?> completeSubtopic(@PathVariable String subtopicId, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        // Find the subtopic and its parent course
        Subtopic subtopic = subtopicRepository.findById(subtopicId)
                .orElseThrow(() -> new RuntimeException("Subtopic not found"));
        Course course = subtopic.getTopic().getCourse();

        // Find the user's enrollment for this specific course
        Enrollment enrollment = enrollmentRepository.findByUserAndCourseId(user, course.getId())
                .orElseThrow(() -> new RuntimeException("You must enroll in this course first"));

        // Check if already completed
        if (!progressRepository.existsByEnrollmentAndSubtopicId(enrollment, subtopicId)) {
            SubtopicProgress progress = new SubtopicProgress();
            progress.setEnrollment(enrollment);
            progress.setSubtopicId(subtopicId);
            progressRepository.save(progress);
        }

        return ResponseEntity.ok(Map.of("message", "Subtopic marked as complete"));
    }

    // 3. VIEW PROGRESS
    @GetMapping("/enrollments/{enrollmentId}/progress")
    public ResponseEntity<?> getProgress(@PathVariable Long enrollmentId, Principal principal) {
        String email = principal.getName();
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        // Security Check: Ensure the logged-in user owns this enrollment
        if (!enrollment.getUser().getEmail().equals(email)) {
            return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
        }

        // Calculate Stats
        long totalSubtopics = enrollment.getCourse().getTopics().stream()
                .mapToLong(t -> t.getSubtopics().size()).sum();

        long completedCount = progressRepository.countByEnrollment(enrollment);

        return ResponseEntity.ok(Map.of(
                "courseTitle", enrollment.getCourse().getTitle(),
                "totalSubtopics", totalSubtopics,
                "completedSubtopics", completedCount,
                "completionPercentage", totalSubtopics == 0 ? 0 : (double) completedCount / totalSubtopics * 100
        ));
    }
}