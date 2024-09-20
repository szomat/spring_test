package net.javaguides.springboot.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.springboot.dto.UserDto;
import net.javaguides.springboot.entity.User;
import net.javaguides.springboot.exception.EmailAlreadyExistsException;
import net.javaguides.springboot.exception.UserNotFoundException;
import net.javaguides.springboot.mapper.AutoUserMapper;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;


    private ModelMapper modelMapper;

//    @Override
//    public UserDto createUser(UserDto userDto) {
////        User user = UserMapper.mapToUser(userDto);
//        User user = modelMapper.map(userDto, User.class);
//        User savedUser = userRepository.save(user);
//        return modelMapper.map(savedUser, UserDto.class);
//    }

    @Override
    public UserDto createUser(UserDto userDto) {
//        User user = UserMapper.mapToUser(userDto);
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());

        if(optionalUser.isPresent()){
            throw new EmailAlreadyExistsException("Email already exists for user!");
        }
        User user = AutoUserMapper.MAPPER.mapToUser(userDto);
        User savedUser = userRepository.save(user);
        return AutoUserMapper.MAPPER.mapToUserDto(savedUser);
    }

//    @Override
//    public UserDto getUserById(Long userId) {
//        return modelMapper.map(userRepository.findById(userId).get(), UserDto.class);
//    }


    public UserDto getUserById(Long userId) {
        return AutoUserMapper.MAPPER.mapToUserDto(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User", "id", userId)));
    }

//    @Override
//    public List<UserDto> getAllUser() {
//        List<User> userList = userRepository.findAll();
////        return userList.stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
//        return userList.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
//    }

    @Override
    public List<UserDto> getAllUser() {
        List<User> userList = userRepository.findAll();
//        return userList.stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
        return userList.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    //    @Override
//    public UserDto updateUser(UserDto user) {
//        User existingUser = userRepository.findById(user.getId()).get();
//        existingUser.setFirstName(user.getFirstName());
//        existingUser.setLastName(user.getLastName());
//        existingUser.setEmail(user.getEmail());
//        return modelMapper.map(userRepository.save(existingUser), UserDto.class);
//    }

    @Override
    public UserDto updateUser(UserDto user) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("User", "id", user.getId()));
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        return AutoUserMapper.MAPPER.mapToUserDto(userRepository.save(existingUser));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User", "id", userId));
        userRepository.deleteById(userId);
    }
}
