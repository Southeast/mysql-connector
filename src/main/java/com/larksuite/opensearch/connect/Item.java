package com.larksuite.opensearch.connect;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class Item {

    private String id;
    private ItemACL acl;
    // JSON String
    private String structuredData;
    private Content content;
    private String test;

    @Getter@Setter
    public static class Content {
        private String format;
        private String contentData;
    }

}

