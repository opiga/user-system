package com.example.user_system.service;

import com.example.user_system.dto.response.PageResponse;
import com.example.user_system.dto.request.UserFilterDto;
import com.example.user_system.dto.request.UserRequestDto;
import com.example.user_system.dto.response.UserResponseDto;
import com.example.user_system.dto.request.UserSearchRequest;
import com.example.user_system.entity.User;
import com.example.user_system.repository.UserRepository;
import com.example.user_system.specification.UserSpecifications;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto createUser(UserRequestDto request) {

        userRepository.findByEmail(request.getEmail()).ifPresent(existingUser -> {
            throw new IllegalArgumentException("User with this email already exists.");
        });

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .contactInformation(request.getContactInformation())
                .email(request.getEmail())
                .build();

        user = userRepository.save(user);
        return convertToResponse(user);
    }

    public PageResponse<UserResponseDto> getUsers(UserSearchRequest request) {
        int page = Math.max(request.getPage(), 0);
        int size = Math.max(request.getSize(), 1);
        Pageable pageable = PageRequest.of(page, size);

        UserFilterDto filter = Optional.of(request.getFilter()).orElse(new UserFilterDto());

        Specification<User> spec = null;

        if (filter.getFirstName() != null && !filter.getFirstName().isEmpty()) {
            spec = UserSpecifications.firstNameEquals(filter.getFirstName());
        }
        if (filter.getLastName() != null && !filter.getLastName().isEmpty()) {
            Specification<User> lastNameSpec = UserSpecifications.lastNameEquals(filter.getLastName());
            spec = (spec == null) ? lastNameSpec : spec.and(lastNameSpec);
        }

        Page<User> usersPage = (spec == null)
                ? userRepository.findAll(pageable)
                : userRepository.findAll(spec, pageable);

        Page<UserResponseDto> dtoPage = usersPage.map(this::convertToResponse);

        return new PageResponse<>(
                dtoPage.getContent(),
                dtoPage.getNumber(),
                dtoPage.getSize(),
                dtoPage.getTotalElements(),
                dtoPage.getTotalPages()
        );
    }

    private UserResponseDto convertToResponse(User user) {
        return UserResponseDto.builder()
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .contactInformation(user.getContactInformation())
                                .build();
    }

    public UserResponseDto updateUser(Long id, UserRequestDto updatedUser) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setContactInformation(updatedUser.getContactInformation());
        user.setEmail(updatedUser.getEmail());

        return convertToResponse(userRepository.save(user));
    }

}