package com.solbeg.userservice.controller.openapi;

import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.exception.model.IncorrectData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "User", description = "The User Api")
public interface UserOpenApi {

    @Operation(
            method = "GET",
            tags = "User",
            description = "Get page of users",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class),
                                    examples = @ExampleObject("""
                                            {
                                                    "content": [
                                                        {
                                                            "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                                                            "createdBy": null,
                                                            "updatedBy": null,
                                                            "createdAt": "2024-02-15T12:00:00",
                                                            "updatedAt": "2024-02-19T12:00:00",
                                                            "firstName": "Ivan",
                                                            "lastName": "Sidorov",
                                                            "password": "$2a$10$ch99apPuJoORMIf8Ew.D9e.cgWa1C6EYQ3iQMp7idTlGyNpyoF.P.",
                                                            "email": "ivan@google.com",
                                                            "roles": [
                                                                "ADMIN"
                                                            ],
                                                            "status": "ACTIVE"
                                                        },
                                                        {
                                                            "id": "b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12",
                                                            "createdBy": null,
                                                            "updatedBy": null,
                                                            "createdAt": "2024-02-13T12:00:00",
                                                            "updatedAt": "2024-02-16T12:00:00",
                                                            "firstName": "Egor",
                                                            "lastName": "Strelin",
                                                            "password": "$2a$10$uuwsQHbWZMIUMTuxKwij8e/l5zea9.Q2XW0eG3Bs/2fUMarbqiymG",
                                                            "email": "strelin@mail.ru",
                                                            "roles": [
                                                                "JOURNALIST"
                                                            ],
                                                            "status": "ACTIVE"
                                                        },
                                                        {
                                                            "id": "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13",
                                                            "createdBy": null,
                                                            "updatedBy": null,
                                                            "createdAt": "2024-02-11T12:00:00",
                                                            "updatedAt": "2024-02-14T12:00:00",
                                                            "firstName": "Alex",
                                                            "lastName": "Volk",
                                                            "password": "$2a$10$wH5b5g3QibOOdDhOVlSGxuyvqOO4kDcWMI3TQKNK9HdzUeQLowmNG",
                                                            "email": "volk@google.com",
                                                            "roles": [
                                                                "SUBSCRIBER"
                                                            ],
                                                            "status": "ACTIVE"
                                                        }
                                                    ],
                                                    "pageable": {
                                                        "pageNumber": 0,
                                                        "pageSize": 15,
                                                        "sort": [],
                                                        "offset": 0,
                                                        "unpaged": false,
                                                        "paged": true
                                                    },
                                                    "last": true,
                                                    "totalPages": 1,
                                                    "totalElements": 3,
                                                    "size": 15,
                                                    "number": 0,
                                                    "sort": [],
                                                    "first": true,
                                                    "numberOfElements": 3,
                                                    "empty": false
                                                }
                                            """)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when the token is not entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-03-03T20:15:31.7881304",
                                        "error_message": "Full authentication is required to access this resource",
                                        "error_status": 401
                                    }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an expired token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:07:49.848746",
                                                "error_message": "JWT expired at 2024-02-25T17:00:46Z. Current time: 2024-03-03T18:07:49Z, a difference of 608823848 milliseconds.  Allowed clock skew: 0 milliseconds.",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an invalid token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:08:13.1738829",
                                                "error_message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "403", description = "Not Authenticated User if a token with a non-admin role is entered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T20:38:59.6169887",
                                                "error_message": "Access Denied",
                                                "error_status": 403
                                            }
                                    """)))
            }
    )
    ResponseEntity<Page<UserResponse>> findAll(Pageable pageable);

    @Operation(
            method = "GET",
            tags = "User",
            description = "Get a User by uuid",
            parameters = {
                    @Parameter(name = "uuid", description = "Id of User", example = "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class),
                                    examples = @ExampleObject("""
                                            {
                                                "id": "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13",
                                                "createdBy": null,
                                                "updatedBy": null,
                                                "createdAt": "2024-02-11T12:00:00",
                                                "updatedAt": "2024-02-14T12:00:00",
                                                "firstName": "Alex",
                                                "lastName": "Volk",
                                                "password": "$2a$10$wH5b5g3QibOOdDhOVlSGxuyvqOO4kDcWMI3TQKNK9HdzUeQLowmNG",
                                                "email": "volk@google.com",
                                                "roles": [
                                                    "SUBSCRIBER"
                                                ],
                                                "status": "ACTIVE"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when the token is not entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-03-03T20:41:58.7865553",
                                        "error_message": "Full authentication is required to access this resource",
                                        "error_status": 401
                                    }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an expired token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:08:47.8864875",
                                                "error_message": "JWT expired at 2024-02-25T17:00:46Z. Current time: 2024-03-03T18:08:47Z, a difference of 608881885 milliseconds.  Allowed clock skew: 0 milliseconds.",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an invalid token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:09:04.1926719",
                                                "error_message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "403", description = "The endpoint has not been completed if a token with a non-admin role is entered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T20:45:26.1894423",
                                                "error_message": "Access Denied",
                                                "error_status": 403
                                            }
                                    """))),
                    @ApiResponse(responseCode = "404", description = "The user not found when entered not existent uuid.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:11:33.2154937",
                                                "error_message": "User with c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15 not found!",
                                                "error_status": 404
                                            }
                                    """))),
            }
    )
    ResponseEntity<UserResponse> findById(UUID uuid);

    @Operation(
            method = "PUT",
            tags = "User",
            description = "Update an user with uuid",
            parameters = {
                    @Parameter(name = "uuid", description = "Uuid of user", example = "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserUpdateRequest.class),
                            examples = @ExampleObject("""
                                    {
                                        "firstName": "Nikolay",
                                        "lastName": "Minich",
                                        "password": "8776868",
                                        "email": "nikolayv@example.com"
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class),
                                    examples = @ExampleObject("""
                                            {
                                                "id": "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13",
                                                "createdBy": null,
                                                "updatedBy": null,
                                                "createdAt": "2024-02-11T12:00:00",
                                                "updatedAt": "2024-02-14T12:00:00",
                                                "firstName": "Nikolay",
                                                "lastName": "Minich",
                                                "password": "$2a$10$4NHxBR02aHWxiHt5CrGOmuLhTqu.0SQYU16F/OQXus5ZQ2.KDvCPq",
                                                "email": "nikolayv@example.com",
                                                "roles": [
                                                    "SUBSCRIBER"
                                                ],
                                                "status": "ACTIVE"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "The endpoint has not been completed when an invalid uuid is entered.",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:04:33.3089845",
                                                "error_message": "UUID was entered incorrectly!",
                                                "error_status": 400
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when the token is not entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class),  examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-03-03T21:05:16.3651964",
                                        "error_message": "Full authentication is required to access this resource",
                                        "error_status": 401
                                    }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an expired token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:10:03.6470458",
                                                "error_message": "JWT expired at 2024-02-25T17:00:46Z. Current time: 2024-03-03T18:10:03Z, a difference of 608957647 milliseconds.  Allowed clock skew: 0 milliseconds.",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an invalid token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:10:25.6642296",
                                                "error_message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "404", description = "The user not found when entered not existent uuid.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:13:39.1105685",
                                                "error_message": "User with c0eebc99-9c0b-4ef8-bb6d-00000006bb15 not found!",
                                                "error_status": 404
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "The endpoint has not been completed when entered not existent uuid.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:14:31.5823614",
                                                "error_message": "{lastName=размер должен находиться в диапазоне от 2 до 50, firstName=размер должен находиться в диапазоне от 2 до 40, email=должно иметь формат адреса электронной почты}",
                                                "error_status": 400
                                            }
                                    """))),
                    @ApiResponse(responseCode = "406", description = "The endpoint has not been completed when the email of UserUpdateRequest is exist in BD.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:15:04.0445334",
                                                "error_message": "Email is occupied! Another user is already registered by this nikolayv@example.com",
                                                "error_status": 406
                                            }
                                    """)))
            }
    )
    ResponseEntity<UserResponse> update(UUID uuid, @Parameter(hidden = true) UserUpdateRequest updateRequest);

    @Operation(
            method = "PATCH",
            tags = "User",
            description = "Changing the user's status to NOT_ACTIVE",
            parameters = {
                    @Parameter(name = "uuid", description = "Uuid of user", example = "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "The endpoint has been completed."),
                    @ApiResponse(responseCode = "404", description = "The user not found when entered not existent uuid.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:16:40.4767385",
                                                "error_message": "User with c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a18 not found!",
                                                "error_status": 404
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "The endpoint has not been completed when an invalid uuid is entered.",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:17:09.5058146",
                                                "error_message": "UUID was entered incorrectly!",
                                                "error_status": 400
                                            }
                                    """))),
                    @ApiResponse(responseCode = "409", description = "You cannot change the status of a user with the role ADMIN.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:17:42.2449021",
                                                "error_message": "You cannot change the status of a user with the ADMIN role.",
                                                "error_status": 409
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when the token is not entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-03-03T21:18:03.3175993",
                                        "error_message": "Full authentication is required to access this resource",
                                        "error_status": 401
                                    }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an expired token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:18:36.2612456",
                                                "error_message": "JWT expired at 2024-02-25T17:00:46Z. Current time: 2024-03-03T18:18:36Z, a difference of 609470261 milliseconds.  Allowed clock skew: 0 milliseconds.",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an invalid token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:18:52.4644725",
                                                "error_message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                "error_status": 401
                                            }
                                    """)))
            }
    )
    ResponseEntity<?> deactivateUser(UUID id, @Parameter(hidden = true) String token);

    @Operation(
            method = "PATCH",
            tags = "User",
            description = "Changing the user's status to DELETED",
            parameters = {
                    @Parameter(name = "uuid", description = "Uuid of user", example = "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "The endpoint has been completed."),
                    @ApiResponse(responseCode = "404", description = "The user not found when entered not existent uuid.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:19:45.6832412",
                                                "error_message": "User with a0eebc99-9c0b-4ef8-bb6d-6bb9bd380154 not found!",
                                                "error_status": 404
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "The endpoint has not been completed when an invalid uuid is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:20:10.2958205",
                                                "error_message": "UUID was entered incorrectly!",
                                                "error_status": 400
                                            }
                                    """))),
                    @ApiResponse(responseCode = "409", description = "You cannot change the status of a user with the role ADMIN.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:20:43.5178811",
                                                "error_message": "You cannot change the status of a user with the ADMIN role.",
                                                "error_status": 409
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when the token is not entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-03-03T21:21:01.5391987",
                                        "error_message": "Full authentication is required to access this resource",
                                        "error_status": 401
                                    }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an expired token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:21:30.5794483",
                                                "error_message": "JWT expired at 2024-02-25T17:00:46Z. Current time: 2024-03-03T18:21:30Z, a difference of 609644579 milliseconds.  Allowed clock skew: 0 milliseconds.",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an invalid token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:21:45.4513425",
                                                "error_message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                "error_status": 401
                                            }
                                    """)))
            }
    )
    ResponseEntity<?> deleteUser(UUID id, @Parameter(hidden = true) String token);
}