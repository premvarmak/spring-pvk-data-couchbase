package com.nephos.cb;

import com.couchbase.client.core.deps.com.fasterxml.jackson.databind.DeserializationFeature;
import com.couchbase.client.core.encryption.CryptoManager;
import com.couchbase.client.core.env.Authenticator;
import com.couchbase.client.core.env.PasswordAuthenticator;
import com.couchbase.client.core.error.CouchbaseException;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.codec.JacksonJsonSerializer;
import com.couchbase.client.java.encryption.databind.jackson.EncryptionModule;
import com.couchbase.client.java.env.ClusterEnvironment;
import com.couchbase.client.java.env.ClusterEnvironment.Builder;
import com.couchbase.client.java.json.JacksonTransformers;
import com.couchbase.client.java.json.JsonValueModule;
import com.couchbase.client.java.query.QueryScanConsistency;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.couchbase.CouchbaseClientFactory;
import org.springframework.data.couchbase.SimpleCouchbaseClientFactory;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.ReactiveCouchbaseTemplate;
import org.springframework.data.couchbase.core.convert.CouchbaseCustomConversions;
import org.springframework.data.couchbase.core.convert.MappingCouchbaseConverter;
import org.springframework.data.couchbase.core.convert.translation.JacksonTranslationService;
import org.springframework.data.couchbase.core.convert.translation.TranslationService;
import org.springframework.data.couchbase.core.mapping.CouchbaseMappingContext;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.repository.config.ReactiveRepositoryOperationsMapping;
import org.springframework.data.couchbase.repository.config.RepositoryOperationsMapping;
import org.springframework.data.mapping.model.CamelCaseAbbreviatingFieldNamingStrategy;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.PropertyNameFieldNamingStrategy;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

@Configuration
public abstract class NephosAbstractCouchbaseConfiguration {
    public NephosAbstractCouchbaseConfiguration() {
    }

    public abstract String getConnectionString();

    public abstract String getUserName();

    public abstract String getPassword();

    public abstract String getBucketName();

    protected String getScopeName() {
        return null;
    }

    protected Authenticator authenticator() {
        return PasswordAuthenticator.create(this.getUserName(), this.getPassword());
    }

    @Bean
    public CouchbaseClientFactory nephosCouchbaseClientFactory(final Cluster couchbaseCluster) {
        return new SimpleCouchbaseClientFactory(couchbaseCluster, this.getBucketName(), this.getScopeName());
    }

    @Bean(
            destroyMethod = "disconnect"
    )
    public Cluster nephosCouchbaseCluster(ClusterEnvironment couchbaseClusterEnvironment) {
        return Cluster.connect(this.getConnectionString(), ClusterOptions.clusterOptions(this.authenticator()).environment(couchbaseClusterEnvironment));
    }

    @Bean(
            destroyMethod = "shutdown"
    )
    public ClusterEnvironment nephosCouchbaseClusterEnvironment() {
        Builder builder = ClusterEnvironment.builder();
        if (!this.nonShadowedJacksonPresent()) {
            throw new CouchbaseException("non-shadowed Jackson not present");
        } else {
            builder.jsonSerializer(JacksonJsonSerializer.create(this.couchbaseObjectMapper()));
            this.configureEnvironment(builder);
            return builder.build();
        }
    }

    protected void configureEnvironment(final Builder builder) {
    }

    @Bean(
            name = {"nephosCouchbaseTemplate"}
    )
    public CouchbaseTemplate nephosCouchbaseTemplate(CouchbaseClientFactory couchbaseClientFactory, MappingCouchbaseConverter mappingCouchbaseConverter, TranslationService couchbaseTranslationService) {
        return new CouchbaseTemplate(couchbaseClientFactory, mappingCouchbaseConverter, couchbaseTranslationService, this.getDefaultConsistency());
    }

    public CouchbaseTemplate couchbaseTemplate(CouchbaseClientFactory couchbaseClientFactory, MappingCouchbaseConverter mappingCouchbaseConverter) {
        return this.nephosCouchbaseTemplate(couchbaseClientFactory, mappingCouchbaseConverter, new JacksonTranslationService());
    }

    @Bean(
            name = {"nephosReactiveCouchbaseTemplate"}
    )
    public ReactiveCouchbaseTemplate nephosReactiveCouchbaseTemplate(CouchbaseClientFactory couchbaseClientFactory, MappingCouchbaseConverter mappingCouchbaseConverter, TranslationService couchbaseTranslationService) {
        return new ReactiveCouchbaseTemplate(couchbaseClientFactory, mappingCouchbaseConverter, couchbaseTranslationService, this.getDefaultConsistency());
    }

    public ReactiveCouchbaseTemplate reactiveCouchbaseTemplate(CouchbaseClientFactory couchbaseClientFactory, MappingCouchbaseConverter mappingCouchbaseConverter) {
        return this.nephosReactiveCouchbaseTemplate(couchbaseClientFactory, mappingCouchbaseConverter, new JacksonTranslationService());
    }

    @Bean(
            name = {"nephosCouchbaseRepositoryOperationsMapping"}
    )
    public RepositoryOperationsMapping nephosCouchbaseRepositoryOperationsMapping(CouchbaseTemplate couchbaseTemplate) {
        RepositoryOperationsMapping baseMapping = new RepositoryOperationsMapping(couchbaseTemplate);
        this.nephosConfigureRepositoryOperationsMapping(baseMapping);
        return baseMapping;
    }

    protected void nephosConfigureRepositoryOperationsMapping(RepositoryOperationsMapping mapping) {
    }

    @Bean(
            name = {"nephosReactiveCouchbaseRepositoryOperationsMapping"}
    )
    public ReactiveRepositoryOperationsMapping nephosReactiveCouchbaseRepositoryOperationsMapping(ReactiveCouchbaseTemplate reactiveCouchbaseTemplate) {
        ReactiveRepositoryOperationsMapping baseMapping = new ReactiveRepositoryOperationsMapping(reactiveCouchbaseTemplate);
        this.configureReactiveRepositoryOperationsMapping(baseMapping);
        return baseMapping;
    }

    protected void configureReactiveRepositoryOperationsMapping(ReactiveRepositoryOperationsMapping mapping) {
    }

    protected Set<Class<?>> getInitialEntitySet() throws ClassNotFoundException {
        String basePackage = this.getMappingBasePackage();
        Set<Class<?>> initialEntitySet = new HashSet();
        if (StringUtils.hasText(basePackage)) {
            ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false);
            componentProvider.addIncludeFilter(new AnnotationTypeFilter(Document.class));
            Iterator var4 = componentProvider.findCandidateComponents(basePackage).iterator();

            while(var4.hasNext()) {
                BeanDefinition candidate = (BeanDefinition)var4.next();
                initialEntitySet.add(ClassUtils.forName(candidate.getBeanClassName(), org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration.class.getClassLoader()));
            }
        }

        return initialEntitySet;
    }

    public String typeKey() {
        return "_class";
    }

    @Bean
    public MappingCouchbaseConverter nephosMappingCouchbaseConverter(CouchbaseMappingContext couchbaseMappingContext, CouchbaseCustomConversions couchbaseCustomConversions) {
        MappingCouchbaseConverter converter = new MappingCouchbaseConverter(couchbaseMappingContext, this.typeKey());
        converter.setCustomConversions(couchbaseCustomConversions);
        return converter;
    }

    @Bean
    public TranslationService nephosCouchbaseTranslationService() {
        JacksonTranslationService jacksonTranslationService = new JacksonTranslationService();
        jacksonTranslationService.afterPropertiesSet();
        JacksonTransformers.MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return jacksonTranslationService;
    }

    @Bean(
            name = {"nephosCouchbaseMappingContext"}
    )
    public CouchbaseMappingContext nephosCouchbaseMappingContext() throws Exception {
        CouchbaseMappingContext mappingContext = new CouchbaseMappingContext();
        mappingContext.setInitialEntitySet(this.getInitialEntitySet());
        mappingContext.setSimpleTypeHolder(nephosCouchbaseCustomConversions().getSimpleTypeHolder());
        mappingContext.setFieldNamingStrategy(this.fieldNamingStrategy());
        mappingContext.setAutoIndexCreation(this.autoIndexCreation());
        return mappingContext;
    }

    public ObjectMapper couchbaseObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JsonValueModule());
        CryptoManager cryptoManager = null;
        if (cryptoManager != null) {
            mapper.registerModule(new EncryptionModule((CryptoManager)cryptoManager));
        }

        return mapper;
    }

    protected boolean autoIndexCreation() {
        return false;
    }


    protected CustomConversions nephosCouchbaseCustomConversions() {
        return new CouchbaseCustomConversions(Collections.emptyList());
    }

    protected String getMappingBasePackage() {
        return this.getClass().getPackage().getName();
    }

    protected boolean abbreviateFieldNames() {
        return false;
    }

    protected FieldNamingStrategy fieldNamingStrategy() {
        return (FieldNamingStrategy)(this.abbreviateFieldNames() ? new CamelCaseAbbreviatingFieldNamingStrategy() : PropertyNameFieldNamingStrategy.INSTANCE);
    }

    private boolean nonShadowedJacksonPresent() {
        try {
            JacksonJsonSerializer.preflightCheck();
            return true;
        } catch (Throwable var2) {
            return false;
        }
    }

    public QueryScanConsistency getDefaultConsistency() {
        return null;
    }
}
