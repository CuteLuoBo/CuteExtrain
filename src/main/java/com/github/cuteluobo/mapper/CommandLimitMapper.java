package com.github.cuteluobo.mapper;

import com.github.cuteluobo.model.CommandLimit;import org.apache.ibatis.annotations.Select;import java.util.List;

/**
 * @author CuteLuoBo
 * @date 2021-04-15
 */
public interface CommandLimitMapper {
    /**
     * delete by primary key
     *
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(CommandLimit record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(CommandLimit record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    CommandLimit selectByPrimaryKey(Integer id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(CommandLimit record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(CommandLimit record);

    /**
     * 获取列表
     *
     * @return 查询结果
     */
    @Select("SELECT * FROM command_limit")
    List<CommandLimit> selectList();

    //TODO 增加CURD接口
}