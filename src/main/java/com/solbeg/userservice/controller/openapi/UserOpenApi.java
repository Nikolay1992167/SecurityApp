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
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                    Unauthorized.
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an expired token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "ExpiredJwtException",
                                                "error_message": "JWT expired at 2024-02-25T17:00:46Z. Current time: 2024-02-27T19:36:34Z, a difference of 182148885 milliseconds.  Allowed clock skew: 0 milliseconds.",
                                                "error_code": "401 UNAUTHORIZED"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an invalid token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "SignatureException",
                                                "error_message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                "error_code": "401 UNAUTHORIZED"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "403", description = "Not Authenticated User if a token with a non-admin role is entered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-02-27T20:32:40.560+00:00",
                                                "status": 403,
                                                "error": "Forbidden",
                                                "path": "/api/v1/users"
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
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                    Unauthorized.
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an expired token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "ExpiredJwtException",
                                                "error_message": "JWT expired at 2024-02-25T17:00:46Z. Current time: 2024-02-27T21:02:24Z, a difference of 187298859 milliseconds.  Allowed clock skew: 0 milliseconds.",
                                                "error_code": "401 UNAUTHORIZED"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an invalid token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "SignatureException",
                                                "error_message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                "error_code": "401 UNAUTHORIZED"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "403", description = "The endpoint has not been completed if a token with a non-admin role is entered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-02-27T21:04:12.254+00:00",
                                                "status": 403,
                                                "error": "Forbidden",
                                                "path": "/api/v1/users/c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13"
                                            }
                                    """)))
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
                                                "timestamp": "2024-02-27T21:12:01.059+00:00",
                                                "status": 400,
                                                "error": "Bad Request",
                                                "path": "/api/v1/users/c"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when the token is not entered.",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                    Unauthorized.
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an expired token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "ExpiredJwtException",
                                                "error_message": "JWT expired at 2024-02-25T17:00:46Z. Current time: 2024-02-27T21:27:34Z, a difference of 188808512 milliseconds.  Allowed clock skew: 0 milliseconds.",
                                                "error_code": "401 UNAUTHORIZED"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an invalid token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "SignatureException",
                                                "error_message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                "error_code": "401 UNAUTHORIZED"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "404", description = "The user not found when entered not existent uuid.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "NotFoundException",
                                                "error_message": "User with c0eebc99-9c0b-4ef8-bb6d-00000006bb9b not found!",
                                                "error_code": "404 NOT_FOUND"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "409", description = "The endpoint has not been completed when entered not existent uuid.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "MethodArgumentNotValidException",
                                                "error_message": "{firstName=размер должен находиться в диапазоне от 2 до 40, email=должно иметь формат адреса электронной почты, lastName=размер должен находиться в диапазоне от 2 до 50}",
                                                "error_code": "409 CONFLICT"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "406", description = "The endpoint has not been completed when the email of UserUpdateRequest is exist in BD.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "UniqueEmailException",
                                                "error_message": "Email nikolayv@example.com is occupied! Another user is already registered by this email!",
                                                "error_code": "406 NOT_ACCEPTABLE"
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
                                                "exception": "NotFoundException",
                                                "error_message": "User with c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15 not found!",
                                                "error_code": "404 NOT_FOUND"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "The endpoint has not been completed when an invalid uuid is entered.",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-02-27T22:19:46.837+00:00",
                                                "status": 400,
                                                "error": "Bad Request",
                                                "path": "/api/v1/users/deactivate/12"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "409", description = "You cannot change the status of a user with the role ADMIN.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "InformationChangeStatusUserException",
                                                "error_message": "You cannot change the status of a user with the ADMIN role.",
                                                "error_code": "409 CONFLICT"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when the token is not entered.",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                    Unauthorized.
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an expired token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "ExpiredJwtException",
                                                "error_message": "JWT expired at 2024-02-25T17:00:46Z. Current time: 2024-02-27T22:24:19Z, a difference of 192213453 milliseconds.  Allowed clock skew: 0 milliseconds.",
                                                "error_code": "401 UNAUTHORIZED"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an invalid token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "SignatureException",
                                                "error_message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                "error_code": "401 UNAUTHORIZED"
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
                                                "exception": "NotFoundException",
                                                "error_message": "User with c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a18 not found!",
                                                "error_code": "404 NOT_FOUND"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "The endpoint has not been completed when an invalid uuid is entered.",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-02-27T22:26:56.666+00:00",
                                                "status": 400,
                                                "error": "Bad Request",
                                                "path": "/api/v1/users/delete/11"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "409", description = "You cannot change the status of a user with the role ADMIN.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "InformationChangeStatusUserException",
                                                "error_message": "You cannot change the status of a user with the ADMIN role.",
                                                "error_code": "409 CONFLICT"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when the token is not entered.",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                    Unauthorized.
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an expired token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "ExpiredJwtException",
                                                "error_message": "JWT expired at 2024-02-25T17:00:46Z. Current time: 2024-02-27T22:28:31Z, a difference of 192465784 milliseconds.  Allowed clock skew: 0 milliseconds.",
                                                "error_code": "401 UNAUTHORIZED"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Not Authenticated User when an invalid token is entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "exception": "SignatureException",
                                                "error_message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                "error_code": "401 UNAUTHORIZED"
                                            }
                                    """)))
            }
    )
    ResponseEntity<?> deleteUser(UUID id, @Parameter(hidden = true) String token);
}