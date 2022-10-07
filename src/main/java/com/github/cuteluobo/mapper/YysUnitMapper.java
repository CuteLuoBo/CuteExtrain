package com.github.cuteluobo.mapper;

import com.github.cuteluobo.model.YysUnit;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
*@author CuteLuoBo
*@date 2021-04-08
*/
@Mapper
public interface YysUnitMapper {

    /**
     * 新增记录
     *TODO 对于xml配置无法生效，只能用注解模式，待排查
     * @param yysUnit 新记录数据
     * @return 变更数量
     */
    @Insert("INSERT INTO unit_yys (unit_id,level,name,special_name,can_roll) VALUES (#{unitId},#{level},#{name},#{specialName},#{canRoll})")
    @Options(useGeneratedKeys = true,keyColumn = "id")
    public int addOne(YysUnit yysUnit);

    @Insert({"<script>",
            "INSERT INTO unit_yys (unit_id,level,name,special_name,can_roll) VALUES ",
            "<foreach item='item' collection='list' separator=','>",
            "(#{item.unitId},#{item.level},#{item.name},#{item.specialName},#{item.canRoll})",
            "</foreach>",
            "</script>"})
    int addAll(List<YysUnit> list);

    /**
     * 查询所有可用式神列表
     * @return 查询列表
     */
    @Select("SELECT * FROM unit_yys")
    public List<YysUnit> selectList();
    /**
     * 查询式神列表
     * @param canRoll 筛选可抽取
     * @return 查询列表
     */
    @Select("SELECT * FROM unit_yys WHERE can_roll = #{arg0}")
    public List<YysUnit> selectListByCanRoll(Boolean canRoll);

    /**
     * 查询式神
     * @param name    式神名称
     * @param canRoll 筛选可抽取
     * @return 查询列表
     */
    @Select("SELECT * FROM unit_yys WHERE name Like #{arg0}||'%' AND can_roll = #{arg1} LIMIT 1")
    public YysUnit selectOneByName(String name,Boolean canRoll);

    /**
     * 查询式神
     * @param id    式神ID
     * @return 查询列表
     */
    @Select("SELECT * FROM unit_yys WHERE unit_id = #{arg1}")
    public YysUnit selectOneByUnitId(Integer id);
}