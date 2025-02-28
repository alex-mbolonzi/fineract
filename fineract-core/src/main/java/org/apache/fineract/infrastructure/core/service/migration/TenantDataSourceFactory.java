/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.infrastructure.core.service.migration;

import static org.apache.fineract.infrastructure.core.domain.FineractPlatformTenantConnection.toJdbcUrl;
import static org.apache.fineract.infrastructure.core.domain.FineractPlatformTenantConnection.toJdbcUrlGCP;
import static org.apache.fineract.infrastructure.core.domain.FineractPlatformTenantConnection.toProtocol;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.fineract.infrastructure.core.domain.FineractPlatformTenant;
import org.apache.fineract.infrastructure.core.domain.FineractPlatformTenantConnection;
import org.apache.fineract.infrastructure.core.service.database.DatabasePasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@Component
public class TenantDataSourceFactory {

    private static final Logger LOG = LoggerFactory.getLogger(TenantDataSourceFactory.class);

    private final HikariDataSource tenantDataSource;

    @Autowired
    ApplicationContext context;

    private final DatabasePasswordEncryptor databasePasswordEncryptor;

    @Autowired
    public TenantDataSourceFactory(@Qualifier("hikariTenantDataSource") HikariDataSource tenantDataSource,
            DatabasePasswordEncryptor databasePasswordEncryptor) {
        this.tenantDataSource = tenantDataSource;
        this.databasePasswordEncryptor = databasePasswordEncryptor;
    }

    public DataSource create(FineractPlatformTenant tenant) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(tenantDataSource.getDriverClassName());
        dataSource.setDataSourceProperties(tenantDataSource.getDataSourceProperties());
        dataSource.setMinimumIdle(tenantDataSource.getMinimumIdle());
        dataSource.setMaximumPoolSize(tenantDataSource.getMaximumPoolSize());
        dataSource.setIdleTimeout(tenantDataSource.getIdleTimeout());
        dataSource.setConnectionTestQuery(tenantDataSource.getConnectionTestQuery());

        FineractPlatformTenantConnection tenantConnection = tenant.getConnection();
        if (!databasePasswordEncryptor.isMasterPasswordHashValid(tenantConnection.getMasterPasswordHash())) {
            throw new IllegalArgumentException("Invalid master password");
        }
        dataSource.setUsername(tenantConnection.getSchemaUsername());
        dataSource.setPassword(databasePasswordEncryptor.decrypt(tenantConnection.getSchemaPassword()));
        String protocol = toProtocol(tenantDataSource);
//        String tenantJdbcUrl = toJdbcUrl(protocol, tenantConnection.getSchemaServer(), tenantConnection.getSchemaServerPort(),
//                tenantConnection.getSchemaName(), tenantConnection.getSchemaConnectionParameters());
        Environment environment = context.getEnvironment();

        String tenantJdbcUrl = toJdbcUrlGCP(protocol, tenantConnection.getSchemaName(),
                environment.getProperty("FINERACT_DEFAULT_TENANTDB_CONN_PARAMS"), tenantConnection.getSchemaServer());
        LOG.info("JDBC URL for tenant {} is {}", tenant.getTenantIdentifier(), tenantJdbcUrl);
        dataSource.setJdbcUrl(tenantJdbcUrl);
        return dataSource;
    }
}
