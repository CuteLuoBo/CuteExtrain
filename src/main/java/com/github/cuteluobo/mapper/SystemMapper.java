package com.github.cuteluobo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author CuteLuoBo
 * @date 2021-04-22
 */
@Mapper
public interface SystemMapper {
    /**
     * 查询指定表名是否存在
     * 参考 https://blog.csdn.net/p15097962069/article/details/103578662 #22楼
     * @param tableName 表名
     * @return 是否存在
     */
    @Select("SELECT count(*) FROM sqlite_master WHERE type='table' AND name = #{tableName};")
    public Boolean existTable(String tableName);
}
