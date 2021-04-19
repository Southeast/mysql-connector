package com.larksuite.opensearch.connect;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class DataSource {
    private String id;
    private String name;
    private String description;
    private int state;
    private boolean exceed_quota;
    private String appid;
}
