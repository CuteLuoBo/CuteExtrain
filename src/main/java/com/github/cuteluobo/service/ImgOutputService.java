package com.github.cuteluobo.service;

import com.github.cuteluobo.pojo.RollImgResult;
import com.github.cuteluobo.pojo.RollResultData;

/**
 * 图片输出服务类
 *
 * @author CuteLuoBo
 * @date 2022/6/18 14:09
 */
public interface ImgOutputService {

    /**
     * 生成图片输出结果
     * @param rollResultData 用于生成的抽取结果集
     * @param title          图片标题
     * @return 处理的图片数据
     */
    RollImgResult createImgResult(RollResultData rollResultData,String title);
}
