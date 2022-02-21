package com.nephos.cb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TravelSample {

    @Id
    private String id;

    @Field("id")
    private int airlineId;

    @Field("type")
    private String airlineType;

    @Field("name")
    private String airlineName;

    @Field("callsign")
    private String callSign;

    @Field("country")
    private String country;

}
