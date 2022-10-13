package com.github.cuteluobo.repository;

import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlNode;
import com.amihaiemil.eoyaml.YamlSequence;
import com.github.cuteluobo.enums.config.impl.AiDrawConfigEnum;
import com.github.cuteluobo.enums.config.impl.WebUiConfig;
import com.github.cuteluobo.service.AiDrawService;
import com.github.cuteluobo.service.Impl.WebUiAiDrawServiceImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author CuteLuoBo
 * @date 2022/10/12 2:05
 */
public class WebUiServerRepository {
    private List<WebUiAiDrawServiceImpl> sfwList = new ArrayList<>(3);
    private List<WebUiAiDrawServiceImpl> allNsfwList  = new ArrayList<>(3);

    private static volatile WebUiServerRepository instance;

    public static WebUiServerRepository getInstance() {
        if (instance == null) {
            synchronized (WebUiServerRepository.class) {
                instance = new WebUiServerRepository();
            }
        }
        return instance;
    }
    private WebUiServerRepository() {
        reloadServerList();
    }

    public boolean reloadServerList() {
        sfwList.clear();
        allNsfwList.clear();
        YamlMapping aiDrawMapping = GlobalConfig.getInstance().getAiDrawMapping();
        String aiDrawEnable = aiDrawMapping.string(AiDrawConfigEnum.ENABLE.getLabel());
        if (aiDrawMapping == null || aiDrawEnable == null || !"true".equalsIgnoreCase(aiDrawEnable.trim())) {
            return false;
        }
        YamlMapping webuiConfig = aiDrawMapping.yamlMapping(AiDrawConfigEnum.WEB_UI_CONFIG.getLabel());
        if (webuiConfig != null) {
            String webuiEnable = webuiConfig.string(WebUiConfig.ENABLE.getLabel());
            if (!(webuiEnable != null && "true".equalsIgnoreCase(webuiEnable.trim()))) {
                return false;
            }
            //从配置文件中读取
            YamlSequence yamlNodes = webuiConfig.yamlSequence(WebUiConfig.SERVER_LIST.getLabel());
            for (YamlNode node : yamlNodes) {
                if (node instanceof YamlMapping) {
                    YamlMapping mapping = (YamlMapping) node;
                    WebUiAiDrawServiceImpl webUiAiDrawService = new WebUiAiDrawServiceImpl(
                            mapping.string("url"), mapping.string("token"), mapping.string("username"), mapping.string("password")
                    );
                    String allowNsfw = mapping.string("allowNsfw");
                    if (allowNsfw != null && "true".equalsIgnoreCase(allowNsfw.trim())) {
                        allNsfwList.add(webUiAiDrawService);
                    } else {
                        sfwList.add(webUiAiDrawService);
                    }
                }
            }
        }
        return true;
    }

    /**
     * @return 是否为空
     */
    public boolean isEmpty() {
        return sfwList.isEmpty() && allNsfwList.isEmpty();
    }

    /**
     * 随机取出服务
     * @param allowNsfw 是否允许Nsfw内容
     * @return 无服务时，返回Null
     */
    public AiDrawService randomService(boolean allowNsfw) {
        if (allowNsfw) {
            if (!allNsfwList.isEmpty()) {
                if (allNsfwList.size() == 1) {
                    return allNsfwList.get(0);
                } else {
                    Random random = new Random();
                    return allNsfwList.get(random.nextInt(allNsfwList.size()));
                }
            }
        }
        if (sfwList.isEmpty()) {
            return null;
        } else if (sfwList.size() == 1) {
            return sfwList.get(0);
        } else {
            Random random = new Random();
            return sfwList.get(random.nextInt(sfwList.size()));
        }
    }
}
