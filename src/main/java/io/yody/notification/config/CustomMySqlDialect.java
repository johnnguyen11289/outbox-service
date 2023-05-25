package io.yody.notification.config;

import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

@SuppressWarnings("unused")
public class CustomMySqlDialect extends MySQL8Dialect {

    public CustomMySqlDialect() {
        super();
        // jhipster-needle-register-function-to-mysql-dialect - JHipster will register function to mysql dialect here
    }
}
