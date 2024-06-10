package com.solbeg.userservice.util.testdata;

import com.solbeg.userservice.dto.response.UserTokenResponse;
import com.solbeg.userservice.entity.UserToken;
import com.solbeg.userservice.enums.TokenType;

import static com.solbeg.userservice.util.initdata.InitData.ACTUAL_AT_USERTOKEN;
import static com.solbeg.userservice.util.initdata.InitData.EXPIRED_AT_USERTOKEN;
import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.ID_USERTOKEN;
import static com.solbeg.userservice.util.initdata.InitData.TOKEN_USERTOKEN;


public class UserTokenTestData {

    public static UserToken getUserToken() {
        return UserToken.builder()
                .id(ID_USERTOKEN)
                .expirationAt(ACTUAL_AT_USERTOKEN)
                .token(TOKEN_USERTOKEN)
                .tokenType(TokenType.ACTIVATION)
                .user(UserTestData.getJournalist())
                .build();
    }

    public static UserToken getExpiredUserToken() {
        return UserToken.builder()
                .id(ID_USERTOKEN)
                .expirationAt(EXPIRED_AT_USERTOKEN)
                .token(TOKEN_USERTOKEN)
                .tokenType(TokenType.ACTIVATION)
                .user(UserTestData.getJournalist())
                .build();
    }

    public static UserTokenResponse getUserTokenResponse() {
        return UserTokenResponse.builder()
                .id(ID_USERTOKEN)
                .expirationAt(ACTUAL_AT_USERTOKEN)
                .token(TOKEN_USERTOKEN)
                .tokenType(TokenType.ACTIVATION)
                .userId(ID_JOURNALIST)
                .build();
    }
}