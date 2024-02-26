package com.solbeg.userservice.util.testdata;

import com.solbeg.userservice.entity.Role;
import com.solbeg.userservice.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.solbeg.userservice.util.initdata.InitData.CREATED_AT_ROLE;
import static com.solbeg.userservice.util.initdata.InitData.CREATED_BY_ROLE;
import static com.solbeg.userservice.util.initdata.InitData.ID_ROLE;
import static com.solbeg.userservice.util.initdata.InitData.ROLE_NAME_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.UPDATED_AT_ROLE;
import static com.solbeg.userservice.util.initdata.InitData.UPDATED_BY_ROLE;

@Data
@Builder(setterPrefix = "with")
public class RoleTestData {

    @Builder.Default
    private UUID id = ID_ROLE;

    @Builder.Default
    private UUID createdBy = CREATED_BY_ROLE;

    @Builder.Default
    private UUID updatedBy = UPDATED_BY_ROLE;

    @Builder.Default
    private LocalDateTime createdAt = CREATED_AT_ROLE;

    @Builder.Default
    private LocalDateTime updatedAt = UPDATED_AT_ROLE;

    @Builder.Default
    private String name = ROLE_NAME_JOURNALIST;

    @Builder.Default
    private List<User> users = List.of();

    public Role getEntity() {
        return Role.builder()
                .id(id)
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .name(name)
                .users(users)
                .build();
    }
}