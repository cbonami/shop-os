package be.vdab.demo.stockmanager;

import io.prometheus.client.CollectorRegistry;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = StockManagerApplication.class)
@TestPropertySource(properties = {"management.port=0"})
public class ShopfrontApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void contextLoads() {
    }

    // fixes https://github.com/prometheus/client_java/issues/279
    @After
    public void tearDown() {
        CollectorRegistry.defaultRegistry.clear();
    }

}
