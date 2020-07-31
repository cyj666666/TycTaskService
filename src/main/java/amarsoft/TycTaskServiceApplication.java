package amarsoft;

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

    private static List<String> tables;

    //category_code
    static {
        tables = Arrays.asList("company_change", "company_holder", "company_holder_entpub", "company_staff" , "area_code", "category_code", "company_judicial_sale", "company_judicial_sale_item", "company_land_mortgage", "company_punishment_info_creditchina", "company_zxr_evaluate", "company_court_announcement", "company_court_open_announcement", "company_court_register", "company_dishonest_info", "company_judicial_assistance", "company_lawsuit", "company_send_announcement", "company_zxr", "company_zxr_final_case", "company_zxr_restrict", "company_copyright_reg", "company_copyright_works", "company_icp", "company_patent", "company_tm", "company_wechat", "company_app_info", "company_certificate", "company_customs_credit", "company_customs_credit_administrative_penalty", "company_customs_credit_rating", "company_employment", "company_land_announcement", "company_land_publicity", "company_land_transfer", "company_tele_license", "company_tele_license_annual_report", "company_tele_license_communication_badness", "company_weibo", "company_bid", "company_license", "company_license_creditchina", "company_license_entpub", "product_competition", "company_finance", "company_team_member", "organization", "organization_company_relation", "organization_invest");
    }

//    @Autowired
//    private TYCService tycService;

    @Autowired
    private PaoPishuju paoPishuju;

    @Override
    public void run(String... strings) throws Exception {
        logger.info("当前时间：" + LocalDateTime.now());
        paoPishuju.run();
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
