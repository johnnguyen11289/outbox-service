package io.yody.notification.service.mapper;

import io.yody.notification.domain.MobileDevicesEntity;
import io.yody.notification.service.dto.MobileDevicesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MobileDevicesEntity} and its DTO {@link MobileDevicesDTO}.
 */
@Mapper(componentModel = "spring")
public interface MobileDevicesMapper extends EntityMapper<MobileDevicesDTO, MobileDevicesEntity> {}
