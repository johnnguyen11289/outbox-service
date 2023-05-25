package io.yody.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.notification.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MobileDevicesEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MobileDevicesEntity.class);
        MobileDevicesEntity mobileDevicesEntity1 = new MobileDevicesEntity();
        mobileDevicesEntity1.setId(1L);
        MobileDevicesEntity mobileDevicesEntity2 = new MobileDevicesEntity();
        mobileDevicesEntity2.setId(mobileDevicesEntity1.getId());
        assertThat(mobileDevicesEntity1).isEqualTo(mobileDevicesEntity2);
        mobileDevicesEntity2.setId(2L);
        assertThat(mobileDevicesEntity1).isNotEqualTo(mobileDevicesEntity2);
        mobileDevicesEntity1.setId(null);
        assertThat(mobileDevicesEntity1).isNotEqualTo(mobileDevicesEntity2);
    }
}
