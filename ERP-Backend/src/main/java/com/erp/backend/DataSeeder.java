package com.erp.backend;

import com.erp.backend.entity.Course;
import com.erp.backend.entity.Enrollment;
import com.erp.backend.entity.User;
import com.erp.backend.repository.CourseRepository;
import com.erp.backend.repository.EnrollmentRepository;
import com.erp.backend.repository.UserRepository;
import com.erp.backend.service.UserService;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // --- Create faculty members if they don't exist ---
        User profRavi = createTeacherIfNotExists("prof_ravi", "ravi@kluniversity.in", "Prof@2024!", "Dr. Ravi", "Kumar");
        User profSuresh = createTeacherIfNotExists("prof_suresh", "suresh@kluniversity.in", "Prof@2024!", "Dr. Suresh", "Babu");
        User profLakshmi = createTeacherIfNotExists("prof_lakshmi", "lakshmi@kluniversity.in", "Prof@2024!", "Dr. Lakshmi", "Devi");
        User profAnand = createTeacherIfNotExists("prof_anand", "anand@kluniversity.in", "Prof@2024!", "Dr. Anand", "Rao");
        User profPriya = createTeacherIfNotExists("prof_priya", "priya@kluniversity.in", "Prof@2024!", "Dr. Priya", "Sharma");
        User profKiran = createTeacherIfNotExists("prof_kiran", "kiran@kluniversity.in", "Prof@2024!", "Dr. Kiran", "Reddy");

        // --- Seed B.Tech CSE Courses (Semester 1-2: Foundation) ---
        createCourseIfNotExists("23CS1101", "Problem Solving Using C", "Fundamentals of programming using C language including arrays, pointers, and file handling.", profRavi, 4, "Spring", "Mon & Wed 9:00 AM - 10:30 AM", "C-Block, Room 201", null);
        createCourseIfNotExists("23CS1102", "IT Workshop", "Hands-on workshop covering OS installation, networking basics, and office tools.", profSuresh, 2, "Spring", "Fri 2:00 PM - 4:00 PM", "CS Lab 1, Block A", null);
        createCourseIfNotExists("23MA1101", "Linear Algebra & Calculus", "Matrices, eigenvalues, differential and integral calculus for engineering.", profLakshmi, 4, "Spring", "Tue & Thu 9:00 AM - 10:30 AM", "D-Block, Room 105", null);
        createCourseIfNotExists("23PH1101", "Engineering Physics", "Quantum mechanics, optics, semiconductor physics, and nanomaterials.", profAnand, 4, "Spring", "Mon & Wed 11:00 AM - 12:30 PM", "Physics Lab, Block B", null);
        createCourseIfNotExists("23EE1101", "Basic Electrical Engineering", "DC/AC circuits, transformers, electrical machines, and power systems.", profKiran, 3, "Spring", "Tue & Thu 11:00 AM - 12:15 PM", "EE Block, Room 301", null);
        createCourseIfNotExists("23EN1101", "Communicative English", "English grammar, vocabulary, and professional communication skills.", profPriya, 3, "Spring", "Wed & Fri 9:00 AM - 10:15 AM", "Language Lab, Block C", null);

        // --- Semester 2 ---
        createCourseIfNotExists("23CS1201", "Data Structures", "Arrays, linked lists, stacks, queues, trees, graphs, and hashing algorithms.", profRavi, 4, "Fall", "Mon & Wed 10:00 AM - 11:30 AM", "C-Block, Room 202", "23CS1101");
        createCourseIfNotExists("23CS1202", "Object Oriented Programming (Java)", "OOP concepts, inheritance, polymorphism, exception handling using Java.", profSuresh, 4, "Fall", "Tue & Thu 10:00 AM - 11:30 AM", "CS Lab 2, Block A", "23CS1101");
        createCourseIfNotExists("23MA1201", "Probability & Statistics", "Random variables, probability distributions, hypothesis testing, and regression.", profLakshmi, 4, "Fall", "Mon & Wed 2:00 PM - 3:30 PM", "D-Block, Room 106", "23MA1101");
        createCourseIfNotExists("23CH1201", "Engineering Chemistry", "Chemical bonding, polymers, corrosion, water treatment, and green chemistry.", profAnand, 4, "Fall", "Tue & Thu 2:00 PM - 3:30 PM", "Chemistry Lab, Block B", null);

        // --- Semester 3: Core CS ---
        createCourseIfNotExists("23CS2101", "Design & Analysis of Algorithms", "Divide and conquer, dynamic programming, greedy algorithms, NP-completeness.", profRavi, 4, "Spring", "Mon & Wed 9:00 AM - 10:30 AM", "C-Block, Room 301", "23CS1201");
        createCourseIfNotExists("23CS2102", "Database Management Systems", "ER modeling, SQL, normalization, transactions, and query optimization.", profSuresh, 4, "Spring", "Tue & Thu 9:00 AM - 10:30 AM", "CS Lab 3, Block A", "23CS1201");
        createCourseIfNotExists("23CS2103", "Computer Organization & Architecture", "CPU design, pipelining, memory hierarchy, I/O systems, and RISC/CISC.", profKiran, 4, "Spring", "Mon & Wed 11:00 AM - 12:30 PM", "C-Block, Room 302", null);
        createCourseIfNotExists("23CS2104", "Discrete Mathematics", "Logic, sets, relations, graph theory, counting principles, and recurrences.", profLakshmi, 4, "Spring", "Tue & Thu 11:00 AM - 12:30 PM", "D-Block, Room 201", "23MA1101");
        createCourseIfNotExists("23CS2105", "Operating Systems", "Process management, memory management, file systems, and synchronization.", profAnand, 3, "Spring", "Wed & Fri 2:00 PM - 3:15 PM", "C-Block, Room 303", "23CS1201");

        // --- Semester 4: Advanced Core ---
        createCourseIfNotExists("23CS2201", "Software Engineering", "SDLC, agile methodologies, UML, software testing, and project management.", profPriya, 4, "Fall", "Mon & Wed 10:00 AM - 11:30 AM", "C-Block, Room 401", "23CS2102");
        createCourseIfNotExists("23CS2202", "Computer Networks", "OSI model, TCP/IP, routing, switching, network security fundamentals.", profKiran, 4, "Fall", "Tue & Thu 10:00 AM - 11:30 AM", "Networking Lab, Block A", null);
        createCourseIfNotExists("23CS2203", "Theory of Computation", "Finite automata, regular expressions, context-free grammars, Turing machines.", profLakshmi, 4, "Fall", "Mon & Wed 2:00 PM - 3:30 PM", "D-Block, Room 202", "23CS2104");
        createCourseIfNotExists("23CS2204", "Web Technologies", "HTML, CSS, JavaScript, React, Node.js, REST APIs, and full-stack development.", profSuresh, 4, "Fall", "Tue & Thu 2:00 PM - 3:30 PM", "CS Lab 4, Block A", "23CS1202");
        createCourseIfNotExists("23CS2205", "Python Programming", "Python fundamentals, data structures, file I/O, libraries, and automation.", profRavi, 3, "Fall", "Fri 9:00 AM - 11:30 AM", "CS Lab 2, Block A", "23CS1101");

        // --- Semester 5: Specialization ---
        createCourseIfNotExists("23CS3101", "Machine Learning", "Supervised/unsupervised learning, SVM, neural networks, and model evaluation.", profAnand, 4, "Spring", "Mon & Wed 9:00 AM - 10:30 AM", "AI Lab, Block D", "23MA1201");
        createCourseIfNotExists("23CS3102", "Compiler Design", "Lexical analysis, parsing, syntax-directed translation, and code generation.", profLakshmi, 4, "Spring", "Tue & Thu 9:00 AM - 10:30 AM", "C-Block, Room 501", "23CS2203");
        createCourseIfNotExists("23CS4101", "Cyber Security", "Network security, cryptography, digital forensics, and secure coding practices.", profKiran, 4, "Spring", "Mon & Wed 2:00 PM - 3:30 PM", "Security Lab, Block A", "23CS2202");
        createCourseIfNotExists("23CS4102", "Cloud Computing", "Virtualization, IaaS, PaaS, SaaS, AWS/Azure, and cloud architecture.", profSuresh, 3, "Spring", "Fri 10:00 AM - 12:30 PM", "CS Lab 4, Block A", "23CS2105");
        createCourseIfNotExists("23CS4103", "Block Chain Technologies", "Decentralized ledgers, smart contracts, Ethereum, and consensus algorithms.", profAnand, 3, "Spring", "Sat 10:00 AM - 1:00 PM", "CS Lab 1, Block A", "23CS2102");
        createCourseIfNotExists("23CS3103", "Information Security", "Cryptography, PKI, network security, ethical hacking, and digital forensics.", profKiran, 4, "Spring", "Mon & Wed 11:00 AM - 12:30 PM", "Cyber Lab, Block A", "23CS2202");
        createCourseIfNotExists("23CS3104", "Cloud Computing", "Virtualization, AWS/Azure fundamentals, microservices, and serverless computing.", profSuresh, 4, "Spring", "Tue & Thu 11:00 AM - 12:30 PM", "Cloud Lab, Block D", "23CS2202");
        createCourseIfNotExists("23CS3105", "Full Stack Development", "MERN/Spring Boot stack, REST APIs, authentication, and deployment pipelines.", profRavi, 3, "Spring", "Wed & Fri 2:00 PM - 3:15 PM", "CS Lab 5, Block A", "23CS2204");

        // --- Semester 6: Advanced Electives ---
        createCourseIfNotExists("23CS3201", "Artificial Intelligence", "Search algorithms, knowledge representation, NLP, robotics, and expert systems.", profAnand, 4, "Fall", "Mon & Wed 10:00 AM - 11:30 AM", "AI Lab, Block D", "23CS3101");
        createCourseIfNotExists("23CS3202", "Deep Learning", "CNNs, RNNs, GANs, transfer learning, and natural language processing.", profPriya, 4, "Fall", "Tue & Thu 10:00 AM - 11:30 AM", "AI Lab, Block D", "23CS3101");
        createCourseIfNotExists("23CS3203", "Big Data Analytics", "Hadoop, Spark, MapReduce, NoSQL databases, and real-time data processing.", profSuresh, 4, "Fall", "Mon & Wed 2:00 PM - 3:30 PM", "Data Lab, Block D", "23CS2102");
        createCourseIfNotExists("23CS3204", "Internet of Things", "IoT architecture, sensors, embedded systems, MQTT, and smart applications.", profKiran, 4, "Fall", "Tue & Thu 2:00 PM - 3:30 PM", "IoT Lab, Block D", "23CS2202");
        createCourseIfNotExists("23CS3205", "Blockchain Technology", "Distributed ledger, smart contracts, Ethereum, Solidity, and DApps.", profRavi, 3, "Fall", "Fri 9:00 AM - 11:30 AM", "C-Block, Room 502", "23CS2202");

        // --- Additional elective and specialization courses ---
        createCourseIfNotExists("23CS3301", "Mobile Application Development", "Android and iOS app development using Flutter and React Native.", profPriya, 4, "Spring", "Mon & Wed 3:30 PM - 5:00 PM", "Mobile Lab, Block C", "23CS2204");
        createCourseIfNotExists("23CS3302", "DevOps & Cloud Engineering", "CI/CD pipelines, container orchestration, Kubernetes, and cloud infrastructure.", profAnand, 4, "Spring", "Tue & Thu 3:30 PM - 5:00 PM", "Cloud Lab, Block D", "23CS2204");
        createCourseIfNotExists("23CS3303", "Cybersecurity Fundamentals", "Network security, cryptography, secure coding, and ethical hacking basics.", profKiran, 4, "Spring", "Wed & Fri 3:30 PM - 5:00 PM", "Cyber Lab, Block A", "23CS3103");
        createCourseIfNotExists("23CS3304", "Human-Computer Interaction", "User-centered design, accessibility, usability testing, and prototyping.", profLakshmi, 3, "Spring", "Mon & Wed 5:00 PM - 6:30 PM", "Design Studio, Block B", null);
        createCourseIfNotExists("23CS3305", "Entrepreneurship for Engineers", "Startup fundamentals, product-market fit, business modeling, and pitching.", profRavi, 3, "Spring", "Fri 11:30 AM - 1:00 PM", "Management Lab, Block C", null);

        // --- Seed Students ---
        createStudentIfNotExists("student1", "student1@kluniversity.in", "Student@2024!", "Aryan", "Sharma");
        createStudentIfNotExists("student2", "student2@kluniversity.in", "Student@2024!", "Kavita", "Reddy");
        createStudentIfNotExists("student3", "student3@kluniversity.in", "Student@2024!", "Aditya", "Verma");
        createStudentIfNotExists("student4", "student4@kluniversity.in", "Student@2024!", "Sneha", "Gupta");
        createStudentIfNotExists("student5", "student5@kluniversity.in", "Student@2024!", "Rahul", "Nair");
        createStudentIfNotExists("student6", "student6@kluniversity.in", "Student@2024!", "Priya", "Patel");
        createStudentIfNotExists("student7", "student7@kluniversity.in", "Student@2024!", "Vikram", "Singh");
        createStudentIfNotExists("student8", "student8@kluniversity.in", "Student@2024!", "Ananya", "Das");

        // --- Enroll All Students (including manually added ones like 2400030410) ---
        List<User> allStudents = userRepository.findAll().stream()
                .filter(u -> u.getRole() == User.Role.STUDENT)
                .toList();

        List<Course> coreCourses = courseRepository.findAll().stream().limit(5).toList();
        
        for (User student : allStudents) {
            for (Course course : coreCourses) {
                if (!enrollmentRepository.existsByStudentAndCourse(student, course)) {
                    Enrollment enrollment = new Enrollment(student, course);
                    enrollment.setEnrolledAt(java.time.LocalDateTime.now());
                    enrollment.setStatus(Enrollment.EnrollmentStatus.ACTIVE);
                    enrollmentRepository.save(enrollment);
                }
            }
        }

        System.out.println("✅ KL University B.Tech CSE course data and student enrollments seeded successfully!");
    }

    private User createTeacherIfNotExists(String username, String email, String password, String firstName, String lastName) {
        if (userService.findByUsername(username).isEmpty()) {
            User teacher = new User(username, email, password, firstName, lastName, User.Role.TEACHER);
            return userService.createUser(teacher);
        }
        return userService.findByUsername(username).get();
    }

    private User createStudentIfNotExists(String username, String email, String password, String firstName, String lastName) {
        if (userService.findByUsername(username).isEmpty()) {
            User student = new User(username, email, password, firstName, lastName, User.Role.STUDENT);
            return userService.createUser(student);
        }
        return userService.findByUsername(username).get();
    }

    private void createCourseIfNotExists(String code, String name, String description, User teacher, int credits, String semester, String schedule, String location, String prerequisites) {
        if (courseRepository.findByCourseCode(code).isEmpty()) {
            Course course = new Course(code, name, description, teacher);
            course.setCredits(credits);
            course.setSemester(semester);
            course.setSchedule(schedule);
            course.setLocation(location);
            course.setPrerequisites(prerequisites);
            course.setCapacity(60);
            courseRepository.save(course);
        }
    }
}