package io.yody.notification.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MobileDevicesMapperTest {

    private MobileDevicesMapper mobileDevicesMapper;

    @BeforeEach
    public void setUp() {
        mobileDevicesMapper = new MobileDevicesMapperImpl();
    }
}
