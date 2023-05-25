package io.yody.notification.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.yody.notification.IntegrationTest;
import io.yody.notification.domain.NotificationEntity;
import io.yody.notification.repository.NotificationRepository;
import io.yody.notification.service.criteria.NotificationCriteria;
import io.yody.notification.service.dto.NotificationDTO;
import io.yody.notification.service.mapper.NotificationMapper;
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
 * Integration tests for the {@link NotificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificationResourceIT {

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SEEN = false;
    private static final Boolean UPDATED_SEEN = true;

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

    private static final String ENTITY_API_URL = "/api/notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationMockMvc;

    private NotificationEntity notificationEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationEntity createEntity(EntityManager em) {
        NotificationEntity notificationEntity = new NotificationEntity()
            .subject(DEFAULT_SUBJECT)
            .seen(DEFAULT_SEEN)
            .deleted(DEFAULT_DELETED)
            .deletedAt(DEFAULT_DELETED_AT)
            .deletedBy(DEFAULT_DELETED_BY)
            .version(DEFAULT_VERSION)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT);
        return notificationEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationEntity createUpdatedEntity(EntityManager em) {
        NotificationEntity notificationEntity = new NotificationEntity()
            .subject(UPDATED_SUBJECT)
            .seen(UPDATED_SEEN)
            .deleted(UPDATED_DELETED)
            .deletedAt(UPDATED_DELETED_AT)
            .deletedBy(UPDATED_DELETED_BY)
            .version(UPDATED_VERSION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);
        return notificationEntity;
    }

    @BeforeEach
    public void initTest() {
        notificationEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createNotification() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();
        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notificationEntity);
        restNotificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Notification in the database
        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate + 1);
        NotificationEntity testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testNotification.getSeen()).isEqualTo(DEFAULT_SEEN);
        assertThat(testNotification.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testNotification.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
        assertThat(testNotification.getDeletedBy()).isEqualTo(DEFAULT_DELETED_BY);
        assertThat(testNotification.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testNotification.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testNotification.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testNotification.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testNotification.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createNotificationWithExistingId() throws Exception {
        // Create the Notification with an existing ID
        notificationEntity.setId(1L);
        NotificationDTO notificationDTO = notificationMapper.toDto(notificationEntity);

        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notificationEntity.setSubject(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notificationEntity);

        restNotificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeenIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notificationEntity.setSeen(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notificationEntity);

        restNotificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notificationEntity.setDeleted(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notificationEntity);

        restNotificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notificationEntity.setVersion(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notificationEntity);

        restNotificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notificationEntity.setCreatedBy(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notificationEntity);

        restNotificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notificationEntity.setCreatedAt(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notificationEntity);

        restNotificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotifications() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].seen").value(hasItem(DEFAULT_SEEN.booleanValue())))
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
    void getNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get the notification
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL_ID, notificationEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificationEntity.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.seen").value(DEFAULT_SEEN.booleanValue()))
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
    void getNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        Long id = notificationEntity.getId();

        defaultNotificationShouldBeFound("id.equals=" + id);
        defaultNotificationShouldNotBeFound("id.notEquals=" + id);

        defaultNotificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNotificationShouldNotBeFound("id.greaterThan=" + id);

        defaultNotificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNotificationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNotificationsBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where subject equals to DEFAULT_SUBJECT
        defaultNotificationShouldBeFound("subject.equals=" + DEFAULT_SUBJECT);

        // Get all the notificationList where subject equals to UPDATED_SUBJECT
        defaultNotificationShouldNotBeFound("subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllNotificationsBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where subject in DEFAULT_SUBJECT or UPDATED_SUBJECT
        defaultNotificationShouldBeFound("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT);

        // Get all the notificationList where subject equals to UPDATED_SUBJECT
        defaultNotificationShouldNotBeFound("subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllNotificationsBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where subject is not null
        defaultNotificationShouldBeFound("subject.specified=true");

        // Get all the notificationList where subject is null
        defaultNotificationShouldNotBeFound("subject.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsBySubjectContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where subject contains DEFAULT_SUBJECT
        defaultNotificationShouldBeFound("subject.contains=" + DEFAULT_SUBJECT);

        // Get all the notificationList where subject contains UPDATED_SUBJECT
        defaultNotificationShouldNotBeFound("subject.contains=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllNotificationsBySubjectNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where subject does not contain DEFAULT_SUBJECT
        defaultNotificationShouldNotBeFound("subject.doesNotContain=" + DEFAULT_SUBJECT);

        // Get all the notificationList where subject does not contain UPDATED_SUBJECT
        defaultNotificationShouldBeFound("subject.doesNotContain=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllNotificationsBySeenIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where seen equals to DEFAULT_SEEN
        defaultNotificationShouldBeFound("seen.equals=" + DEFAULT_SEEN);

        // Get all the notificationList where seen equals to UPDATED_SEEN
        defaultNotificationShouldNotBeFound("seen.equals=" + UPDATED_SEEN);
    }

    @Test
    @Transactional
    void getAllNotificationsBySeenIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where seen in DEFAULT_SEEN or UPDATED_SEEN
        defaultNotificationShouldBeFound("seen.in=" + DEFAULT_SEEN + "," + UPDATED_SEEN);

        // Get all the notificationList where seen equals to UPDATED_SEEN
        defaultNotificationShouldNotBeFound("seen.in=" + UPDATED_SEEN);
    }

    @Test
    @Transactional
    void getAllNotificationsBySeenIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where seen is not null
        defaultNotificationShouldBeFound("seen.specified=true");

        // Get all the notificationList where seen is null
        defaultNotificationShouldNotBeFound("seen.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where deleted equals to DEFAULT_DELETED
        defaultNotificationShouldBeFound("deleted.equals=" + DEFAULT_DELETED);

        // Get all the notificationList where deleted equals to UPDATED_DELETED
        defaultNotificationShouldNotBeFound("deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllNotificationsByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where deleted in DEFAULT_DELETED or UPDATED_DELETED
        defaultNotificationShouldBeFound("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED);

        // Get all the notificationList where deleted equals to UPDATED_DELETED
        defaultNotificationShouldNotBeFound("deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllNotificationsByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where deleted is not null
        defaultNotificationShouldBeFound("deleted.specified=true");

        // Get all the notificationList where deleted is null
        defaultNotificationShouldNotBeFound("deleted.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByDeletedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where deletedAt equals to DEFAULT_DELETED_AT
        defaultNotificationShouldBeFound("deletedAt.equals=" + DEFAULT_DELETED_AT);

        // Get all the notificationList where deletedAt equals to UPDATED_DELETED_AT
        defaultNotificationShouldNotBeFound("deletedAt.equals=" + UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    void getAllNotificationsByDeletedAtIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where deletedAt in DEFAULT_DELETED_AT or UPDATED_DELETED_AT
        defaultNotificationShouldBeFound("deletedAt.in=" + DEFAULT_DELETED_AT + "," + UPDATED_DELETED_AT);

        // Get all the notificationList where deletedAt equals to UPDATED_DELETED_AT
        defaultNotificationShouldNotBeFound("deletedAt.in=" + UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    void getAllNotificationsByDeletedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where deletedAt is not null
        defaultNotificationShouldBeFound("deletedAt.specified=true");

        // Get all the notificationList where deletedAt is null
        defaultNotificationShouldNotBeFound("deletedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByDeletedByIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where deletedBy equals to DEFAULT_DELETED_BY
        defaultNotificationShouldBeFound("deletedBy.equals=" + DEFAULT_DELETED_BY);

        // Get all the notificationList where deletedBy equals to UPDATED_DELETED_BY
        defaultNotificationShouldNotBeFound("deletedBy.equals=" + UPDATED_DELETED_BY);
    }

    @Test
    @Transactional
    void getAllNotificationsByDeletedByIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where deletedBy in DEFAULT_DELETED_BY or UPDATED_DELETED_BY
        defaultNotificationShouldBeFound("deletedBy.in=" + DEFAULT_DELETED_BY + "," + UPDATED_DELETED_BY);

        // Get all the notificationList where deletedBy equals to UPDATED_DELETED_BY
        defaultNotificationShouldNotBeFound("deletedBy.in=" + UPDATED_DELETED_BY);
    }

    @Test
    @Transactional
    void getAllNotificationsByDeletedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where deletedBy is not null
        defaultNotificationShouldBeFound("deletedBy.specified=true");

        // Get all the notificationList where deletedBy is null
        defaultNotificationShouldNotBeFound("deletedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByDeletedByContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where deletedBy contains DEFAULT_DELETED_BY
        defaultNotificationShouldBeFound("deletedBy.contains=" + DEFAULT_DELETED_BY);

        // Get all the notificationList where deletedBy contains UPDATED_DELETED_BY
        defaultNotificationShouldNotBeFound("deletedBy.contains=" + UPDATED_DELETED_BY);
    }

    @Test
    @Transactional
    void getAllNotificationsByDeletedByNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where deletedBy does not contain DEFAULT_DELETED_BY
        defaultNotificationShouldNotBeFound("deletedBy.doesNotContain=" + DEFAULT_DELETED_BY);

        // Get all the notificationList where deletedBy does not contain UPDATED_DELETED_BY
        defaultNotificationShouldBeFound("deletedBy.doesNotContain=" + UPDATED_DELETED_BY);
    }

    @Test
    @Transactional
    void getAllNotificationsByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where version equals to DEFAULT_VERSION
        defaultNotificationShouldBeFound("version.equals=" + DEFAULT_VERSION);

        // Get all the notificationList where version equals to UPDATED_VERSION
        defaultNotificationShouldNotBeFound("version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllNotificationsByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where version in DEFAULT_VERSION or UPDATED_VERSION
        defaultNotificationShouldBeFound("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION);

        // Get all the notificationList where version equals to UPDATED_VERSION
        defaultNotificationShouldNotBeFound("version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllNotificationsByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where version is not null
        defaultNotificationShouldBeFound("version.specified=true");

        // Get all the notificationList where version is null
        defaultNotificationShouldNotBeFound("version.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where version is greater than or equal to DEFAULT_VERSION
        defaultNotificationShouldBeFound("version.greaterThanOrEqual=" + DEFAULT_VERSION);

        // Get all the notificationList where version is greater than or equal to UPDATED_VERSION
        defaultNotificationShouldNotBeFound("version.greaterThanOrEqual=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllNotificationsByVersionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where version is less than or equal to DEFAULT_VERSION
        defaultNotificationShouldBeFound("version.lessThanOrEqual=" + DEFAULT_VERSION);

        // Get all the notificationList where version is less than or equal to SMALLER_VERSION
        defaultNotificationShouldNotBeFound("version.lessThanOrEqual=" + SMALLER_VERSION);
    }

    @Test
    @Transactional
    void getAllNotificationsByVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where version is less than DEFAULT_VERSION
        defaultNotificationShouldNotBeFound("version.lessThan=" + DEFAULT_VERSION);

        // Get all the notificationList where version is less than UPDATED_VERSION
        defaultNotificationShouldBeFound("version.lessThan=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllNotificationsByVersionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where version is greater than DEFAULT_VERSION
        defaultNotificationShouldNotBeFound("version.greaterThan=" + DEFAULT_VERSION);

        // Get all the notificationList where version is greater than SMALLER_VERSION
        defaultNotificationShouldBeFound("version.greaterThan=" + SMALLER_VERSION);
    }

    @Test
    @Transactional
    void getAllNotificationsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where createdBy equals to DEFAULT_CREATED_BY
        defaultNotificationShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the notificationList where createdBy equals to UPDATED_CREATED_BY
        defaultNotificationShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllNotificationsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultNotificationShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the notificationList where createdBy equals to UPDATED_CREATED_BY
        defaultNotificationShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllNotificationsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where createdBy is not null
        defaultNotificationShouldBeFound("createdBy.specified=true");

        // Get all the notificationList where createdBy is null
        defaultNotificationShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where createdBy contains DEFAULT_CREATED_BY
        defaultNotificationShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the notificationList where createdBy contains UPDATED_CREATED_BY
        defaultNotificationShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllNotificationsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where createdBy does not contain DEFAULT_CREATED_BY
        defaultNotificationShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the notificationList where createdBy does not contain UPDATED_CREATED_BY
        defaultNotificationShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllNotificationsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where createdAt equals to DEFAULT_CREATED_AT
        defaultNotificationShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the notificationList where createdAt equals to UPDATED_CREATED_AT
        defaultNotificationShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllNotificationsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultNotificationShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the notificationList where createdAt equals to UPDATED_CREATED_AT
        defaultNotificationShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllNotificationsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where createdAt is not null
        defaultNotificationShouldBeFound("createdAt.specified=true");

        // Get all the notificationList where createdAt is null
        defaultNotificationShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where updatedBy equals to DEFAULT_UPDATED_BY
        defaultNotificationShouldBeFound("updatedBy.equals=" + DEFAULT_UPDATED_BY);

        // Get all the notificationList where updatedBy equals to UPDATED_UPDATED_BY
        defaultNotificationShouldNotBeFound("updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllNotificationsByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where updatedBy in DEFAULT_UPDATED_BY or UPDATED_UPDATED_BY
        defaultNotificationShouldBeFound("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY);

        // Get all the notificationList where updatedBy equals to UPDATED_UPDATED_BY
        defaultNotificationShouldNotBeFound("updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllNotificationsByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where updatedBy is not null
        defaultNotificationShouldBeFound("updatedBy.specified=true");

        // Get all the notificationList where updatedBy is null
        defaultNotificationShouldNotBeFound("updatedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByUpdatedByContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where updatedBy contains DEFAULT_UPDATED_BY
        defaultNotificationShouldBeFound("updatedBy.contains=" + DEFAULT_UPDATED_BY);

        // Get all the notificationList where updatedBy contains UPDATED_UPDATED_BY
        defaultNotificationShouldNotBeFound("updatedBy.contains=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllNotificationsByUpdatedByNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where updatedBy does not contain DEFAULT_UPDATED_BY
        defaultNotificationShouldNotBeFound("updatedBy.doesNotContain=" + DEFAULT_UPDATED_BY);

        // Get all the notificationList where updatedBy does not contain UPDATED_UPDATED_BY
        defaultNotificationShouldBeFound("updatedBy.doesNotContain=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllNotificationsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultNotificationShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the notificationList where updatedAt equals to UPDATED_UPDATED_AT
        defaultNotificationShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllNotificationsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultNotificationShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the notificationList where updatedAt equals to UPDATED_UPDATED_AT
        defaultNotificationShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllNotificationsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        // Get all the notificationList where updatedAt is not null
        defaultNotificationShouldBeFound("updatedAt.specified=true");

        // Get all the notificationList where updatedAt is null
        defaultNotificationShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificationShouldBeFound(String filter) throws Exception {
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].seen").value(hasItem(DEFAULT_SEEN.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedBy").value(hasItem(DEFAULT_DELETED_BY)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificationShouldNotBeFound(String filter) throws Exception {
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification
        NotificationEntity updatedNotificationEntity = notificationRepository.findById(notificationEntity.getId()).get();
        // Disconnect from session so that the updates on updatedNotificationEntity are not directly saved in db
        em.detach(updatedNotificationEntity);
        updatedNotificationEntity
            .subject(UPDATED_SUBJECT)
            .seen(UPDATED_SEEN)
            .deleted(UPDATED_DELETED)
            .deletedAt(UPDATED_DELETED_AT)
            .deletedBy(UPDATED_DELETED_BY)
            .version(UPDATED_VERSION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);
        NotificationDTO notificationDTO = notificationMapper.toDto(updatedNotificationEntity);

        restNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Notification in the database
        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        NotificationEntity testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testNotification.getSeen()).isEqualTo(UPDATED_SEEN);
        assertThat(testNotification.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testNotification.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
        assertThat(testNotification.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testNotification.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testNotification.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testNotification.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testNotification.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testNotification.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notificationEntity.setId(count.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notificationEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notificationEntity.setId(count.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notificationEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notificationEntity.setId(count.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notificationEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notification in the database
        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificationWithPatch() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification using partial update
        NotificationEntity partialUpdatedNotificationEntity = new NotificationEntity();
        partialUpdatedNotificationEntity.setId(notificationEntity.getId());

        partialUpdatedNotificationEntity
            .deleted(UPDATED_DELETED)
            .deletedAt(UPDATED_DELETED_AT)
            .deletedBy(UPDATED_DELETED_BY)
            .version(UPDATED_VERSION)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotificationEntity))
            )
            .andExpect(status().isOk());

        // Validate the Notification in the database
        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        NotificationEntity testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testNotification.getSeen()).isEqualTo(DEFAULT_SEEN);
        assertThat(testNotification.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testNotification.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
        assertThat(testNotification.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testNotification.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testNotification.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testNotification.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testNotification.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testNotification.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateNotificationWithPatch() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification using partial update
        NotificationEntity partialUpdatedNotificationEntity = new NotificationEntity();
        partialUpdatedNotificationEntity.setId(notificationEntity.getId());

        partialUpdatedNotificationEntity
            .subject(UPDATED_SUBJECT)
            .seen(UPDATED_SEEN)
            .deleted(UPDATED_DELETED)
            .deletedAt(UPDATED_DELETED_AT)
            .deletedBy(UPDATED_DELETED_BY)
            .version(UPDATED_VERSION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotificationEntity))
            )
            .andExpect(status().isOk());

        // Validate the Notification in the database
        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        NotificationEntity testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testNotification.getSeen()).isEqualTo(UPDATED_SEEN);
        assertThat(testNotification.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testNotification.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
        assertThat(testNotification.getDeletedBy()).isEqualTo(UPDATED_DELETED_BY);
        assertThat(testNotification.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testNotification.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testNotification.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testNotification.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testNotification.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notificationEntity.setId(count.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notificationEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notificationEntity.setId(count.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notificationEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notificationEntity.setId(count.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notificationEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notification in the database
        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notificationEntity);

        int databaseSizeBeforeDelete = notificationRepository.findAll().size();

        // Delete the notification
        restNotificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificationEntity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NotificationEntity> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
