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
public class BeerSample {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("city")
    private String city;

    @Field("state")
    private String state;

    @Field("code")
    private String code;
}
