package com.ayush.estore.AyushStore.services;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ayush.estore.AyushStore.dtos.PageableResponse;
import com.ayush.estore.AyushStore.dtos.UserDtos;

@Service
public interface UserService {
    // create
    UserDtos createUser(UserDtos userDto);

    // update
    UserDtos updateUser(UserDtos userDto, String userId);

    // delete

    void deleteUser(String userId);

    // get all users
    // PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String
    // sortBy, String sortDir);
    PageableResponse<UserDtos> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    // get single user by id
    UserDtos getUserById(String userId);

    // get single user by email
    UserDtos getUserByEmail(String email);

    // search user
    List<UserDtos> searchUser(String keyword);
}
