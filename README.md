Course Platform API
✨ Key Features
1. Public Browsing & Deep Search
  Browse Courses: Users can view a list of all available courses and deep-dive into specific course details (Topics & Subtopics).

  Deep Search Engine: A custom SQL query searches for keywords across Course Titles, Topic Titles, and even inside Subtopic Markdown Content.

  Example: Searching for "velocity" finds the Physics course because the word appears inside the subtopic text.

2. Authentication & Security
  JWT Implementation: Custom security filter that validates Bearer tokens for every protected request.

  Stateless: No server-side sessions; scalable authentication using tokens.

  Public vs Private: Search and Course Viewing are public; Enrollment and Progress are private.

3. Student Progress Tracking
  Enrollment System: Prevents duplicate enrollments (User cannot join the same course twice).

  Progress Tracking: Users can mark specific subtopics as "Complete".

  Analytics: Calculates completion percentage (e.g., "55% Completed") based on total subtopics vs. completed ones.

4. Automatic Seeding
  Data Loader: On startup, the application checks if the database is empty. If yes, it automatically parses a courses.json file and populates the database with dummy data (Physics, Math, CS courses).

<img width="707" height="431" alt="image" src="https://github.com/user-attachments/assets/696054cf-8c9a-4d6d-970e-a27f3a00b05d" />

