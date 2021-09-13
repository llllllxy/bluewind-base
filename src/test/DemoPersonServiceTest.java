import com.bluewind.base.module.system.demoperson.service.DemoPersonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author liuxingyu01
 * @date 2021-09-12-21:18
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class DemoPersonServiceTest {
    @Autowired
    private DemoPersonService demoPersonService;

    @Test
    public void getAll() {
        System.out.println(demoPersonService.getAllPerson("zhangsan"));
    }

}
