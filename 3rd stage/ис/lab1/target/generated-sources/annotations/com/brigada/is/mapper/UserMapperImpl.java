package com.brigada.is.mapper;

import com.brigada.is.security.entity.AdminApplication;
import com.brigada.is.security.entity.User;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-09T21:36:23+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20 (Oracle Corporation)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(AdminApplication application) {
        if ( application == null ) {
            return null;
        }

        User user = new User();

        user.setUsername( application.getUsername() );
        user.setPassword( application.getPassword() );

        user.setRoles( getAdminRole() );

        return user;
    }

    @Override
    public AdminApplication toAdminApplication(User user) {
        if ( user == null ) {
            return null;
        }

        AdminApplication adminApplication = new AdminApplication();

        adminApplication.setId( user.getId() );
        adminApplication.setUsername( user.getUsername() );
        adminApplication.setPassword( user.getPassword() );

        return adminApplication;
    }
}
