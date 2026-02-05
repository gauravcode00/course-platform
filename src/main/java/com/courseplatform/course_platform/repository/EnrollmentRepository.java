package com.courseplatform.course_platform.repository;

import com.courseplatform.course_platform.model.Course;
import com.courseplatform.course_platform.model.Enrollment;
import com.courseplatform.course_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByUserAndCourse(User user, Course course);
    Optional<Enrollment> findByUserAndCourseId(User user, String courseId);
    List<Enrollment> findByUser(User user);
}