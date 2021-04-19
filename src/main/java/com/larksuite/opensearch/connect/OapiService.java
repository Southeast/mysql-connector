package com.larksuite.opensearch.connect;

import com.larksuite.oapi.core.AppSettings;
import com.larksuite.oapi.core.Config;
import com.larksuite.oapi.core.DefaultStore;
import com.larksuite.oapi.core.Domain;
import com.larksuite.oapi.core.api.AccessTokenType;
import com.larksuite.oapi.core.api.Api;
import com.larksuite.oapi.core.api.request.Request;
import com.larksuite.oapi.core.api.response.Response;
import com.larksuite.oapi.core.utils.Jsons;

import java.util.HashMap;
import java.util.Map;

public class OapiService {

    private static final String CREATE_DATASOURCE_PATH = "search/v1/datasource/create";
    private static final String INTEGRATE_ITEM_PATH = "search/v1/datasource/%s/item/create";

    private String appId;
    private String appSecret;
    private String verificationToken;
    private String encryptKey;

    private AppSettings appSettings;
    private Config config;

    public void init() {
        appSettings = Config.createInternalAppSettings(appId, appSecret, verificationToken, encryptKey);
        config = new Config(Domain.LarkSuite, appSettings, new DefaultStore());
    }

    public String CreateDatasource(DataSource dataSource) {
        // Build request
        Request<DataSource, Map<String, Object>> request = Request.newRequest(CREATE_DATASOURCE_PATH,
                "POST", AccessTokenType.Tenant, dataSource, new HashMap<>());

        String dataSourceId = "";
        // Send request && response of the request = http response body json
        Response<Map<String, Object>> response = null;
        try {
            response = Api.send(config, request);
            dataSourceId = String.valueOf(response.getData().get("data_source_id"));
            // Print the requestId of the request
            System.out.println(response.getRequestID());
            // Print the response status of the request
            System.out.println(response.getHTTPStatusCode());
            // Print the result of the request
            System.out.println(Jsons.DEFAULT_GSON.toJson(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSourceId;
    }

    public String CreateItem(Item item) {
        // Build request
        Request<Item, Map<String, Object>> request = Request.newRequest(CREATE_DATASOURCE_PATH,
                "POST", AccessTokenType.Tenant, item, new HashMap<>());

        String itemId = "";
        // Send request && response of the request = http response body json
        Response<Map<String, Object>> response = null;
        try {
            response = Api.send(config, request);
            itemId = String.valueOf(response.getData().get("id"));
            // Print the requestId of the request
            System.out.println(response.getRequestID());
            // Print the response status of the request
            System.out.println(response.getHTTPStatusCode());
            // Print the result of the request
            System.out.println(Jsons.DEFAULT_GSON.toJson(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemId;
    }


}
