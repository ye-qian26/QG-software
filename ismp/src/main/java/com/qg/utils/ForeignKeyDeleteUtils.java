package com.qg.utils;

import com.qg.domain.ForeignKeyInformation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Component
public class ForeignKeyDeleteUtils {

    @Autowired
    private DataSource dataSource;

    private Map<String, Set<ForeignKeyInformation>> foreignKeyMap;

    /**
     * 启动时扫描所有外键
     *
     * @throws SQLException
     */
    @PostConstruct
    public void init() throws SQLException {
        foreignKeyMap = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String catalog = connection.getCatalog(); // 获取当前数据库名

            // 获取数据库中所有表名
            List<String> tableNames = getAllTableNames(metaData, catalog);

            // 获取所有外键关系
            // 数据库名称、null不管、null指定所有表
            for (String tableName : tableNames) {
                ResultSet resultSet = metaData.getExportedKeys(connection.getCatalog(), null, tableName);
                while (resultSet.next()) {
                    String primaryKeyTable = resultSet.getString("PKTABLE_NAME");   // 获取主键表名
                    String foreignKeyTable = resultSet.getString("FKTABLE_NAME");   // 获取外键表名
                    String foreignKeyColumn = resultSet.getString("FKCOLUMN_NAME"); // 获取外键列名

                    ForeignKeyInformation foreignKeyInformation =
                            new ForeignKeyInformation(foreignKeyTable, foreignKeyColumn);

                    // 如果primaryKeyTable已作为key存在于map中：直接返回对应的value（HashSet）
                    foreignKeyMap.computeIfAbsent(primaryKeyTable,
                            // 如果不存在，new一个set丢进去
                            key -> new HashSet<>()).add(foreignKeyInformation);
                }
            }
        }
    }

    /**
     * 通过表名
     * 获取该表的关联外键
     *
     * @param tableName
     * @return
     */
    public Set<ForeignKeyInformation> getForeignKey(String tableName) {
        return foreignKeyMap.getOrDefault(tableName, Collections.emptySet());
    }

    /**
     * 获取数据库中所有表名
     *
     * @param metaData
     * @param catalog
     * @return
     * @throws SQLException
     */
    private List<String> getAllTableNames(DatabaseMetaData metaData, String catalog) throws SQLException {
        List<String> tableNames = new ArrayList<>();
        // 只查询表类型（排除视图等）
        try (ResultSet tables = metaData.getTables(
                catalog,  // 数据库名
                null,     // 模式名（MySQL通常为null）
                "%",      // 表名匹配模式
                new String[]{"TABLE"})) {  // 只查询表

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                tableNames.add(tableName);
            }
        }
        return tableNames;
    }


}