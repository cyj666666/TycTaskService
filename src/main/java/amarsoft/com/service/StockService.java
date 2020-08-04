package amarsoft.com.service;

import amarsoft.com.dao.TYCTaskMonitorDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Author lwp
 * @Date 2020/8/3 15:24
 * @Version
 */
@Service
public class StockService {
    private final Logger logger = LoggerFactory.getLogger(StockService.class);
    private static final List<String> entNames;
    @Value("${amarsoft.tyc.accessKey}")
    private String accesskey;

    @Value("${amarsoft.tyc.accessSecret}")
    private String accessSecet;

    @Value("${amarsoft.tyc.scrollTimeout}")
    private Integer scrollTimeout;

    @Value("${amarsoft.tyc.enableScroll}")
    private boolean enableScroll;

    @Value("${amarsoft.tyc.ps}")
    private Integer ps;

    @Autowired
    @Qualifier("tycdbJdbcTemplate")
    private JdbcTemplate tycJdbcTemplate;
    static {
        entNames = Arrays.asList("林州市开元区皓翔烟酒店","郑州亿顺化工物流有限公司","河南屹霖森装饰工程有限公司");
        //tables = Arrays.asList("company_holder","company_holder_entpub");
    }
    public void run (){
        entNames.parallelStream().forEach(e->{

        });
    }
}
