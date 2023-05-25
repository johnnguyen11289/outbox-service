package io.yody.notification.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link io.yody.notification.domain.MobileDevicesEntity} entity. This class is used
 * in {@link io.yody.notification.web.rest.MobileDevicesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /mobile-devices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MobileDevicesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter deviceId;

    private StringFilter deviceToken;

    private BooleanFilter deleted;

    private InstantFilter deletedAt;

    private StringFilter deletedBy;

    private IntegerFilter version;

    private StringFilter createdBy;

    private InstantFilter createdAt;

    private StringFilter updatedBy;

    private InstantFilter updatedAt;

    private Boolean distinct;

    public MobileDevicesCriteria() {}

    public MobileDevicesCriteria(MobileDevicesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.deviceId = other.deviceId == null ? null : other.deviceId.copy();
        this.deviceToken = other.deviceToken == null ? null : other.deviceToken.copy();
        this.deleted = other.deleted == null ? null : other.deleted.copy();
        this.deletedAt = other.deletedAt == null ? null : other.deletedAt.copy();
        this.deletedBy = other.deletedBy == null ? null : other.deletedBy.copy();
        this.version = other.version == null ? null : other.version.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedBy = other.updatedBy == null ? null : other.updatedBy.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MobileDevicesCriteria copy() {
        return new MobileDevicesCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDeviceId() {
        return deviceId;
    }

    public StringFilter deviceId() {
        if (deviceId == null) {
            deviceId = new StringFilter();
        }
        return deviceId;
    }

    public void setDeviceId(StringFilter deviceId) {
        this.deviceId = deviceId;
    }

    public StringFilter getDeviceToken() {
        return deviceToken;
    }

    public StringFilter deviceToken() {
        if (deviceToken == null) {
            deviceToken = new StringFilter();
        }
        return deviceToken;
    }

    public void setDeviceToken(StringFilter deviceToken) {
        this.deviceToken = deviceToken;
    }

    public BooleanFilter getDeleted() {
        return deleted;
    }

    public BooleanFilter deleted() {
        if (deleted == null) {
            deleted = new BooleanFilter();
        }
        return deleted;
    }

    public void setDeleted(BooleanFilter deleted) {
        this.deleted = deleted;
    }

    public InstantFilter getDeletedAt() {
        return deletedAt;
    }

    public InstantFilter deletedAt() {
        if (deletedAt == null) {
            deletedAt = new InstantFilter();
        }
        return deletedAt;
    }

    public void setDeletedAt(InstantFilter deletedAt) {
        this.deletedAt = deletedAt;
    }

    public StringFilter getDeletedBy() {
        return deletedBy;
    }

    public StringFilter deletedBy() {
        if (deletedBy == null) {
            deletedBy = new StringFilter();
        }
        return deletedBy;
    }

    public void setDeletedBy(StringFilter deletedBy) {
        this.deletedBy = deletedBy;
    }

    public IntegerFilter getVersion() {
        return version;
    }

    public IntegerFilter version() {
        if (version == null) {
            version = new IntegerFilter();
        }
        return version;
    }

    public void setVersion(IntegerFilter version) {
        this.version = version;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            createdBy = new StringFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            createdAt = new InstantFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public StringFilter getUpdatedBy() {
        return updatedBy;
    }

    public StringFilter updatedBy() {
        if (updatedBy == null) {
            updatedBy = new StringFilter();
        }
        return updatedBy;
    }

    public void setUpdatedBy(StringFilter updatedBy) {
        this.updatedBy = updatedBy;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new InstantFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MobileDevicesCriteria that = (MobileDevicesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(deviceId, that.deviceId) &&
            Objects.equals(deviceToken, that.deviceToken) &&
            Objects.equals(deleted, that.deleted) &&
            Objects.equals(deletedAt, that.deletedAt) &&
            Objects.equals(deletedBy, that.deletedBy) &&
            Objects.equals(version, that.version) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            deviceId,
            deviceToken,
            deleted,
            deletedAt,
            deletedBy,
            version,
            createdBy,
            createdAt,
            updatedBy,
            updatedAt,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MobileDevicesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (deviceId != null ? "deviceId=" + deviceId + ", " : "") +
            (deviceToken != null ? "deviceToken=" + deviceToken + ", " : "") +
            (deleted != null ? "deleted=" + deleted + ", " : "") +
            (deletedAt != null ? "deletedAt=" + deletedAt + ", " : "") +
            (deletedBy != null ? "deletedBy=" + deletedBy + ", " : "") +
            (version != null ? "version=" + version + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedBy != null ? "updatedBy=" + updatedBy + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
