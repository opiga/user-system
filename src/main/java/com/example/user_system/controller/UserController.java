package com.example.user_system.controller;

import com.example.user_system.dto.response.PageResponse;
import com.example.user_system.dto.request.UserRequestDto;
import com.example.user_system.dto.response.UserResponseDto;
import com.example.user_system.dto.request.UserSearchRequest;
import com.example.user_system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Validated UserRequestDto request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PostMapping("/search")
    public ResponseEntity<PageResponse<UserResponseDto>> getUsers(@Valid @RequestBody UserSearchRequest request) {
        PageResponse<UserResponseDto> response = userService.getUsers(request);
        return response == null || response.getUsers().isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserRequestDto updatedUser) {
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }

}