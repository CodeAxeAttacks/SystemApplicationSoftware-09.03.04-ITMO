package com.brigada.is.service;

import com.brigada.is.domain.MusicBand;
import com.brigada.is.domain.Studio;
import com.brigada.is.exception.NoPermissionException;
import com.brigada.is.security.entity.Role;
import com.brigada.is.security.entity.User;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {
    public void checkPermission(User user, MusicBand musicBand) {
        if (!canEditOrDelete(user, musicBand)) throw new NoPermissionException("No permission to edit this object");
    }

    public void checkPermission(User user, Studio studio) {
        if (!canEditOrDelete(user, studio)) throw new NoPermissionException("No permission to edit this object");
    }

    private boolean canEditOrDelete(User currentUser, MusicBand musicBand) {
        if (musicBand.getCreatedBy().equals(currentUser)) {
            return true;
        }
        return currentUser.getRoles().contains(Role.ADMIN);
    }

    private boolean canEditOrDelete(User currentUser, Studio studio) {
        if (studio.getCreatedBy().equals(currentUser)) {
            return true;
        }
        return currentUser.getRoles().contains(Role.ADMIN);
    }

}
