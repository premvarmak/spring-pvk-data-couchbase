package com.nephos.cb;

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
public class NephosCbConfig extends AbstractCouchbaseConfiguration {

    @Value("${couchbase.bootstrap-hosts}")
    private String connectionString;
    @Value("${couchbase.username}")
    private String userName;
    @Value("${couchbase.password}")
    private String password;
    @Value("${couchbase.bucket}")
    private String bucketName;
    @Value("${couchbase.bucket2}")
    private String travelSampleBucket;

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

    /* Multi bucket configuration START */
    @Override
    public void configureRepositoryOperationsMapping(RepositoryOperationsMapping baseMapping) {
        try {
            CouchbaseTemplate travelSampleTemplate = myCouchbaseTemplate(myCouchbaseClientFactory(travelSampleBucket),new MappingCouchbaseConverter());
            baseMapping.mapEntity(TravelSample.class,  travelSampleTemplate); // TravelSample mapped to "travel-sample" bucket

        } catch (Exception e) {
            throw e;
        }
    }

    public CouchbaseTemplate myCouchbaseTemplate(CouchbaseClientFactory couchbaseClientFactory,
                                                 MappingCouchbaseConverter mappingCouchbaseConverter) {
        return new CouchbaseTemplate(couchbaseClientFactory, mappingCouchbaseConverter);
    }

    public CouchbaseClientFactory myCouchbaseClientFactory(String bucketName) {
        return new SimpleCouchbaseClientFactory(getConnectionString(), authenticator(), bucketName );
    }
    /* Multi bucket configuration END */
}
