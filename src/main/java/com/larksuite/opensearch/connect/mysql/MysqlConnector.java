package com.larksuite.opensearch.connect.mysql;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.larksuite.opensearch.connect.Item;
import com.larksuite.opensearch.connect.ItemACL;
import com.larksuite.opensearch.connect.OapiService;
import lombok.Getter;
import lombok.Setter;

import java.sql.*;
import java.util.List;

@Getter
@Setter
public class MysqlConnector {

    private String host;
    private String port;
    private String dbName;
    private String user;
    private String password;
    private String dbUrl;

    private String entitySql;
    private String aclSql;
    private List<String> entityColumns;
    private String identifierColumn;

    private OapiService oapiService;

    public void Init() throws ClassNotFoundException {
        dbUrl = System.out.printf("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", host, port, dbName).toString();
        Class.forName("com.mysql.jdbc.Driver");
    }

    /**
     * 全量同步
     */
    public void fullSync() {

        ResultSet rs = null;
        List<Item> items = Lists.newArrayList();
        try {
            Connection conn = DriverManager.getConnection(dbUrl, user, password);

            //查询并组装item
            // TODO 分页查询
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(entitySql);
            while (rs.next()) {
                Item item = new Item();
                String id = rs.getString(identifierColumn);
                item.setId(id);
                String description = rs.getString("description");
                String location = rs.getString("location");
                String manager = rs.getString("manager");
                Gson gson = new Gson();
                String itemJSON = gson.toJson(item);
                item.setStructuredData(itemJSON);
                Item.Content content = new Item.Content();
                //这里的contentData可能比structuredData有更多信息
                content.setContentData(itemJSON);
                content.setFormat("test");
                item.setContent(content);
                items.add(item);
            }

            // 查询并设置acl
            for (Item item : items) {
                try {
                    PreparedStatement stmtAcl = conn.prepareStatement(aclSql);
                    stmtAcl.setString(1, item.getId());
                    String allowedUsers = rs.getString("acl");
                    ItemACL acl = new ItemACL();
                    acl.setAllowedUsers(allowedUsers);
                    item.setAcl(acl);
                    if (stmtAcl != null) {
                        stmtAcl.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // 调用开放接口集成数据
        for (Item item : items) {
            oapiService.CreateItem(item);
        }

    }

    @Setter
    @Getter
    public static class WumeiStoreSiteInfo {
        private String id;
        private String location;
        private String description;
    }

}
