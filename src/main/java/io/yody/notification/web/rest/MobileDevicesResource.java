package io.yody.notification.web.rest;

import io.yody.notification.repository.MobileDevicesRepository;
import io.yody.notification.service.MobileDevicesQueryService;
import io.yody.notification.service.MobileDevicesService;
import io.yody.notification.service.criteria.MobileDevicesCriteria;
import io.yody.notification.service.dto.MobileDevicesDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nentangso.core.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.yody.notification.domain.MobileDevicesEntity}.
 */
@RestController
@RequestMapping("/api")
public class MobileDevicesResource {

    private final Logger log = LoggerFactory.getLogger(MobileDevicesResource.class);

    private static final String ENTITY_NAME = "yodyNotificationMobileDevices";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MobileDevicesService mobileDevicesService;

    private final MobileDevicesRepository mobileDevicesRepository;

    private final MobileDevicesQueryService mobileDevicesQueryService;

    public MobileDevicesResource(
        MobileDevicesService mobileDevicesService,
        MobileDevicesRepository mobileDevicesRepository,
        MobileDevicesQueryService mobileDevicesQueryService
    ) {
        this.mobileDevicesService = mobileDevicesService;
        this.mobileDevicesRepository = mobileDevicesRepository;
        this.mobileDevicesQueryService = mobileDevicesQueryService;
    }

    /**
     * {@code POST  /mobile-devices} : Create a new mobileDevices.
     *
     * @param mobileDevicesDTO the mobileDevicesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mobileDevicesDTO, or with status {@code 400 (Bad Request)} if the mobileDevices has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mobile-devices")
    public ResponseEntity<MobileDevicesDTO> createMobileDevices(@Valid @RequestBody MobileDevicesDTO mobileDevicesDTO)
        throws URISyntaxException {
        log.debug("REST request to save MobileDevices : {}", mobileDevicesDTO);
        if (mobileDevicesDTO.getId() != null) {
            throw new BadRequestAlertException("A new mobileDevices cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MobileDevicesDTO result = mobileDevicesService.save(mobileDevicesDTO);
        return ResponseEntity
            .created(new URI("/api/mobile-devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mobile-devices/:id} : Updates an existing mobileDevices.
     *
     * @param id the id of the mobileDevicesDTO to save.
     * @param mobileDevicesDTO the mobileDevicesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mobileDevicesDTO,
     * or with status {@code 400 (Bad Request)} if the mobileDevicesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mobileDevicesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mobile-devices/{id}")
    public ResponseEntity<MobileDevicesDTO> updateMobileDevices(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MobileDevicesDTO mobileDevicesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MobileDevices : {}, {}", id, mobileDevicesDTO);
        if (mobileDevicesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mobileDevicesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mobileDevicesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MobileDevicesDTO result = mobileDevicesService.update(mobileDevicesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mobileDevicesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mobile-devices/:id} : Partial updates given fields of an existing mobileDevices, field will ignore if it is null
     *
     * @param id the id of the mobileDevicesDTO to save.
     * @param mobileDevicesDTO the mobileDevicesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mobileDevicesDTO,
     * or with status {@code 400 (Bad Request)} if the mobileDevicesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mobileDevicesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mobileDevicesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mobile-devices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MobileDevicesDTO> partialUpdateMobileDevices(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MobileDevicesDTO mobileDevicesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MobileDevices partially : {}, {}", id, mobileDevicesDTO);
        if (mobileDevicesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mobileDevicesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mobileDevicesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MobileDevicesDTO> result = mobileDevicesService.partialUpdate(mobileDevicesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mobileDevicesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mobile-devices} : get all the mobileDevices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mobileDevices in body.
     */
    @GetMapping("/mobile-devices")
    public ResponseEntity<List<MobileDevicesDTO>> getAllMobileDevices(
        MobileDevicesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MobileDevices by criteria: {}", criteria);
        Page<MobileDevicesDTO> page = mobileDevicesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mobile-devices/count} : count all the mobileDevices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/mobile-devices/count")
    public ResponseEntity<Long> countMobileDevices(MobileDevicesCriteria criteria) {
        log.debug("REST request to count MobileDevices by criteria: {}", criteria);
        return ResponseEntity.ok().body(mobileDevicesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mobile-devices/:id} : get the "id" mobileDevices.
     *
     * @param id the id of the mobileDevicesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mobileDevicesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mobile-devices/{id}")
    public ResponseEntity<MobileDevicesDTO> getMobileDevices(@PathVariable Long id) {
        log.debug("REST request to get MobileDevices : {}", id);
        Optional<MobileDevicesDTO> mobileDevicesDTO = mobileDevicesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mobileDevicesDTO);
    }

    /**
     * {@code DELETE  /mobile-devices/:id} : delete the "id" mobileDevices.
     *
     * @param id the id of the mobileDevicesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mobile-devices/{id}")
    public ResponseEntity<Void> deleteMobileDevices(@PathVariable Long id) {
        log.debug("REST request to delete MobileDevices : {}", id);
        mobileDevicesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
