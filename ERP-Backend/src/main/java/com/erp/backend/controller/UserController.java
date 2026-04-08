package com.erp.backend.controller;

import com.erp.backend.entity.User;
import com.erp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/students")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllStudents() {
        try {
            List<User> students = userRepository.findByRole(User.Role.STUDENT);
            List<Map<String, String>> mappedStudents = students.stream()
                    .map(student -> {
                        Map<String, String> map = new HashMap<>();
                        map.put("id", student.getId().toString());
                        map.put("name", student.getFirstName() + " " + student.getLastName());
                        map.put("username", student.getUsername());
                        map.put("email", student.getEmail());
                        return map;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", mappedStudents
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }
}
