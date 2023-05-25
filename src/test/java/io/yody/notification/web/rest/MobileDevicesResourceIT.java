package io.yody.notification.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.yody.notification.IntegrationTest;
import io.yody.notification.domain.MobileDevicesEntity;
import io.yody.notification.repository.MobileDevicesRepository;
import io.yody.notification.service.criteria.MobileDevicesCriteria;
import io.yody.notification.service.dto.MobileDevicesDTO;
import io.yody.notification.service.mapper.MobileDevicesMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MobileDevicesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MobileDevicesResourceIT {

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_TOKEN = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DELETED_BY = "AAAAAAAAAA";
    private static final String UPDATED_DELETED_BY = "BBBBBBBBBB";

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;
    private static final Integer SMALLER_VERSION = 1 - 1;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/mobile-devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MobileDevicesRepository mobileDevicesRepository;

    @Autowired
    private MobileDevicesMapper mobileDevicesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMobileDevicesMockMvc;

    private MobileDevicesEntity mobileDevicesEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MobileDevicesEntity createEntity(EntityManager em) {
        MobileDevicesEntity mobileDevicesEntity = new MobileDevicesEntity()
            .deviceId(DEFAULT_DEVICE_ID)
            .deviceToken(DEFAULT_DEVICE_TOKEN)
            .deleted(DEFAULT_DELETED)
            .deletedAt(DEFAULT_DELETED_AT)
            .deletedBy(DEFAULT_DELETED_BY)
            .version(DEFAULT_VERSION)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT);
        return mobileDevicesEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MobileDevicesEntity createUpdatedEntity(EntityManager em) {
        MobileDevicesEntity mobileDevicesEntity = new MobileDevicesEntity()
            .deviceId(UPDATED_DEVICE_ID)
            .deviceToken(UPDATED_DEVICE_TOKEN)
            .deleted(UPDATED_DELETED)
            .deletedAt(UPDATED_DELETED_AT)
            .deletedBy(UPDATED_DELETED_BY)
            .version(UPDATED_VERSION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);
        return mobileDevicesEntity;
    }

    @BeforeEach
    public void initTest() {
        mobileDevicesEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createMobileDevices() throws Exception {
        int databaseSizeBeforeCreate = mobileDevicesRepository.findAll().size();
        // Create the MobileDevices
        MobileDevicesDTO mobileDevicesDTO = mobileDevicesMapper.toDto(mobileDevicesEntity);
        restMobileDevicesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileDevicesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MobileDevices in the database
        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeCreate + 1);
        MobileDevicesEntity testMobileDevices = mobileDevicesList.get(mobileDevicesList.size() - 1);
        assertThat(testMobileDevices.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testMobileDevices.getDeviceToken()).isEqualTo(DEFAULT_DEVICE_TOKEN);
        assertThat(testMobileDevices.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testMobileDevices.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
        assertThat(testMobileDevices.getDeletedBy()).isEqualTo(DEFAULT_DELETED_BY);
        assertThat(testMobileDevices.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testMobileDevices.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testMobileDevices.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testMobileDevices.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testMobileDevices.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createMobileDevicesWithExistingId() throws Exception {
        // Create the MobileDevices with an existing ID
        mobileDevicesEntity.setId(1L);
        MobileDevicesDTO mobileDevicesDTO = mobileDevicesMapper.toDto(mobileDevicesEntity);

        int databaseSizeBeforeCreate = mobileDevicesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMobileDevicesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileDevicesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MobileDevices in the database
        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDeviceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = mobileDevicesRepository.findAll().size();
        // set the field null
        mobileDevicesEntity.setDeviceId(null);

        // Create the MobileDevices, which fails.
        MobileDevicesDTO mobileDevicesDTO = mobileDevicesMapper.toDto(mobileDevicesEntity);

        restMobileDevicesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileDevicesDTO))
            )
            .andExpect(status().isBadRequest());

        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeviceTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = mobileDevicesRepository.findAll().size();
        // set the field null
        mobileDevicesEntity.setDeviceToken(null);

        // Create the MobileDevices, which fails.
        MobileDevicesDTO mobileDevicesDTO = mobileDevicesMapper.toDto(mobileDevicesEntity);

        restMobileDevicesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileDevicesDTO))
            )
            .andExpect(status().isBadRequest());

        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = mobileDevicesRepository.findAll().size();
        // set the field null
        mobileDevicesEntity.setDeleted(null);

        // Create the MobileDevices, which fails.
        MobileDevicesDTO mobileDevicesDTO = mobileDevicesMapper.toDto(mobileDevicesEntity);

        restMobileDevicesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileDevicesDTO))
            )
            .andExpect(status().isBadRequest());

        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = mobileDevicesRepository.findAll().size();
        // set the field null
        mobileDevicesEntity.setVersion(null);

        // Create the MobileDevices, which fails.
        MobileDevicesDTO mobileDevicesDTO = mobileDevicesMapper.toDto(mobileDevicesEntity);

        restMobileDevicesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileDevicesDTO))
            )
            .andExpect(status().isBadRequest());

        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = mobileDevicesRepository.findAll().size();
        // set the field null
        mobileDevicesEntity.setCreatedBy(null);

        // Create the MobileDevices, which fails.
        MobileDevicesDTO mobileDevicesDTO = mobileDevicesMapper.toDto(mobileDevicesEntity);

        restMobileDevicesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileDevicesDTO))
            )
            .andExpect(status().isBadRequest());

        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = mobileDevicesRepository.findAll().size();
        // set the field null
        mobileDevicesEntity.setCreatedAt(null);

        // Create the MobileDevices, which fails.
        MobileDevicesDTO mobileDevicesDTO = mobileDevicesMapper.toDto(mobileDevicesEntity);

        restMobileDevicesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileDevicesDTO))
            )
            .andExpect(status().isBadRequest());

        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMobileDevices() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList
        restMobileDevicesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mobileDevicesEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID)))
            .andExpect(jsonPath("$.[*].deviceToken").value(hasItem(DEFAULT_DEVICE_TOKEN)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedBy").value(hasItem(DEFAULT_DELETED_BY)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getMobileDevices() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get the mobileDevices
        restMobileDevicesMockMvc
            .perform(get(ENTITY_API_URL_ID, mobileDevicesEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mobileDevicesEntity.getId().intValue()))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID))
            .andExpect(jsonPath("$.deviceToken").value(DEFAULT_DEVICE_TOKEN))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()))
            .andExpect(jsonPath("$.deletedBy").value(DEFAULT_DELETED_BY))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getMobileDevicesByIdFiltering() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        Long id = mobileDevicesEntity.getId();

        defaultMobileDevicesShouldBeFound("id.equals=" + id);
        defaultMobileDevicesShouldNotBeFound("id.notEquals=" + id);

        defaultMobileDevicesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMobileDevicesShouldNotBeFound("id.greaterThan=" + id);

        defaultMobileDevicesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMobileDevicesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeviceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deviceId equals to DEFAULT_DEVICE_ID
        defaultMobileDevicesShouldBeFound("deviceId.equals=" + DEFAULT_DEVICE_ID);

        // Get all the mobileDevicesList where deviceId equals to UPDATED_DEVICE_ID
        defaultMobileDevicesShouldNotBeFound("deviceId.equals=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeviceIdIsInShouldWork() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deviceId in DEFAULT_DEVICE_ID or UPDATED_DEVICE_ID
        defaultMobileDevicesShouldBeFound("deviceId.in=" + DEFAULT_DEVICE_ID + "," + UPDATED_DEVICE_ID);

        // Get all the mobileDevicesList where deviceId equals to UPDATED_DEVICE_ID
        defaultMobileDevicesShouldNotBeFound("deviceId.in=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeviceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deviceId is not null
        defaultMobileDevicesShouldBeFound("deviceId.specified=true");

        // Get all the mobileDevicesList where deviceId is null
        defaultMobileDevicesShouldNotBeFound("deviceId.specified=false");
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeviceIdContainsSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deviceId contains DEFAULT_DEVICE_ID
        defaultMobileDevicesShouldBeFound("deviceId.contains=" + DEFAULT_DEVICE_ID);

        // Get all the mobileDevicesList where deviceId contains UPDATED_DEVICE_ID
        defaultMobileDevicesShouldNotBeFound("deviceId.contains=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeviceIdNotContainsSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deviceId does not contain DEFAULT_DEVICE_ID
        defaultMobileDevicesShouldNotBeFound("deviceId.doesNotContain=" + DEFAULT_DEVICE_ID);

        // Get all the mobileDevicesList where deviceId does not contain UPDATED_DEVICE_ID
        defaultMobileDevicesShouldBeFound("deviceId.doesNotContain=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeviceTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deviceToken equals to DEFAULT_DEVICE_TOKEN
        defaultMobileDevicesShouldBeFound("deviceToken.equals=" + DEFAULT_DEVICE_TOKEN);

        // Get all the mobileDevicesList where deviceToken equals to UPDATED_DEVICE_TOKEN
        defaultMobileDevicesShouldNotBeFound("deviceToken.equals=" + UPDATED_DEVICE_TOKEN);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeviceTokenIsInShouldWork() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deviceToken in DEFAULT_DEVICE_TOKEN or UPDATED_DEVICE_TOKEN
        defaultMobileDevicesShouldBeFound("deviceToken.in=" + DEFAULT_DEVICE_TOKEN + "," + UPDATED_DEVICE_TOKEN);

        // Get all the mobileDevicesList where deviceToken equals to UPDATED_DEVICE_TOKEN
        defaultMobileDevicesShouldNotBeFound("deviceToken.in=" + UPDATED_DEVICE_TOKEN);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeviceTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deviceToken is not null
        defaultMobileDevicesShouldBeFound("deviceToken.specified=true");

        // Get all the mobileDevicesList where deviceToken is null
        defaultMobileDevicesShouldNotBeFound("deviceToken.specified=false");
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeviceTokenContainsSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deviceToken contains DEFAULT_DEVICE_TOKEN
        defaultMobileDevicesShouldBeFound("deviceToken.contains=" + DEFAULT_DEVICE_TOKEN);

        // Get all the mobileDevicesList where deviceToken contains UPDATED_DEVICE_TOKEN
        defaultMobileDevicesShouldNotBeFound("deviceToken.contains=" + UPDATED_DEVICE_TOKEN);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeviceTokenNotContainsSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deviceToken does not contain DEFAULT_DEVICE_TOKEN
        defaultMobileDevicesShouldNotBeFound("deviceToken.doesNotContain=" + DEFAULT_DEVICE_TOKEN);

        // Get all the mobileDevicesList where deviceToken does not contain UPDATED_DEVICE_TOKEN
        defaultMobileDevicesShouldBeFound("deviceToken.doesNotContain=" + UPDATED_DEVICE_TOKEN);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deleted equals to DEFAULT_DELETED
        defaultMobileDevicesShouldBeFound("deleted.equals=" + DEFAULT_DELETED);

        // Get all the mobileDevicesList where deleted equals to UPDATED_DELETED
        defaultMobileDevicesShouldNotBeFound("deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deleted in DEFAULT_DELETED or UPDATED_DELETED
        defaultMobileDevicesShouldBeFound("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED);

        // Get all the mobileDevicesList where deleted equals to UPDATED_DELETED
        defaultMobileDevicesShouldNotBeFound("deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deleted is not null
        defaultMobileDevicesShouldBeFound("deleted.specified=true");

        // Get all the mobileDevicesList where deleted is null
        defaultMobileDevicesShouldNotBeFound("deleted.specified=false");
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeletedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deletedAt equals to DEFAULT_DELETED_AT
        defaultMobileDevicesShouldBeFound("deletedAt.equals=" + DEFAULT_DELETED_AT);

        // Get all the mobileDevicesList where deletedAt equals to UPDATED_DELETED_AT
        defaultMobileDevicesShouldNotBeFound("deletedAt.equals=" + UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeletedAtIsInShouldWork() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deletedAt in DEFAULT_DELETED_AT or UPDATED_DELETED_AT
        defaultMobileDevicesShouldBeFound("deletedAt.in=" + DEFAULT_DELETED_AT + "," + UPDATED_DELETED_AT);

        // Get all the mobileDevicesList where deletedAt equals to UPDATED_DELETED_AT
        defaultMobileDevicesShouldNotBeFound("deletedAt.in=" + UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeletedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deletedAt is not null
        defaultMobileDevicesShouldBeFound("deletedAt.specified=true");

        // Get all the mobileDevicesList where deletedAt is null
        defaultMobileDevicesShouldNotBeFound("deletedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeletedByIsEqualToSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deletedBy equals to DEFAULT_DELETED_BY
        defaultMobileDevicesShouldBeFound("deletedBy.equals=" + DEFAULT_DELETED_BY);

        // Get all the mobileDevicesList where deletedBy equals to UPDATED_DELETED_BY
        defaultMobileDevicesShouldNotBeFound("deletedBy.equals=" + UPDATED_DELETED_BY);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeletedByIsInShouldWork() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deletedBy in DEFAULT_DELETED_BY or UPDATED_DELETED_BY
        defaultMobileDevicesShouldBeFound("deletedBy.in=" + DEFAULT_DELETED_BY + "," + UPDATED_DELETED_BY);

        // Get all the mobileDevicesList where deletedBy equals to UPDATED_DELETED_BY
        defaultMobileDevicesShouldNotBeFound("deletedBy.in=" + UPDATED_DELETED_BY);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeletedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deletedBy is not null
        defaultMobileDevicesShouldBeFound("deletedBy.specified=true");

        // Get all the mobileDevicesList where deletedBy is null
        defaultMobileDevicesShouldNotBeFound("deletedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeletedByContainsSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deletedBy contains DEFAULT_DELETED_BY
        defaultMobileDevicesShouldBeFound("deletedBy.contains=" + DEFAULT_DELETED_BY);

        // Get all the mobileDevicesList where deletedBy contains UPDATED_DELETED_BY
        defaultMobileDevicesShouldNotBeFound("deletedBy.contains=" + UPDATED_DELETED_BY);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByDeletedByNotContainsSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where deletedBy does not contain DEFAULT_DELETED_BY
        defaultMobileDevicesShouldNotBeFound("deletedBy.doesNotContain=" + DEFAULT_DELETED_BY);

        // Get all the mobileDevicesList where deletedBy does not contain UPDATED_DELETED_BY
        defaultMobileDevicesShouldBeFound("deletedBy.doesNotContain=" + UPDATED_DELETED_BY);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where version equals to DEFAULT_VERSION
        defaultMobileDevicesShouldBeFound("version.equals=" + DEFAULT_VERSION);

        // Get all the mobileDevicesList where version equals to UPDATED_VERSION
        defaultMobileDevicesShouldNotBeFound("version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where version in DEFAULT_VERSION or UPDATED_VERSION
        defaultMobileDevicesShouldBeFound("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION);

        // Get all the mobileDevicesList where version equals to UPDATED_VERSION
        defaultMobileDevicesShouldNotBeFound("version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where version is not null
        defaultMobileDevicesShouldBeFound("version.specified=true");

        // Get all the mobileDevicesList where version is null
        defaultMobileDevicesShouldNotBeFound("version.specified=false");
    }

    @Test
    @Transactional
    void getAllMobileDevicesByVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where version is greater than or equal to DEFAULT_VERSION
        defaultMobileDevicesShouldBeFound("version.greaterThanOrEqual=" + DEFAULT_VERSION);

        // Get all the mobileDevicesList where version is greater than or equal to UPDATED_VERSION
        defaultMobileDevicesShouldNotBeFound("version.greaterThanOrEqual=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByVersionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where version is less than or equal to DEFAULT_VERSION
        defaultMobileDevicesShouldBeFound("version.lessThanOrEqual=" + DEFAULT_VERSION);

        // Get all the mobileDevicesList where version is less than or equal to SMALLER_VERSION
        defaultMobileDevicesShouldNotBeFound("version.lessThanOrEqual=" + SMALLER_VERSION);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where version is less than DEFAULT_VERSION
        defaultMobileDevicesShouldNotBeFound("version.lessThan=" + DEFAULT_VERSION);

        // Get all the mobileDevicesList where version is less than UPDATED_VERSION
        defaultMobileDevicesShouldBeFound("version.lessThan=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByVersionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where version is greater than DEFAULT_VERSION
        defaultMobileDevicesShouldNotBeFound("version.greaterThan=" + DEFAULT_VERSION);

        // Get all the mobileDevicesList where version is greater than SMALLER_VERSION
        defaultMobileDevicesShouldBeFound("version.greaterThan=" + SMALLER_VERSION);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where createdBy equals to DEFAULT_CREATED_BY
        defaultMobileDevicesShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the mobileDevicesList where createdBy equals to UPDATED_CREATED_BY
        defaultMobileDevicesShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultMobileDevicesShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the mobileDevicesList where createdBy equals to UPDATED_CREATED_BY
        defaultMobileDevicesShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where createdBy is not null
        defaultMobileDevicesShouldBeFound("createdBy.specified=true");

        // Get all the mobileDevicesList where createdBy is null
        defaultMobileDevicesShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMobileDevicesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where createdBy contains DEFAULT_CREATED_BY
        defaultMobileDevicesShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the mobileDevicesList where createdBy contains UPDATED_CREATED_BY
        defaultMobileDevicesShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where createdBy does not contain DEFAULT_CREATED_BY
        defaultMobileDevicesShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the mobileDevicesList where createdBy does not contain UPDATED_CREATED_BY
        defaultMobileDevicesShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where createdAt equals to DEFAULT_CREATED_AT
        defaultMobileDevicesShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the mobileDevicesList where createdAt equals to UPDATED_CREATED_AT
        defaultMobileDevicesShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultMobileDevicesShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the mobileDevicesList where createdAt equals to UPDATED_CREATED_AT
        defaultMobileDevicesShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where createdAt is not null
        defaultMobileDevicesShouldBeFound("createdAt.specified=true");

        // Get all the mobileDevicesList where createdAt is null
        defaultMobileDevicesShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllMobileDevicesByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where updatedBy equals to DEFAULT_UPDATED_BY
        defaultMobileDevicesShouldBeFound("updatedBy.equals=" + DEFAULT_UPDATED_BY);

        // Get all the mobileDevicesList where updatedBy equals to UPDATED_UPDATED_BY
        defaultMobileDevicesShouldNotBeFound("updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where updatedBy in DEFAULT_UPDATED_BY or UPDATED_UPDATED_BY
        defaultMobileDevicesShouldBeFound("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY);

        // Get all the mobileDevicesList where updatedBy equals to UPDATED_UPDATED_BY
        defaultMobileDevicesShouldNotBeFound("updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where updatedBy is not null
        defaultMobileDevicesShouldBeFound("updatedBy.specified=true");

        // Get all the mobileDevicesList where updatedBy is null
        defaultMobileDevicesShouldNotBeFound("updatedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMobileDevicesByUpdatedByContainsSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where updatedBy contains DEFAULT_UPDATED_BY
        defaultMobileDevicesShouldBeFound("updatedBy.contains=" + DEFAULT_UPDATED_BY);

        // Get all the mobileDevicesList where updatedBy contains UPDATED_UPDATED_BY
        defaultMobileDevicesShouldNotBeFound("updatedBy.contains=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByUpdatedByNotContainsSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where updatedBy does not contain DEFAULT_UPDATED_BY
        defaultMobileDevicesShouldNotBeFound("updatedBy.doesNotContain=" + DEFAULT_UPDATED_BY);

        // Get all the mobileDevicesList where updatedBy does not contain UPDATED_UPDATED_BY
        defaultMobileDevicesShouldBeFound("updatedBy.doesNotContain=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultMobileDevicesShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the mobileDevicesList where updatedAt equals to UPDATED_UPDATED_AT
        defaultMobileDevicesShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultMobileDevicesShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the mobileDevicesList where updatedAt equals to UPDATED_UPDATED_AT
        defaultMobileDevicesShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllMobileDevicesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        // Get all the mobileDevicesList where updatedAt is not null
        defaultMobileDevicesShouldBeFound("updatedAt.specified=true");

        // Get all the mobileDevicesList where updatedAt is null
        defaultMobileDevicesShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMobileDevicesShouldBeFound(String filter) throws Exception {
        restMobileDevicesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mobileDevicesEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID)))
            .andExpect(jsonPath("$.[*].deviceToken").value(hasItem(DEFAULT_DEVICE_TOKEN)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedBy").value(hasItem(DEFAULT_DELETED_BY)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restMobileDevicesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMobileDevicesShouldNotBeFound(String filter) throws Exception {
        restMobileDevicesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMobileDevicesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMobileDevices() throws Exception {
        // Get the mobileDevices
        restMobileDevicesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMobileDevices() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        int databaseSizeBeforeUpdate = mobileDevicesRepository.findAll().size();

        // Update the mobileDevices
        MobileDevicesEntity updatedMobileDevicesEntity = mobileDevicesRepository.findById(mobileDevicesEntity.getId()).get();
        // Disconnect from session so that the updates on updatedMobileDevicesEntity are not directly saved in db
        em.detach(updatedMobileDevicesEntity);
        updatedMobileDevicesEntity
            .deviceId(UPDATED_DEVICE_ID)
            .deviceToken(UPDATED_DEVICE_TOKEN)
            .deleted(UPDATED_DELETED)
            .deletedAt(UPDATED_DELETED_AT)
            .deletedBy(UPDATED_DELETED_BY)
            .version(UPDATED_VERSION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);
        MobileDevicesDTO mobileDevicesDTO = mobileDevicesMapper.toDto(updatedMobileDevicesEntity);

        restMobileDevicesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mobileDevicesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileDevicesDTO))
            )
            .andExpect(status().isOk());

        // Validate the MobileDevices in the database
        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeUpdate);
        MobileDevicesEntity testMobileDevices = mobileDevicesList.get(mobileDevicesList.size() - 1);
        assertThat(testMobileDevices.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testMobileDevices.getDeviceToken()).isEqualTo(UPDATED_DEVICE_TOKEN);
        assertThat(testMobileDevices.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testMobileDevices.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
        assertThat(testMobileDevices.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testMobileDevices.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testMobileDevices.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMobileDevices.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testMobileDevices.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testMobileDevices.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingMobileDevices() throws Exception {
        int databaseSizeBeforeUpdate = mobileDevicesRepository.findAll().size();
        mobileDevicesEntity.setId(count.incrementAndGet());

        // Create the MobileDevices
        MobileDevicesDTO mobileDevicesDTO = mobileDevicesMapper.toDto(mobileDevicesEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMobileDevicesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mobileDevicesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileDevicesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MobileDevices in the database
        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMobileDevices() throws Exception {
        int databaseSizeBeforeUpdate = mobileDevicesRepository.findAll().size();
        mobileDevicesEntity.setId(count.incrementAndGet());

        // Create the MobileDevices
        MobileDevicesDTO mobileDevicesDTO = mobileDevicesMapper.toDto(mobileDevicesEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMobileDevicesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileDevicesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MobileDevices in the database
        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMobileDevices() throws Exception {
        int databaseSizeBeforeUpdate = mobileDevicesRepository.findAll().size();
        mobileDevicesEntity.setId(count.incrementAndGet());

        // Create the MobileDevices
        MobileDevicesDTO mobileDevicesDTO = mobileDevicesMapper.toDto(mobileDevicesEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMobileDevicesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileDevicesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MobileDevices in the database
        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMobileDevicesWithPatch() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        int databaseSizeBeforeUpdate = mobileDevicesRepository.findAll().size();

        // Update the mobileDevices using partial update
        MobileDevicesEntity partialUpdatedMobileDevicesEntity = new MobileDevicesEntity();
        partialUpdatedMobileDevicesEntity.setId(mobileDevicesEntity.getId());

        partialUpdatedMobileDevicesEntity.createdBy(UPDATED_CREATED_BY).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restMobileDevicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMobileDevicesEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMobileDevicesEntity))
            )
            .andExpect(status().isOk());

        // Validate the MobileDevices in the database
        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeUpdate);
        MobileDevicesEntity testMobileDevices = mobileDevicesList.get(mobileDevicesList.size() - 1);
        assertThat(testMobileDevices.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testMobileDevices.getDeviceToken()).isEqualTo(DEFAULT_DEVICE_TOKEN);
        assertThat(testMobileDevices.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testMobileDevices.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
        assertThat(testMobileDevices.getDeletedBy()).isEqualTo(DEFAULT_DELETED_BY);
        assertThat(testMobileDevices.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testMobileDevices.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMobileDevices.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testMobileDevices.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testMobileDevices.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateMobileDevicesWithPatch() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        int databaseSizeBeforeUpdate = mobileDevicesRepository.findAll().size();

        // Update the mobileDevices using partial update
        MobileDevicesEntity partialUpdatedMobileDevicesEntity = new MobileDevicesEntity();
        partialUpdatedMobileDevicesEntity.setId(mobileDevicesEntity.getId());

        partialUpdatedMobileDevicesEntity
            .deviceId(UPDATED_DEVICE_ID)
            .deviceToken(UPDATED_DEVICE_TOKEN)
            .deleted(UPDATED_DELETED)
            .deletedAt(UPDATED_DELETED_AT)
            .deletedBy(UPDATED_DELETED_BY)
            .version(UPDATED_VERSION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restMobileDevicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMobileDevicesEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMobileDevicesEntity))
            )
            .andExpect(status().isOk());

        // Validate the MobileDevices in the database
        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeUpdate);
        MobileDevicesEntity testMobileDevices = mobileDevicesList.get(mobileDevicesList.size() - 1);
        assertThat(testMobileDevices.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testMobileDevices.getDeviceToken()).isEqualTo(UPDATED_DEVICE_TOKEN);
        assertThat(testMobileDevices.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testMobileDevices.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
        assertThat(testMobileDevices.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testMobileDevices.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testMobileDevices.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMobileDevices.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testMobileDevices.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testMobileDevices.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingMobileDevices() throws Exception {
        int databaseSizeBeforeUpdate = mobileDevicesRepository.findAll().size();
        mobileDevicesEntity.setId(count.incrementAndGet());

        // Create the MobileDevices
        MobileDevicesDTO mobileDevicesDTO = mobileDevicesMapper.toDto(mobileDevicesEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMobileDevicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mobileDevicesDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mobileDevicesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MobileDevices in the database
        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMobileDevices() throws Exception {
        int databaseSizeBeforeUpdate = mobileDevicesRepository.findAll().size();
        mobileDevicesEntity.setId(count.incrementAndGet());

        // Create the MobileDevices
        MobileDevicesDTO mobileDevicesDTO = mobileDevicesMapper.toDto(mobileDevicesEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMobileDevicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mobileDevicesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MobileDevices in the database
        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMobileDevices() throws Exception {
        int databaseSizeBeforeUpdate = mobileDevicesRepository.findAll().size();
        mobileDevicesEntity.setId(count.incrementAndGet());

        // Create the MobileDevices
        MobileDevicesDTO mobileDevicesDTO = mobileDevicesMapper.toDto(mobileDevicesEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMobileDevicesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mobileDevicesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MobileDevices in the database
        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMobileDevices() throws Exception {
        // Initialize the database
        mobileDevicesRepository.saveAndFlush(mobileDevicesEntity);

        int databaseSizeBeforeDelete = mobileDevicesRepository.findAll().size();

        // Delete the mobileDevices
        restMobileDevicesMockMvc
            .perform(delete(ENTITY_API_URL_ID, mobileDevicesEntity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MobileDevicesEntity> mobileDevicesList = mobileDevicesRepository.findAll();
        assertThat(mobileDevicesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
