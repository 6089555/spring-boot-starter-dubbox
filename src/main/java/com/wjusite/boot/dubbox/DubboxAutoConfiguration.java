package com.wjusite.boot.dubbox;

import com.alibaba.dubbo.config.*;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author Kola 6089555
 * @ClassName: DubboxAutoConfiguration
 * @Description: Dubbox自动配置
 * @date 2017年4月19日 下午3:24:12
 */
@Configuration
@ConditionalOnClass(RegistryConfig.class)
public class DubboxAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DubboxAutoConfiguration.class);

    @Bean
    public static AnnotationBean annotationBean(@Value("${dubbox.annotation.package:#{null}}") String packageName) {
        AnnotationBean annotationBean = new AnnotationBean();
        if (!StringUtils.isEmpty(packageName)) {
            annotationBean.setPackage(packageName);
            LOGGER.info("DubboxAutoConfiguration.annotationBean ---> {}", packageName);
        } else {
            LOGGER.error("请设置'dubbox.annotation.package'值,扫描包路径！");
        }
        return annotationBean;
    }

    @Bean
    @ConfigurationProperties(prefix = "dubbox.service")
    public ServiceConfig<?> serviceConfig() {
        return new ServiceConfig<>();
    }

    @Bean
    @ConfigurationProperties(prefix = "dubbox.reference")
    public ReferenceConfig<?> referenceConfig() {
        return new ReferenceConfig<>();
    }

    @Bean
    @ConfigurationProperties(prefix = "dubbox.protocol")
    public ProtocolConfig protocolConfig() {
        return new ProtocolConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "dubbox.application")
    public ApplicationConfig applicationConfig() {
        return new ApplicationConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "dubbox.module")
    public ModuleConfig moduleConfig(ApplicationConfig applicationConfig) {
        ModuleConfig moduleConfig = new ModuleConfig();
        if (null == moduleConfig.getName()) {
            moduleConfig.setName(applicationConfig.getName());
        }
        return moduleConfig;
    }

    @Bean
    @ConfigurationProperties(prefix = "dubbox.registry")
    public RegistryConfig registry() {
        return new RegistryConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "dubbox.monitor")
    public MonitorConfig monitorConfig() {
        return new MonitorConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "dubbox.provider")
    public ProviderConfig providerConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig, ProtocolConfig protocolConfig) {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setApplication(applicationConfig);
        providerConfig.setRegistry(registryConfig);
        providerConfig.setProtocol(protocolConfig);
        LOGGER.info("DubboxAutoConfiguration.providerConfig ---> {}", providerConfig);
        return providerConfig;
    }

    @Bean
    @ConfigurationProperties(prefix = "dubbox.method")
    public MethodConfig methodConfig() {
        return new MethodConfig();
    }

}
