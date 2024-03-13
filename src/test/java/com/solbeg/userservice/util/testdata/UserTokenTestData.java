package com.solbeg.userservice.util.testdata;

import com.solbeg.userservice.entity.UserToken;
import com.solbeg.userservice.enums.TokenType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.solbeg.userservice.util.initdata.InitData.CREATED_BY_USERTOKEN;
import static com.solbeg.userservice.util.initdata.InitData.EXPIRATION_AT_USERTOKEN;
import static com.solbeg.userservice.util.initdata.InitData.ID_USERTOKEN;
import static com.solbeg.userservice.util.initdata.InitData.TOKEN_USERTOKEN;

@Data
@Builder(setterPrefix = "with")
public class UserTokenTestData {

    @Builder.Default
    private UUID id = ID_USERTOKEN;

    @Builder.Default
    private UUID createdBy = CREATED_BY_USERTOKEN;

    @Builder.Default
    private LocalDateTime expirationAt = EXPIRATION_AT_USERTOKEN;

    @Builder.Default
    private String token = TOKEN_USERTOKEN;

    @Builder.Default
    private TokenType tokenType = TokenType.ACTIVATION;

    public UserToken getUserToken() {
        return UserToken.builder()
                .id(id)
                .createdBy(createdBy)
                .expirationAt(expirationAt)
                .token(token)
                .tokenType(tokenType)
                .build();
    }
}