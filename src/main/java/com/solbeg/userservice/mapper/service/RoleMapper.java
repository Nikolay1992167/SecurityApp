package com.solbeg.userservice.mapper.service;

import com.solbeg.userservice.entity.Role;
import com.solbeg.userservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleMapper {
    private final RoleRepository roleRepository;

    @Named("setRolesList")
    public List<Role> setRolesList(String roleName) {
        return new ArrayList<>(List.of(roleRepository.findByName(roleName)));
    }
}
