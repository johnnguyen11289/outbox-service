package io.yody.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.notification.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationEntity.class);
        NotificationEntity notificationEntity1 = new NotificationEntity();
        notificationEntity1.setId(1L);
        NotificationEntity notificationEntity2 = new NotificationEntity();
        notificationEntity2.setId(notificationEntity1.getId());
        assertThat(notificationEntity1).isEqualTo(notificationEntity2);
        notificationEntity2.setId(2L);
        assertThat(notificationEntity1).isNotEqualTo(notificationEntity2);
        notificationEntity1.setId(null);
        assertThat(notificationEntity1).isNotEqualTo(notificationEntity2);
    }
}
