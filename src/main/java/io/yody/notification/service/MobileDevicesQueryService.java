package io.yody.notification.service;

import io.yody.notification.domain.*; // for static metamodels
import io.yody.notification.domain.MobileDevicesEntity;
import io.yody.notification.repository.MobileDevicesRepository;
import io.yody.notification.service.criteria.MobileDevicesCriteria;
import io.yody.notification.service.dto.MobileDevicesDTO;
import io.yody.notification.service.mapper.MobileDevicesMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MobileDevicesEntity} entities in the database.
 * The main input is a {@link MobileDevicesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MobileDevicesDTO} or a {@link Page} of {@link MobileDevicesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MobileDevicesQueryService extends QueryService<MobileDevicesEntity> {

    private final Logger log = LoggerFactory.getLogger(MobileDevicesQueryService.class);

    private final MobileDevicesRepository mobileDevicesRepository;

    private final MobileDevicesMapper mobileDevicesMapper;

    public MobileDevicesQueryService(MobileDevicesRepository mobileDevicesRepository, MobileDevicesMapper mobileDevicesMapper) {
        this.mobileDevicesRepository = mobileDevicesRepository;
        this.mobileDevicesMapper = mobileDevicesMapper;
    }

    /**
     * Return a {@link List} of {@link MobileDevicesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MobileDevicesDTO> findByCriteria(MobileDevicesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MobileDevicesEntity> specification = createSpecification(criteria);
        return mobileDevicesMapper.toDto(mobileDevicesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MobileDevicesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MobileDevicesDTO> findByCriteria(MobileDevicesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MobileDevicesEntity> specification = createSpecification(criteria);
        return mobileDevicesRepository.findAll(specification, page).map(mobileDevicesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MobileDevicesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MobileDevicesEntity> specification = createSpecification(criteria);
        return mobileDevicesRepository.count(specification);
    }

    /**
     * Function to convert {@link MobileDevicesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MobileDevicesEntity> createSpecification(MobileDevicesCriteria criteria) {
        Specification<MobileDevicesEntity> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MobileDevicesEntity_.id));
            }
            if (criteria.getDeviceId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeviceId(), MobileDevicesEntity_.deviceId));
            }
            if (criteria.getDeviceToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeviceToken(), MobileDevicesEntity_.deviceToken));
            }
            if (criteria.getDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getDeleted(), MobileDevicesEntity_.deleted));
            }
            if (criteria.getDeletedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeletedAt(), MobileDevicesEntity_.deletedAt));
            }
            if (criteria.getDeletedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeletedBy(), MobileDevicesEntity_.deletedBy));
            }
            if (criteria.getVersion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVersion(), MobileDevicesEntity_.version));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), MobileDevicesEntity_.createdBy));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), MobileDevicesEntity_.createdAt));
            }
            if (criteria.getUpdatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUpdatedBy(), MobileDevicesEntity_.updatedBy));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), MobileDevicesEntity_.updatedAt));
            }
        }
        return specification;
    }
}
