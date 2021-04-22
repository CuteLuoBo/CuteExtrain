package com.github.cuteluobo.mapper;

import com.github.cuteluobo.model.YysUnit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
*@author CuteLuoBo
*@date 2021-04-08
*/
@Mapper
public interface YysUnitMapper {
    /**
     * 查询式神列表
     * @param canRoll 筛选可抽取
     * @return 查询列表
     */
    @Select("SELECT * FROM unit_yys WHERE can_roll = #{canRoll}")
    public List<YysUnit> selectList(Boolean canRoll);

    /**
     * 查询式神
     * @param name    式神名称
     * @param canRoll 筛选可抽取
     * @return 查询列表
     */
    @Select("SELECT * FROM unit_yys WHERE name Like CONCAT(name,'%') AND can_roll = #{canRoll} LIMIT 1")
    public YysUnit selectOneByName(String name,Boolean canRoll);
}