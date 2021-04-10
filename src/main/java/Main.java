import cn.pomit.mybatis.configuration.MybatisConfiguration;
import com.github.cuteluobo.Service.ExpandRollService;
import com.github.cuteluobo.Service.Impl.YysRollServiceImpl;
import com.sun.media.jfxmedia.logging.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author CuteLuoBo
 * @date 2021-04-10
 */
public class Main {
    public static void main(String[] args) {
        initDatasource();
        ExpandRollService expandRollService = YysRollServiceImpl.INSTANCE;
        System.out.println(expandRollService.rollText(100, true, null, null, null));
    }

    private static void initDatasource(){
        Properties properties= new Properties();
        properties.put("mybatis.mapper.scan", "com.github.cuteluobo.mapper");
        properties.put("mybatis.datasource.type", "POOLED");
        properties.put("mybatis.datasource.driver", "org.sqlite.JDBC");
        //TODO 增加从配置文件读取并使用mysql数据库配置方式
        properties.put("mybatis.datasource.url", "jdbc:sqlite:database.sqlite");
        properties.put("mybatis.datasource.username", "");
        properties.put("mybatis.datasource.password", "");

        properties.put("mybatis.logImpl","STDOUT_LOGGING");
        MybatisConfiguration.initConfiguration(properties);

    }
}
