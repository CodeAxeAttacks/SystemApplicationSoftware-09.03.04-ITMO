package com.brigada.is.audit;

import com.brigada.is.audit.entity.ActionType;
import com.brigada.is.audit.entity.MusicBandLog;
import com.brigada.is.audit.repository.MusicBandLogRepository;
import com.brigada.is.dto.response.MusicBandResponseDTO;
import com.brigada.is.security.entity.User;
import com.brigada.is.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class MusicBandLoggingAspect {
    private final MusicBandLogRepository logRepository;
    private final UserRepository userRepository;

    @Pointcut("execution(* com.brigada.is.service.MusicBandService.createMusicBand(..))")
    private void createMethod() {
    }

    @Pointcut("execution(* com.brigada.is.service.MusicBandService.updateMusicBand(..))")
    private void updateMethod() {
    }

    @Pointcut("execution(* com.brigada.is.service.MusicBandService.deleteMusicBand(..))")
    private void deleteMethod() {
    }

    @AfterReturning(pointcut = "createMethod()", returning = "createdBand")
    public void logCreateStudyGroup(JoinPoint joinPoint, MusicBandResponseDTO createdBand) {
        Long userId = createdBand.getCreatedBy();

        logAction(ActionType.CREATE, userId, createdBand.getId());
    }

    @AfterReturning(pointcut = "updateMethod()", returning = "updatedBand")
    public void logUpdateStudyGroup(JoinPoint joinPoint, MusicBandResponseDTO updatedBand) {
        Long userId = updatedBand.getCreatedBy();

        logAction(ActionType.UPDATE, userId, updatedBand.getId());
    }

    @AfterReturning(pointcut = "deleteMethod()")
    public void logDeleteStudyGroup(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long id = ((Long) args[0]);
        String username = ((String) args[1]);

        logAction(ActionType.DELETE, username, id);
    }

    private void logAction(ActionType action, String username, Long id) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        userOptional.ifPresent(user -> logAction(action, user, id));
    }

    private void logAction(ActionType action, Long userId, Long id) {
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(user -> logAction(action, user, id));
    }

    private void logAction(ActionType action, User user, Long id) {
        MusicBandLog log = new MusicBandLog();
        log.setAction(action);
        log.setUser(user);
        log.setStudyGroupId(id);
        log.setActionTime(ZonedDateTime.now());
        logRepository.save(log);
    }
}
