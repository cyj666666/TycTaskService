package amarsoft;

import amarsoft.com.service.DemoSqlService;
import amarsoft.com.service.PaoPishuju;
import amarsoft.com.service.TYCService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication
public class TycTaskServiceApplication implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(TycTaskServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TycTaskServiceApplication.class, args);
    }


//    @Autowired
//    private TYCService tycService;

    @Autowired
    private PaoPishuju paoPishuju;

    @Autowired
    private DemoSqlService demoSqlService;

    @Override
    public void run(String... strings) throws Exception {
        logger.info("当前时间：" + LocalDateTime.now());
        paoPishuju.run();
//        demoSqlService.run();
        System.out.println("任务结束");
        logger.info("结束时间：" + LocalDateTime.now());
    }

    private static long getStartTime() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = df.parse("2020-07-28 00:00:00");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getTimeInMillis();
    }

    private static long getendTime() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = df.parse("2020-07-28 23:50:00");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getTimeInMillis();
    }
}
