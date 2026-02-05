package com.courseplatform.course_platform.config;

import com.courseplatform.course_platform.model.Course;
import com.courseplatform.course_platform.model.Subtopic;
import com.courseplatform.course_platform.model.Topic;
import com.courseplatform.course_platform.repository.CourseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    public DataLoader(CourseRepository courseRepository, ObjectMapper objectMapper) {
        this.courseRepository = courseRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        if (courseRepository.count() == 0) {
            System.out.println("Loading seed data to Neon DB...");
            try (InputStream inputStream = TypeReference.class.getResourceAsStream("/courses.json")) {
                DataWrapper wrapper = objectMapper.readValue(inputStream, DataWrapper.class);
                List<Course> courses = wrapper.getCourses();
                for (Course course : courses) {
                    for (Topic topic : course.getTopics()) {
                        topic.setCourse(course);
                        for (Subtopic subtopic : topic.getSubtopics()) {
                            subtopic.setTopic(topic);
                        }
                    }
                }
                courseRepository.saveAll(courses);
                System.out.println("Seed data loaded successfully!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @lombok.Data
    static class DataWrapper {
        private List<Course> courses;
    }
}