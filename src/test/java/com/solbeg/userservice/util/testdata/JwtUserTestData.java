package com.solbeg.userservice.util.testdata;

import com.solbeg.userservice.security.jwt.JwtUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.FIRST_NAME_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.LAST_NAME_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.PASSWORD_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.ROLE_NAME_JOURNALIST;

public class JwtUserTestData {

    public static JwtUser getJwtUser() {
        return JwtUser.builder()
                .id(ID_JOURNALIST)
                .firstName(FIRST_NAME_JOURNALIST)
                .lastName(LAST_NAME_JOURNALIST)
                .password(PASSWORD_JOURNALIST)
                .email(EMAIL_JOURNALIST)
                .enabled(false)
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(ROLE_NAME_JOURNALIST)))
                .build();
    }
}
