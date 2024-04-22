package com.solbeg.userservice.security.jwt;

import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.enums.Status;
import com.solbeg.userservice.enums.error_response.ErrorMessage;
import com.solbeg.userservice.exception.UserStatusException;
import com.solbeg.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage() + email));
        if (user.getStatus() != Status.ACTIVE) {
            throw new UserStatusException(ErrorMessage.USER_NOT_ACTIVE.getMessage());
        }
        JwtUser jwtUser = JwtUserFactory.create(user);
        log.info("IN loadUserByUserName - user with email: {} successfully loaded.", email);
        return jwtUser;
    }
}