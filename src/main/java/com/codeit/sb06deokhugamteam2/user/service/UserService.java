package com.codeit.sb06deokhugamteam2.user.service;

import com.codeit.sb06deokhugamteam2.common.exception.ErrorCode;
import com.codeit.sb06deokhugamteam2.common.exception.exceptions.BasicException;
import com.codeit.sb06deokhugamteam2.user.dto.UserDto;
import com.codeit.sb06deokhugamteam2.user.dto.UserLoginRequest;
import com.codeit.sb06deokhugamteam2.user.dto.UserRegisterRequest;
import com.codeit.sb06deokhugamteam2.user.entity.User;
import com.codeit.sb06deokhugamteam2.user.mapper.UserMapper;
import com.codeit.sb06deokhugamteam2.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Transactional
    public UserDto register(UserRegisterRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new BasicException(ErrorCode.DUPLICATE_EMAIL, Collections.emptyMap(),
                    HttpStatus.CONFLICT);
        }

        User user = userMapper.toEntity(request);

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserDto login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BasicException(ErrorCode.INVALID_USER_DATA,
                        Collections.emptyMap(), HttpStatus.BAD_REQUEST));

        if (user.isDeleted()) {
            throw new BasicException(ErrorCode.INVALID_USER_DATA,
                    Collections.emptyMap(), HttpStatus.BAD_REQUEST);
        }

        if (!request.password().equals(user.getPassword())) {
            throw new BasicException(ErrorCode.INVALID_USER_PASSWORD,
                    Collections.emptyMap(), HttpStatus.BAD_REQUEST);
        }

        return userMapper.toDto(user);
    }

    public UserDto getUserInfo(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BasicException(ErrorCode.USER_NOT_FOUND,
                        Collections.emptyMap(), HttpStatus.NOT_FOUND));

        return userMapper.toDto(user);
    }
}