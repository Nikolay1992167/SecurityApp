package com.solbeg.userservice.security.jwt;

import com.solbeg.userservice.entity.Role;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.enums.Status;
import com.solbeg.userservice.util.testdata.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;

import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.ROLE_NAME_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.ROLE_NAME_SUBSCRIBER;
import static org.assertj.core.api.Assertions.assertThat;

class JwtUserFactoryTest {

    @Test
    void shouldCreateJwtUserFromUser() {
        // given
        User user = UserTestData.builder()
                .build()
                .getUser();
        Role role1 = new Role();
        role1.setName(ROLE_NAME_SUBSCRIBER);
        Role role2 = new Role();
        role2.setName(ROLE_NAME_JOURNALIST);
        user.setRoles(Arrays.asList(role1, role2));

        // when
        JwtUser jwtUser = JwtUserFactory.create(user);
        List<GrantedAuthority> expectedAuthorities = Arrays.asList(
                new SimpleGrantedAuthority(role1.getName()),
                new SimpleGrantedAuthority(role2.getName())
        );

        // then
        assertThat(jwtUser)
                .hasFieldOrPropertyWithValue("id", ID_JOURNALIST)
                .hasFieldOrPropertyWithValue("username", user.getEmail())
                .hasFieldOrPropertyWithValue("password", user.getPassword())
                .hasFieldOrPropertyWithValue("enabled", user.getStatus().equals(Status.ACTIVE))
                .hasFieldOrPropertyWithValue("authorities", expectedAuthorities);
    }
}