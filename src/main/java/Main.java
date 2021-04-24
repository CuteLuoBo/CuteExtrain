import cn.pomit.mybatis.configuration.MybatisConfiguration;
import com.github.cuteluobo.pojo.RollUnit;
import com.github.cuteluobo.service.ExpandRollService;
import com.github.cuteluobo.service.Impl.YysRollServiceImpl;

import java.util.Properties;

/**
 * @author CuteLuoBo
 * @date 2021-04-10
 */
public class Main {
    public static void main(String[] args) {
        initDatasource();
        ExpandRollService expandRollService = YysRollServiceImpl.INSTANCE;
        RollUnit rollUnit = new RollUnit(363,"SSR","帝释天");
//        Logger logger = LoggerFactory.getLogger(Main.class);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            System.out.println(rollUnit.getLevel()+"-"+rollUnit.getName()+" 追梦结果：");
            System.out.println(expandRollService.rollTextForSpecifyUnit(rollUnit,false).printResultText(false,false));
            System.out.println(System.currentTimeMillis()-startTime+"ms");
        }
//        System.out.println(expandRollService.rollText(10, true, null, null, null).printResultText(true,true));
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
        MybatisConfiguration.getSqlSessionFactory().getConfiguration().setMapUnderscoreToCamelCase(true);
    }
}
