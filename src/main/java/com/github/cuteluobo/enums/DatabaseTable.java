package com.github.cuteluobo.enums;

/**
 * @author CuteLuoBo
 * @date 2021-04-22
 */
public enum DatabaseTable {
    /**
     *
     */
    UNIT_YYS("unit_yys"),COMMAND_LIMIT("command_limit");
    private final String tableName;

    DatabaseTable(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
