package com.mycompany.userservice.mapper;

import com.mycompany.userservice.model.User;
import com.mycompany.userservice.rest.dto.CreateUserRequest;
import com.mycompany.userservice.rest.dto.UpdateUserRequest;
import com.mycompany.userservice.rest.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import(UserMapperImpl.class)
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testToUser() {
        CreateUserRequest createUserRequest = new CreateUserRequest("email@test", "fullName", true);

        User user = userMapper.toUser(createUserRequest);

        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo(createUserRequest.getEmail());
        assertThat(user.getFullName()).isEqualTo(createUserRequest.getFullName());
        assertThat(user.getActive()).isEqualTo(createUserRequest.getActive());
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getUpdatedAt()).isNull();
    }

    @Test
    void testToUserResponse() {
        User user = new User("email@test", "fullName", true);

        UserResponse userResponse = userMapper.toUserResponse(user);

        assertThat(userResponse.getId()).isNull();
        assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(userResponse.getFullName()).isEqualTo(user.getFullName());
        assertThat(userResponse.getActive()).isEqualTo(user.getActive());
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getUpdatedAt()).isNull();
    }

    @ParameterizedTest
    @MethodSource("provideUpdateUserFromRequest")
    void testUpdateUserFromRequest(String newEmail, String newFullName, Boolean newActive, User expectedUser) {
        User user = new User("email@test", "fullName", true);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail(newEmail);
        updateUserRequest.setFullName(newFullName);
        updateUserRequest.setActive(newActive);

        userMapper.updateUserFromRequest(updateUserRequest, user);
        assertThat(user).isEqualTo(expectedUser);
    }

    private static Stream<Arguments> provideUpdateUserFromRequest() {
        return Stream.of(
                Arguments.of("email2@test.com", "fullName2", false, new User("email2@test.com", "fullName2", false)),
                Arguments.of("email2@test.com", null, null, new User("email2@test.com", "fullName", true)),
                Arguments.of(null, "fullName2", null, new User("email@test", "fullName2", true)),
                Arguments.of(null, null, false, new User("email@test", "fullName", false)),
                Arguments.of(null, null, null, new User("email@test", "fullName", true))
        );
    }
}