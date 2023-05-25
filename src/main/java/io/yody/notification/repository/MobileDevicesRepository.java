package io.yody.notification.repository;

import io.yody.notification.domain.MobileDevicesEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MobileDevicesEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MobileDevicesRepository extends JpaRepository<MobileDevicesEntity, Long>, JpaSpecificationExecutor<MobileDevicesEntity> {}
