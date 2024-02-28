package com.solbeg.userservice.controller.openapi;

import com.solbeg.userservice.dto.request.JwtRequest;
import com.solbeg.userservice.dto.request.RefreshTokenRequest;
import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.response.JwtResponse;
import com.solbeg.userservice.dto.response.UserRegisterResponse;
import com.solbeg.userservice.exception.model.IncorrectData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Authentication", description = "The Authentication Api")
public interface AuthOpenApi {

    @Operation(
            method = "POST",
            tags = "Authentication",
            description = "Authentication in app.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JwtRequest.class),
                            examples = @ExampleObject("""
                                    {
                                        "email": "ivan@google.com",
                                        "password": "123456789"
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponse.class),
                                    examples = @ExampleObject("""
                                            {
                                                "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                                                "email": "ivan@google.com",
                                                "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuQGdvb2dsZS5jb20iLCJpZCI6ImEwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMSIsInJvbGVzIjpbIkFETUlOIl0sImV4cCI6MTcwOTA3NTcyOX0.qBkGro_Bz-hLMniW__5OzVuGjgqfdNfPQ8_YHGHuIUQ4jfjnLttC5T6u-QsovcN2ViIuJ2PtHYrrm-mZOlZVPg",
                                                "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuQGdvb2dsZS5jb20iLCJpZCI6ImEwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMSIsImV4cCI6MTcxMTY2NDEyOX0.IycHdxvvWqEMsPU5LXXzENgiSG5hDrLuexBrMAKGs-mPwoyOYz6-rcCpELMd6jGukBxGi33Id_KT7bfQGe1h7w"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(responseCode = "409", description = "The endpoint has not been completed when an invalid email is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "MethodArgumentNotValidException",
                                                "error_message": "{email=должно иметь формат адреса электронной почты}",
                                                "error_code": "409 CONFLICT"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User because the user with this email does not exist.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "NoSuchUserEmailException",
                                                "error_message": "User with email sergey@google.com is not exist or not active.",
                                                "error_code": "401 UNAUTHORIZED"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User because the user with status not active.",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                            {
                                                Unauthorized.
                                            }
                                    """)))
            }
    )
    ResponseEntity<JwtResponse> authenticate(@Parameter(hidden = true) JwtRequest loginRequest);

    @Operation(
            method = "POST",
            tags = "Authentication",
            description = "Registration journalist",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRegisterRequest.class),
                            examples = @ExampleObject("""
                                    {
                                        "firstName": "Pasha",
                                        "lastName": "Grom",
                                        "email": "grom@grom.com",
                                        "password": "63524178",
                                        "status": "ACTIVE"
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserRegisterResponse.class),
                                    examples = @ExampleObject("""
                                            {
                                                "id": "41d96d57-21c6-4e8e-87b4-f43cc1db778e",
                                                "firstName": "Pasha",
                                                "lastName": "Grom",
                                                "password": "$2a$10$3YUCNK8WXGb3z3JL16nwPuN.zH2ZnRoUbcv1xZNOHpe6UPxyPGPqO",
                                                "email": "grom@grom.com",
                                                "roles": [
                                                    "JOURNALIST"
                                                ],
                                                "status": "ACTIVE"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(responseCode = "406", description = "The endpoint has not been completed because an email is exist in DB.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "UniqueEmailException",
                                                "error_message": "Email grom@grom.com is occupied! Another user is already registered by this email!",
                                                "error_code": "406 NOT_ACCEPTABLE"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "409", description = "The endpoint has not been completed because an status is wrong.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "HttpMessageNotReadableException",
                                                "error_message": "Specify the correct status!",
                                                "error_code": "409 CONFLICT"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "409", description = "The endpoint has not been completed because the request arguments are not validated.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "MethodArgumentNotValidException",
                                                "error_message": "{firstName=размер должен находиться в диапазоне от 2 до 40, lastName=размер должен находиться в диапазоне от 2 до 50, email=должно иметь формат адреса электронной почты}",
                                                "error_code": "409 CONFLICT"
                                            }
                                    """)))
            }
    )
    ResponseEntity<UserRegisterResponse> registerJournalist(UserRegisterRequest request);

    @Operation(
            method = "POST",
            tags = "Authentication",
            description = "Registration subscriber",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRegisterRequest.class),
                            examples = @ExampleObject("""
                                    {
                                        "firstName": "Olga",
                                        "lastName": "Popova",
                                        "email": "popova@email.com",
                                        "password": "9652314",
                                        "status": "ACTIVE"
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserRegisterResponse.class),
                                    examples = @ExampleObject("""
                                            {
                                                "id": "33df42aa-223b-47cc-b31b-e8683e2dfa5d",
                                                "firstName": "Olga",
                                                "lastName": "Popova",
                                                "password": "$2a$10$koIMR/n4Ro9UdE1eZ7Xo8O.7DsmUXwDyPnnqwYWuOrBUITR8Nv2vS",
                                                "email": "popova@email.com",
                                                "roles": [
                                                    "SUBSCRIBER"
                                                ],
                                                "status": "ACTIVE"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(responseCode = "406", description = "The endpoint has not been completed because an email is exist in DB.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "UniqueEmailException",
                                                "error_message": "Email dron@mail.ru is occupied! Another user is already registered by this email!",
                                                "error_code": "406 NOT_ACCEPTABLE"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "409", description = "The endpoint has not been completed because an status is wrong.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "HttpMessageNotReadableException",
                                                "error_message": "Specify the correct status!",
                                                "error_code": "409 CONFLICT"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "409", description = "The endpoint has not been completed because the request arguments are not validated.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "MethodArgumentNotValidException",
                                                "error_message": "{firstName=размер должен находиться в диапазоне от 2 до 40, lastName=размер должен находиться в диапазоне от 2 до 50, email=должно иметь формат адреса электронной почты}",
                                                "error_code": "409 CONFLICT"
                                            }
                                    """)))
            }
    )
    ResponseEntity<UserRegisterResponse> registerSubscriber(UserRegisterRequest request);

    @Operation(
            method = "POST",
            tags = "Authentication",
            description = "Updated token",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenRequest.class),
                            examples = @ExampleObject("""
                                    {
                                        "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuQGdvb2dsZS5jb20iLCJpZCI6ImEwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMSIsInJvbGVzIjpbIkFETUlOIl0sImV4cCI6MTcwOTA3ODQ2Nn0.6iwpfXY2eZDZCmxLrcXL1rdEs1ZYoKWTFGHl4spzabAT5hRuZ1pBZDHueydtxj-UeW_r4fsMDB386JUsKulsGQ"
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponse.class),
                                    examples = @ExampleObject("""
                                            {
                                                "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                                                "email": "ivan@google.com",
                                                "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuQGdvb2dsZS5jb20iLCJpZCI6ImEwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMSIsInJvbGVzIjpbIkFETUlOIl0sImV4cCI6MTcwOTA3ODQ4Nn0.dAagsoFXl3GyZUeHn-BUWtSakIkLv5JEJNDycuwSW_iNfW04f5eroXVJtWRX8yCC6gbgzqcPi-BupOyTu8dXdQ",
                                                "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuQGdvb2dsZS5jb20iLCJpZCI6ImEwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMSIsImV4cCI6MTcxMTY2Njg4Nn0.W5FbSle_jHpATJ6SOrWflCdueJSNk9IaT8v1SUSmXN-4HTmL-gmYi_xJGyW7LgCLRumMR4Ad0TuRHldI_p1-uQ"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when the token is not active.",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                    Unauthorized.
                                    """))),
                    @ApiResponse(responseCode = "409", description = "The endpoint has not been completed because the token is not valid.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "MethodArgumentNotValidException",
                                                "error_message": "{refreshToken=должно соответствовать \\"^[a-zA-Z0-9_-]+\\\\.[a-zA-Z0-9_-]+\\\\.[a-zA-Z0-9_-]+$\\"}",
                                                "error_code": "409 CONFLICT"
                                            }
                                    """)))
            }
    )
    ResponseEntity<JwtResponse> refresh(RefreshTokenRequest refreshTokenRequest);
}