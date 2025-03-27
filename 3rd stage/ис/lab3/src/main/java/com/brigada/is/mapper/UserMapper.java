package com.brigada.is.mapper;

import com.brigada.is.security.entity.AdminApplication;
import com.brigada.is.security.entity.Role;
import com.brigada.is.security.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "roles", expression = "java(getAdminRole())")
    @Mapping(target = "id", ignore = true)
    User toUser(AdminApplication application);

    AdminApplication toAdminApplication(User user);

    default Set<Role> getAdminRole() {
        return Set.of(Role.ADMIN);
    }
}
