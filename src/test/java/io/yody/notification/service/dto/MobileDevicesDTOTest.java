package io.yody.notification.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.notification.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MobileDevicesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MobileDevicesDTO.class);
        MobileDevicesDTO mobileDevicesDTO1 = new MobileDevicesDTO();
        mobileDevicesDTO1.setId(1L);
        MobileDevicesDTO mobileDevicesDTO2 = new MobileDevicesDTO();
        assertThat(mobileDevicesDTO1).isNotEqualTo(mobileDevicesDTO2);
        mobileDevicesDTO2.setId(mobileDevicesDTO1.getId());
        assertThat(mobileDevicesDTO1).isEqualTo(mobileDevicesDTO2);
        mobileDevicesDTO2.setId(2L);
        assertThat(mobileDevicesDTO1).isNotEqualTo(mobileDevicesDTO2);
        mobileDevicesDTO1.setId(null);
        assertThat(mobileDevicesDTO1).isNotEqualTo(mobileDevicesDTO2);
    }
}
