package com.wjusite.boot.dubbox;

import com.alibaba.dubbo.config.*;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Kola 6089555
 * @ClassName: DubboxAutoConfiguration
 * @Description: Dubbox自动配置
 * @date 2017年4月19日 下午3:24:12
 */
@Configuration
@EnableConfigurationProperties(DubboxProperties.class)
public class DubboxAutoConfiguration implements EnvironmentAware {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DubboxAutoConfiguration.class);
    
    @Autowired
    DubboxProperties dubboxProperties;
    
    @Bean
    public static AnnotationBean annotationBean(@Value("${dubbo.annotation.package:#{null}}") String packageName) {
        AnnotationBean annotationBean = new AnnotationBean();
        if (!StringUtils.isEmpty(packageName)) {
            annotationBean.setPackage(packageName);
            LOGGER.info("DubboxAutoConfiguration.annotationBean ---> {}", packageName);
        } else {
            LOGGER.error("请设置'dubbo.annotation.package'值,扫描包路径！");
        }
        return annotationBean;
    }
    
    @Bean
    public ServiceConfig<?> serviceConfig(ApplicationConfig applicationConfig, ProviderConfig providerConfig, ProtocolConfig protocolConfig, RegistryConfig registryConfig) {
        ServiceConfig<?> serviceConfig = dubboxProperties.getService();
        serviceConfig.setApplication(applicationConfig);
        serviceConfig.setProvider(providerConfig);
        serviceConfig.setProtocol(protocolConfig);
        serviceConfig.setRegistry(registryConfig);
        return serviceConfig;
    }
    
    @Bean
    public ReferenceConfig<?> referenceConfig() {
        return dubboxProperties.getReference();
    }
    
    @Bean
    public ProtocolConfig protocolConfig() {
        return dubboxProperties.getProtocol();
    }
    
    @Bean
    public ApplicationConfig applicationConfig() {
        return dubboxProperties.getApplication();
    }
    
    @Bean
    public ModuleConfig moduleConfig(ApplicationConfig applicationConfig) {
        ModuleConfig moduleConfig = dubboxProperties.getModule();
        if (null == moduleConfig.getName()) {
            moduleConfig.setName(applicationConfig.getName());
        }
        return moduleConfig;
    }
    
    @Bean
    public RegistryConfig registry() {
        return dubboxProperties.getRegistry();
    }
    
    @Bean
    public MonitorConfig monitorConfig() {
        return dubboxProperties.getMonitor();
    }
    
    @Bean
    public ProviderConfig providerConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig, ProtocolConfig protocolConfig, MonitorConfig monitorConfig, ModuleConfig moduleConfig) {
        ProviderConfig providerConfig = dubboxProperties.getProvider();
        providerConfig.setApplication(applicationConfig);
        providerConfig.setRegistry(registryConfig);
        providerConfig.setProtocol(protocolConfig);
        if (monitorConfig != null) {
            providerConfig.setMonitor(monitorConfig);
        }
        if (moduleConfig != null) {
            providerConfig.setModule(moduleConfig);
        }
        return providerConfig;
    }
    
    @Bean
    public MethodConfig methodConfig() {
        return dubboxProperties.getMethod();
    }
    
    @Override
    public void setEnvironment(Environment environment) {
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment);
        Map<String, Object> subProperties = propertyResolver.getSubProperties("dubbo.");
        if (subProperties != null) {
            LOGGER.debug("DubboxAutoConfiguration fill systemProperty start");
            for (Entry<String, Object> entry : subProperties.entrySet()) {
                if (entry != null && !StringUtils.isEmpty(entry.getKey()) && !StringUtils.isEmpty(entry.getValue())) {
                    System.setProperty("dubbo.".concat(entry.getKey()), entry.getValue().toString());
                }
            }
            LOGGER.debug("DubboxAutoConfiguration fill systemProperty end");
        }
    }
}
