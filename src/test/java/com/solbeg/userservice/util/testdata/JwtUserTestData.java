package com.solbeg.userservice.util.testdata;

import com.solbeg.userservice.security.jwt.JwtUser;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static com.solbeg.userservice.util.initdata.InitData.*;

@Data
@Builder(setterPrefix = "with")
public class JwtUserTestData {

    @Builder.Default
    private UUID id = ID_JOURNALIST;

    @Builder.Default
    private String firstName = FIRST_NAME_JOURNALIST;

    @Builder.Default
    private String lastName = LAST_NAME_JOURNALIST;

    @Builder.Default
    private String password = PASSWORD_JOURNALIST;

    @Builder.Default
    private String email = EMAIL_JOURNALIST;

    @Builder.Default
    private boolean enabled = false;

    @Builder.Default
    private Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(NAME_ROLE_JOURNALIST));

    public JwtUser getJwtUser() {
        return JwtUser.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .email(email)
                .enabled(enabled)
                .authorities(authorities)
                .build();
    }
}
