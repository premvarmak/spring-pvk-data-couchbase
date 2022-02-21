package com.nephos.cb;

import com.nephos.cb.dao.GamesSampleRepository;
import com.nephos.cb.model.GamesSample;
import com.nephos.cb.model.TravelSample;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.CouchbaseClientFactory;
import org.springframework.data.couchbase.SimpleCouchbaseClientFactory;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.convert.MappingCouchbaseConverter;
import org.springframework.data.couchbase.repository.config.RepositoryOperationsMapping;

@Configuration
public class NephosCb2Config extends NephosAbstractCouchbaseConfiguration {

    @Value("${couchbase2.bootstrap-hosts}")
    private String connectionString;
    @Value("${couchbase2.username}")
    private String userName;
    @Value("${couchbase2.password}")
    private String password;
    @Value("${couchbase2.bucket}")
    private String bucketName;

    @Override
    public String getConnectionString() {
        return connectionString;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }

    @Override
    public void nephosConfigureRepositoryOperationsMapping(RepositoryOperationsMapping baseMapping) {
        try {
            CouchbaseTemplate gamesTemplate = nephosCouchbaseTemplate(nephosCouchbaseClientFactory(bucketName),new MappingCouchbaseConverter());
            baseMapping.mapEntity(GamesSample.class,  gamesTemplate); // TravelSample mapped to "travel-sample" bucket
            baseMapping.map(GamesSampleRepository.class, gamesTemplate);
        } catch (Exception e) {
            throw e;
        }
    }

    public CouchbaseTemplate nephosCouchbaseTemplate(CouchbaseClientFactory couchbaseClientFactory,
                                                 MappingCouchbaseConverter mappingCouchbaseConverter) {
        return new CouchbaseTemplate(couchbaseClientFactory, mappingCouchbaseConverter);
    }

    public CouchbaseClientFactory nephosCouchbaseClientFactory(String bucketName) {
        return new SimpleCouchbaseClientFactory(getConnectionString(), authenticator(), bucketName );
    }
}
