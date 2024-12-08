package com.ayush.estore.AyushStore.services.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ayush.estore.AyushStore.Repository.RoleRepo;
import com.ayush.estore.AyushStore.Repository.UserRepo;
import com.ayush.estore.AyushStore.config.AppConstants;
import com.ayush.estore.AyushStore.dtos.PageableResponse;
import com.ayush.estore.AyushStore.dtos.UserDtos;
import com.ayush.estore.AyushStore.entities.Role;
import com.ayush.estore.AyushStore.entities.User;
import com.ayush.estore.AyushStore.exceptions.ResourceNotFoundException;
import com.ayush.estore.AyushStore.helper.Helper;
import com.ayush.estore.AyushStore.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${user.profile.image.path}")
    private String imagePath;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public UserDtos createUser(UserDtos userDto) {
        // TODO Auto-generated method stub

        logger.warn("PROVIDERRR AYUSH {}", userDto.getProvider());
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);

        User user = dtoToEntity(userDto);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = new Role();
        role.setRoleId(UUID.randomUUID().toString());
        role.setName("ROLE_" + AppConstants.ROLE_NORMAL);

        Role roleNormal = roleRepo.findByName("ROLE_" + AppConstants.ROLE_NORMAL).orElse(role);

        user.setRoles(List.of(roleNormal));

        user.setProvider(userDto.getProvider());

        User savedUser = userRepo.save(user);

        UserDtos newDto = entitytoDto(savedUser);
        return newDto;
    }

    private UserDtos entitytoDto(User savedUser) {
        // TODO Auto-generated method stub
        // UserDtos userDto = UserDtos.builder()
        // .userId(savedUser.getUserId())
        // .name(savedUser.getName())
        // .email(savedUser.getEmail())
        // .password(savedUser.getPassword())
        // .about(savedUser.getAbout())
        // .gender(savedUser.getGender())
        // .imageName(savedUser.getImageName())
        // .build();
        // return userDto;
        return modelMapper.map(savedUser, UserDtos.class);

    }

    private User dtoToEntity(UserDtos userDto) {
        // TODO Auto-generated method stub
        // User user = User.builder().userId(userDto.getUserId())
        // .name(userDto.getName())
        // .email(userDto.getEmail())
        // .password(userDto.getPassword())
        // .about(userDto.getAbout())
        // .gender(userDto.getGender())
        // .imageName(userDto.getImageName()).build();
        // return user;
        return modelMapper.map(userDto, User.class);
    }

    @Override
    public void deleteUser(String userId) {
        // TODO Auto-generated method stub
        try {
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!"));
            userRepo.delete(user);

            String fullPath = imagePath + user.getImageName();

            Path path = Paths.get(fullPath);
            Files.delete(path);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.info("Exception AALA RE IN Delete {} ", e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public PageableResponse<UserDtos> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        // TODO Auto-generated method stub
        // page no default starts from 0
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy)).descending() : (Sort.by(sortBy));
        org.springframework.data.domain.Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> userspage = userRepo.findAll(pageable);

        PageableResponse<UserDtos> response = Helper.getPageableResponse(userspage, UserDtos.class);

        // List<User> content = userspage.getContent();

        // List<UserDtos> dtoList = content.stream().map(user ->
        // entitytoDto(user)).collect(Collectors.toList());
        // PageableResponse<UserDtos> response = new PageableResponse<>();
        // response.setContent(dtoList);
        // response.setPageNumber(userspage.getNumber());
        // response.setPageSize(userspage.getSize());
        // response.setTotalElements(userspage.getTotalElements());
        // response.setTotalPages(userspage.getTotalPages());
        // response.setLastPage(userspage.isLast());

        return response;
    }

    @Override
    public UserDtos getUserByEmail(String email) {
        // TODO Auto-generated method stub
        User user = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No Email"));
        return entitytoDto(user);
    }

    @Override
    public UserDtos getUserById(String userId) {
        // TODO Auto-generated method stub
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Exception"));
        return entitytoDto(user);
    }

    @Override
    public List<UserDtos> searchUser(String keyword) {
        // TODO Auto-generated method stub
        List<User> users = userRepo.findByNameContaining(keyword);
        List<UserDtos> dtoList = users.stream().map(user -> entitytoDto(user)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public UserDtos updateUser(UserDtos userDto, String userId) {
        // TODO Auto-generated method stub

        User user;
        try {
            user = userRepo.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!"));
            user.setName(userDto.getName());
            // email update
            user.setAbout(userDto.getAbout());
            user.setGender(userDto.getGender());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setImageName(userDto.getImageName());
            // assign normal role to user
            // jo bhi api se --> nrml user
            Role role = new Role();
            role.setRoleId(UUID.randomUUID().toString());
            Role roleNormal = roleRepo.findByName("ROLE_NORMAL").orElseThrow(null);
            user.setRoles(List.of(roleNormal));
            User updatedUser = userRepo.save(user);
            UserDtos updatedDto = entitytoDto(updatedUser);
            return updatedDto;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // assign normal role to user
        // by detail jo bhi api se user banega usko ham log normal user banayenge

        // save data
        // User updatedUser = userRepo.save(user);
        // UserDtos updatedDto = entitytoDto(updatedUser);
        // return updatedDto;
        return null;
    }

}
