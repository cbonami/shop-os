package be.vdab.demo.zuul;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableZuulProxy
@Slf4j
public class ZuulRestApplication {

    public static void main(String[] args) {
        List<Object> sources = new ArrayList<>();
        sources.add(ZuulRestApplication.class);
        try {
            for (Resource resource : new PathMatchingResourcePatternResolver().getResources("file:/groovy/*.groovy")) {
                log.info("Found and will load groovy script " + resource.getFilename());
                sources.add(resource);
            }
            if (sources.size() == 1) {
                log.info("No groovy script found under /groovy/*.groovy");
            }
        } catch (IOException e) {
            log.warn("Failed to query classpath for groovy scripts", e);
        }
        SpringApplication.run(sources.toArray(), args);
    }
}