package com.courseplatform.course_platform.repository;

import com.courseplatform.course_platform.model.Enrollment;
import com.courseplatform.course_platform.model.SubtopicProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubtopicProgressRepository extends JpaRepository<SubtopicProgress, Long> {
    boolean existsByEnrollmentAndSubtopicId(Enrollment enrollment, String subtopicId);
    long countByEnrollment(Enrollment enrollment);
}
