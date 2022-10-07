package com.github.cuteluobo.service;

import com.github.cuteluobo.pojo.RollUnit;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 信息更新任务接口
 * 返回值一般为定义好的异步任务，需要调用者额外执行
 *
 * @author CuteLuoBo
 * @param <T> 返回的格式对象
 * @date 2022/9/17 16:51
 */
public interface InfoUpdateTaskService<T> {

    /**
     * 更新全部单位信息，返回创建的 task
     * @return 本次更新单位信息列表
     */
    Callable<List<T>> updateAllUnitInfoTask();

    /**
     * 更新指定单位信息，返回创建的 task
     * @param unitIdList 指定更新单位ID列表
     * @return 本次更新单位信息列表
     */
    Callable<List<T>> updateAssignUnitInfoTask(List<Integer> unitIdList);
}
