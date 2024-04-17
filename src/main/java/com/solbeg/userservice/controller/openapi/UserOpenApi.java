package com.solbeg.userservice.controller.openapi;

import com.solbeg.userservice.dto.request.RefreshTokenRequest;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.exception.model.IncorrectData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "The User Api")
public interface UserOpenApi {

    @Operation(
            method = "POST",
            tags = "Authentication",
            description = "Get data about the user.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenRequest.class),
                            examples = @ExampleObject("""
                                    {
                                        "token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuQGdvb2dsZS5jb20iLCJpZCI6ImEwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMSIsInJvbGVzIjpbIkFETUlOIl0sImV4cCI6MTcxMTk2NDQ2Mn0.cF-IwOtiDX0Qg3UY42lIaXOia2WkUyZpH58bZxBxYetBrxgCLPW7C1_wm1M6pyKr66FaWwy7UvRVpQrKXAt99w"
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
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "The endpoint has not been completed because the token is not valid.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-04-03T12:02:01.6087338",
                                        "error_message": "Illegal base64 character 22",
                                        "error_status": 400
                                    }
                                    """)))
            }
    )
    UserResponse getUserData(String token);
}