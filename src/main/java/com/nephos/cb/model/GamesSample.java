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
public class GamesSample {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("uuid")
    private String uniqueId;

    @Field("loggedIn")
    private boolean loggedIn;

    @Field("level")
    private int level;
}
