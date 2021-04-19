package com.larksuite.opensearch.connect;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter@Setter
public class ItemACL {

    private String allowedUsers;
    private String deniedUsers;
    private String allowedGroups;
    private String deniedGroups;

}
