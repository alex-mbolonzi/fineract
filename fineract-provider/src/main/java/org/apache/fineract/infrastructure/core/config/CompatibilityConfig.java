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

package org.apache.fineract.infrastructure.core.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ConditionalOnExpression("#{ systemEnvironment['FINERACT_HIKARI_DRIVER_SOURCE_CLASS_NAME'] != null }")
@Deprecated // NOTE: this will be removed in one of the next releases (probably around version 1.7.x or 1.8.x)
public class CompatibilityConfig {

    private static final Logger LOG = LoggerFactory.getLogger(CompatibilityConfig.class);

    @Autowired
    ApplicationContext context;

    @PostConstruct
    public void init() {
        Environment environment = context.getEnvironment();

        LOG.warn("===============================================================================================\n");
        LOG.warn("You are using a deprecated tenant DB configuration:\n");
        LOG.warn("- Env var 'fineract_tenants_driver':                                  {}",
                environment.getProperty("fineract_tenants_driver"));
        LOG.warn("- Env var 'fineract_tenants_url':                                     {}",
                environment.getProperty("fineract_tenants_url"));
        LOG.warn("- Env var 'fineract_tenants_uid':                                     {}",
                environment.getProperty("fineract_tenants_uid"));
        LOG.warn("- Env var 'fineract_tenants_pwd':                                     ****\n");
        LOG.warn("The preferred way to configure the tenant DB is now via these environment variables:\n");
        LOG.warn("- Env var 'FINERACT_HIKARI_DRIVER_SOURCE_CLASS_NAME':                 {}",
                environment.getProperty("FINERACT_HIKARI_DRIVER_SOURCE_CLASS_NAME"));
        LOG.warn("- Env var 'FINERACT_HIKARI_JDBC_URL':                                 {}",
                environment.getProperty("FINERACT_HIKARI_JDBC_URL"));
        LOG.warn("- Env var 'FINERACT_HIKARI_USERNAME':                                 {}",
                environment.getProperty("FINERACT_HIKARI_USERNAME"));
        LOG.warn("- Env var 'FINERACT_HIKARI_PASSWORD':                                 ****");
        LOG.warn("- Env var 'FINERACT_HIKARI_MINIMUM_IDLE':                             {}",
                environment.getProperty("FINERACT_HIKARI_MINIMUM_IDLE"));
        LOG.warn("- Env var 'FINERACT_HIKARI_MAXIMUM_POOL_SIZE':                        {}",
                environment.getProperty("FINERACT_HIKARI_MAXIMUM_POOL_SIZE"));
        LOG.warn("- Env var 'FINERACT_HIKARI_IDLE_TIMEOUT':                             {}",
                environment.getProperty("FINERACT_HIKARI_IDLE_TIMEOUT"));
        LOG.warn("- Env var 'FINERACT_HIKARI_CONNECTION_TIMEOUT':                       {}",
                environment.getProperty("FINERACT_HIKARI_CONNECTION_TIMEOUT"));
        LOG.warn("- Env var 'FINERACT_HIKARI_TEST_QUERY':                               {}",
                environment.getProperty("FINERACT_HIKARI_TEST_QUERY"));
        LOG.warn("- Env var 'FINERACT_HIKARI_AUTO_COMMIT':                              {}",
                environment.getProperty("FINERACT_HIKARI_AUTO_COMMIT"));
        LOG.warn("- Env var 'FINERACT_HIKARI_DS_PROPERTIES_CACHE_PREP_STMTS':           {}",
                environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_CACHE_PREP_STMTS"));
        LOG.warn("- Env var 'FINERACT_HIKARI_DS_PROPERTIES_PREP_STMT_CACHE_SIZE':       {}",
                environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_PREP_STMT_CACHE_SIZE"));
        LOG.warn("- Env var 'FINERACT_HIKARI_DS_PROPERTIES_PREP_STMT_CACHE_SQL_LIMIT':  {}",
                environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_PREP_STMT_CACHE_SQL_LIMIT"));
        LOG.warn("- Env var 'FINERACT_HIKARI_DS_PROPERTIES_USE_SERVER_PREP_STMTS':      {}",
                environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_USE_SERVER_PREP_STMTS"));
        LOG.warn("- Env var 'FINERACT_HIKARI_DS_PROPERTIES_USE_LOCAL_SESSION_STATE':    {}",
                environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_USE_LOCAL_SESSION_STATE"));
        LOG.warn("- Env var 'FINERACT_HIKARI_DS_PROPERTIES_REWRITE_BATCHED_STATEMENTS': {}",
                environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_REWRITE_BATCHED_STATEMENTS"));
        LOG.warn("- Env var 'FINERACT_HIKARI_DS_PROPERTIES_CACHE_RESULT_SET_METADATA':  {}",
                environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_CACHE_RESULT_SET_METADATA"));
        LOG.warn("- Env var 'FINERACT_HIKARI_DS_PROPERTIES_CACHE_SERVER_CONFIGURATION': {}",
                environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_CACHE_SERVER_CONFIGURATION"));
        LOG.warn("- Env var 'FINERACT_HIKARI_DS_PROPERTIES_ELIDE_SET_AUTO_COMMITS':     {}",
                environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_ELIDE_SET_AUTO_COMMITS"));
        LOG.warn("- Env var 'FINERACT_HIKARI_DS_PROPERTIES_MAINTAIN_TIME_STATS':        {}",
                environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_MAINTAIN_TIME_STATS"));
        LOG.warn("- Env var 'FINERACT_HIKARI_DS_PROPERTIES_LOG_SLOW_QUERIES':           {}",
                environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_LOG_SLOW_QUERIES"));
        LOG.warn("- Env var 'FINERACT_HIKARI_DS_PROPERTIES_DUMP_QUERIES_IN_EXCEPTION':  {}",
                environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_DUMP_QUERIES_IN_EXCEPTION"));
        LOG.warn("- Env var 'FINERACT_HIKARI_DS_PROPERTIES_INSTANCE_CONNECTION_NAME':  {}",
                environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_INSTANCE_CONNECTION_NAME"));
        LOG.warn("===============================================================================================\n");
    }

    @Bean(destroyMethod = "close")
    public HikariDataSource hikariTenantDataSource(HikariConfig hc) {
        return new HikariDataSource(hc);
    }

    @Bean
    public HikariConfig hikariConfig() {
        Environment environment = context.getEnvironment();
        HikariConfig hc = new HikariConfig();

        hc.setDriverClassName(environment.getProperty("FINERACT_HIKARI_DRIVER_SOURCE_CLASS_NAME"));
        hc.setJdbcUrl(environment.getProperty("FINERACT_HIKARI_JDBC_URL"));
        hc.setUsername(environment.getProperty("FINERACT_HIKARI_USERNAME"));
        hc.setPassword(environment.getProperty("FINERACT_HIKARI_PASSWORD"));
        hc.setMinimumIdle(Integer.parseInt(environment.getProperty("FINERACT_HIKARI_MINIMUM_IDLE")));
        hc.setMaximumPoolSize(Integer.parseInt(environment.getProperty("FINERACT_HIKARI_MAXIMUM_POOL_SIZE")));
        hc.setIdleTimeout(Long.parseLong(environment.getProperty("FINERACT_HIKARI_IDLE_TIMEOUT")));
        hc.setConnectionTestQuery(environment.getProperty("FINERACT_HIKARI_TEST_QUERY"));
        hc.setDataSourceProperties(dataSourceProperties());

        return hc;
    }

    // These are the properties for the all Tenants DB; the same configuration is also (hard-coded) in the
    // TomcatJdbcDataSourcePerTenantService class -->
    private Properties dataSourceProperties() {
        Environment environment = context.getEnvironment();

        Properties props = new Properties();

        props.setProperty("cachePrepStmts", environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_CACHE_PREP_STMTS"));
        props.setProperty("prepStmtCacheSize", environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_PREP_STMT_CACHE_SIZE"));
        props.setProperty("prepStmtCacheSqlLimit", environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_PREP_STMT_CACHE_SQL_LIMIT"));
        props.setProperty("useServerPrepStmts", environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_USE_SERVER_PREP_STMTS"));
        props.setProperty("useLocalSessionState", environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_USE_LOCAL_SESSION_STATE"));
        props.setProperty("rewriteBatchedStatements", environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_REWRITE_BATCHED_STATEMENTS"));
        props.setProperty("cacheResultSetMetadata", environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_CACHE_RESULT_SET_METADATA"));
        props.setProperty("cacheServerConfiguration", environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_CACHE_SERVER_CONFIGURATION"));
        props.setProperty("elideSetAutoCommits", environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_ELIDE_SET_AUTO_COMMITS"));
        props.setProperty("maintainTimeStats", environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_MAINTAIN_TIME_STATS"));

        // gcp sql INSTANCE_CONNECTION_NAME
//        if (environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_INSTANCE_CONNECTION_NAME") != null) {
        props.setProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
        props.setProperty("cloudSqlInstance", environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_INSTANCE_CONNECTION_NAME"));
        props.setProperty("ipTypes", "PUBLIC");
//        }

        // https://github.com/brettwooldridge/HikariCP/wiki/JDBC-Logging#mysql-connectorj
        // TODO FINERACT-890: <prop key="logger">com.mysql.cj.log.Slf4JLogger</prop>
        props.setProperty("logSlowQueries", environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_LOG_SLOW_QUERIES"));
        props.setProperty("dumpQueriesOnException", environment.getProperty("FINERACT_HIKARI_DS_PROPERTIES_DUMP_QUERIES_IN_EXCEPTION"));

        return props;
    }
}
