package io.yody.notification.service;

import io.yody.notification.domain.MobileDevicesEntity;
import io.yody.notification.repository.MobileDevicesRepository;
import io.yody.notification.service.dto.MobileDevicesDTO;
import io.yody.notification.service.mapper.MobileDevicesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MobileDevicesEntity}.
 */
@Service
@Transactional
public class MobileDevicesService {

    private final Logger log = LoggerFactory.getLogger(MobileDevicesService.class);

    private final MobileDevicesRepository mobileDevicesRepository;

    private final MobileDevicesMapper mobileDevicesMapper;

    public MobileDevicesService(MobileDevicesRepository mobileDevicesRepository, MobileDevicesMapper mobileDevicesMapper) {
        this.mobileDevicesRepository = mobileDevicesRepository;
        this.mobileDevicesMapper = mobileDevicesMapper;
    }

    /**
     * Save a mobileDevices.
     *
     * @param mobileDevicesDTO the entity to save.
     * @return the persisted entity.
     */
    public MobileDevicesDTO save(MobileDevicesDTO mobileDevicesDTO) {
        log.debug("Request to save MobileDevices : {}", mobileDevicesDTO);
        MobileDevicesEntity mobileDevicesEntity = mobileDevicesMapper.toEntity(mobileDevicesDTO);
        mobileDevicesEntity = mobileDevicesRepository.save(mobileDevicesEntity);
        return mobileDevicesMapper.toDto(mobileDevicesEntity);
    }

    /**
     * Update a mobileDevices.
     *
     * @param mobileDevicesDTO the entity to save.
     * @return the persisted entity.
     */
    public MobileDevicesDTO update(MobileDevicesDTO mobileDevicesDTO) {
        log.debug("Request to update MobileDevices : {}", mobileDevicesDTO);
        MobileDevicesEntity mobileDevicesEntity = mobileDevicesMapper.toEntity(mobileDevicesDTO);
        mobileDevicesEntity = mobileDevicesRepository.save(mobileDevicesEntity);
        return mobileDevicesMapper.toDto(mobileDevicesEntity);
    }

    /**
     * Partially update a mobileDevices.
     *
     * @param mobileDevicesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MobileDevicesDTO> partialUpdate(MobileDevicesDTO mobileDevicesDTO) {
        log.debug("Request to partially update MobileDevices : {}", mobileDevicesDTO);

        return mobileDevicesRepository
            .findById(mobileDevicesDTO.getId())
            .map(existingMobileDevices -> {
                mobileDevicesMapper.partialUpdate(existingMobileDevices, mobileDevicesDTO);

                return existingMobileDevices;
            })
            .map(mobileDevicesRepository::save)
            .map(mobileDevicesMapper::toDto);
    }

    /**
     * Get all the mobileDevices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MobileDevicesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MobileDevices");
        return mobileDevicesRepository.findAll(pageable).map(mobileDevicesMapper::toDto);
    }

    /**
     * Get one mobileDevices by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MobileDevicesDTO> findOne(Long id) {
        log.debug("Request to get MobileDevices : {}", id);
        return mobileDevicesRepository.findById(id).map(mobileDevicesMapper::toDto);
    }

    /**
     * Delete the mobileDevices by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MobileDevices : {}", id);
        mobileDevicesRepository.deleteById(id);
    }
}
