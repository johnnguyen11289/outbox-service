package io.yody.notification.service.mapper;

import io.yody.notification.domain.NotificationEntity;
import io.yody.notification.service.dto.NotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotificationEntity} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, NotificationEntity> {}
