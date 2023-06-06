package com.wcd.userservice.service.user;

import com.wcd.userservice.file.FileStore;
import com.wcd.userservice.client.ClubServiceClient;
import com.wcd.userservice.dto.user.response.ResponseUserById;
import com.wcd.userservice.entity.Users;
import com.wcd.userservice.repository.UserRepository;
import com.wcd.userservice.dto.user.request.RequestUpdateUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final ClubServiceClient clubServiceClient;
    private final FileStore fileStore;


    @Override
    public ResponseUserById getUserById(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseUserById.builder().user(user).build();
    }

    @Transactional
    @Override
    public Long updateUserById(Long userId, RequestUpdateUser requestUpdateUser) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String profileImageUrl = null;

        try {
            profileImageUrl = fileStore.storeFile(requestUpdateUser.getProfileImage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        user.updateUser(requestUpdateUser, profileImageUrl);

        return userId;
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        userRepository.delete(user);
        clubServiceClient.deleteMember(userId);
    }
}