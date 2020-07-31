package amarsoft.com.service;


import amarsoft.com.bean.TYCTaskMonitor;
import amarsoft.com.dao.TYCTaskMonitorDao;
import amarsoft.com.utils.AccessSecretUtils;
import amarsoft.com.utils.CallAppletServiceUtils;
import amarsoft.com.utils.DateUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author lwp
 * @Date 2020/7/29 14:45
 * @Version
 */
@Service
public class TYCService {

    private final Logger logger = LoggerFactory.getLogger(TYCService.class);

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
    private TYCTaskMonitorDao tycTaskMonitorDao;

    @Autowired
    @Qualifier("tycdbJdbcTemplate")
    private JdbcTemplate tycJdbcTemplate;

    private static List<String> tables;

    static {
        tables = Arrays.asList("company_change", "company_holder", "company_holder_entpub", "company_staff", "human_name", "area_code", "category_code", "company_judicial_sale", "company_judicial_sale_item", "company_land_mortgage", "company_punishment_info_creditchina", "company_zxr_evaluate", "company_court_announcement", "company_court_open_announcement", "company_court_register", "company_dishonest_info", "company_judicial_assistance", "company_lawsuit", "company_send_announcement", "company_zxr", "company_zxr_final_case", "company_zxr_restrict", "company_copyright_reg", "company_copyright_works", "company_icp", "company_patent", "company_tm", "company_wechat", "company_app_info", "company_certificate", "company_customs_credit", "company_customs_credit_administrative_penalty", "company_customs_credit_rating", "company_employment", "company_land_announcement", "company_land_publicity", "company_land_transfer", "company_tele_license", "company_tele_license_annual_report", "company_tele_license_communication_badness", "company_weibo", "company_bid", "company_license", "company_license_creditchina", "company_license_entpub", "product_competition", "company_finance", "company_team_member", "organization", "organization_company_relation", "organization_invest");
    }

    public void run() {
        Date now = new Date();
        //表明状态录入时间
        tables.stream().forEach(e -> {
            TYCTaskMonitor tycTaskMonitor = tycTaskMonitorDao.selectNewestMonitor(e);
            if (tycTaskMonitor != null) {
                Integer status = tycTaskMonitor.getStatus();
                if (status == 1) {//查询上一次任务状态为正常结束   如果不是正常结束状态  那就啥都不干
                    String serialno = generatorSerialno(now);
                    String taskStarttime = tycTaskMonitor.getEndtime();
                    Date startTime = DateUtils.parse(taskStarttime);
                    Date endTime = new Date(startTime.getTime() + 600000);
                    String taskEndtime = DateUtils.format(endTime, "yyyy-MM-dd HH:mm:ss");
                    //创建监控记录
                    TYCTaskMonitor newTaskMonitor = new TYCTaskMonitor();
                    newTaskMonitor.setSerialno(e + serialno);
                    newTaskMonitor.setStarttime(taskStarttime);
                    newTaskMonitor.setEndtime(taskEndtime);
                    newTaskMonitor.setStatus(0);//未结束状态
                    newTaskMonitor.setTablename(e);
                    newTaskMonitor.setInputtime(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    tycTaskMonitorDao.insertTaskMonitor(newTaskMonitor);
                    executeTask(e, serialno, startTime, endTime);
                }
            } else {
                logger.info("表[" + e + "]无流水数据,不执行增量落库");
            }
        });
    }

    private Date caculatetaskEndDate(Date now) {
        int nowMin = Integer.parseInt(DateUtils.format(now, "mm"));
        String nowToHour = DateUtils.format(now, "yyyy-MM-dd HH:");

        if (nowMin / 10 != 0) {
            nowMin = nowMin - nowMin % 10;
        }

        String finalDate = nowToHour + nowMin + ":00";

        return DateUtils.parseDate(finalDate);
    }

    private void executeTask(String e, String serialno, Date taskStarttime, Date taskEndtime) {
        boolean dataResult = false;
        String message = "";
        String formatStartTime = DateUtils.format(taskStarttime, "yyyy-MM-dd HH:mm:ss");
        String formatEndTime = DateUtils.format(taskEndtime, "yyyy-MM-dd HH:mm:ss");
        try {
            // 请求参数(变量)
            String table = e;
            Long startTime = taskStarttime.getTime();
            Long endTime = taskEndtime.getTime();

            // 生成签名
            JSONObject query = new JSONObject();
            query.put("table", table);
            query.put("startTime", startTime);
            query.put("endTime", endTime);

            //当前时间戳
            long timeStamp = new Date().getTime();
            query.put("ps", ps);
            query.put("enableScroll", enableScroll);
            query.put("scrollTimeout", scrollTimeout);

            String sign = AccessSecretUtils.sign(accessSecet, timeStamp, query);

            query.put("_accessKey", accesskey);
            query.put("_sign", sign);
            query.put("_timestamp", timeStamp);

            logger.info("[" + formatStartTime + "--" + formatEndTime + "],[" + table + "]开始调用tianyancha查询接口,入参:" + query);

            JSONObject jsonObject = CallAppletServiceUtils.get("https://data.tianyancha.com/dblog.json", query);

            //第一页
            if (jsonObject.get("state").toString().equals("ok")) {
                logger.info("[" + formatStartTime + "--" + formatEndTime + "],[" + table + "]查询出数据" + jsonObject.getJSONObject("data").get("realTotal") + "条");

                int realTotal = jsonObject.getJSONObject("data").getIntValue("realTotal");
                int page = (realTotal - 1) / ps + 1;
                int totalPage = (realTotal - 1) / ps + 1;
                logger.info("[" + formatStartTime + "--" + formatEndTime + "],[" + table + "]查询出" + page + "页数据,每页size:" + ps + "条");

                JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("items");
                insertTableDate(jsonArray);

                page--;
                String scrollId = jsonObject.getJSONObject("data").getString("scrollId");
                //生成signPage
                JSONObject querySign = new JSONObject();
                querySign.put("scrollId", scrollId);
                dataResult = true;
                while (page > 0 && scrollId != null) {
                    JSONObject queryPage = new JSONObject();
                    //当前时间戳
                    long timeStampPage = new Date().getTime();

                    queryPage.put("_accessKey", accesskey);
                    queryPage.put("_timestamp", timeStampPage);
                    queryPage.put("scrollId", scrollId);
                    String signPage = AccessSecretUtils.sign(accessSecet, timeStampPage, querySign);
                    queryPage.put("_sign", signPage);

                    int curpage = totalPage - page + 1;
                    logger.info("[" + formatStartTime + "--" + formatEndTime + "],[" + table + "]开始调用tianyancha接口分页查询第" + curpage + "页数据,入参:" + queryPage);

                    JSONObject jsonObjectPage = CallAppletServiceUtils.get("https://data.tianyancha.com/dblog.json", queryPage);
                    if (jsonObjectPage.get("state").toString().equals("ok")) {
                        JSONArray jsonArrayCurPage = jsonObjectPage.getJSONObject("data").getJSONArray("items");
                        insertTableDate(jsonArrayCurPage);
                        page--;
                    } else {
                        logger.info("[" + formatStartTime + "--" + formatEndTime + "],[" + table + "]查询出数据出错,message:" + jsonObjectPage.get("message"));
                        dataResult = false;
                        message = "[查询第" + curpage + "页数据]报错，message:" + jsonObjectPage.get("message") + "";
                        break;
                    }
                }
            } else {
                logger.info("[" + formatStartTime + "--" + formatEndTime + "],[" + table + "]查询出数据出错,message:" + jsonObject.get("message"));
                dataResult = false;
                message = "[查询第1页数据]报错，message:" + jsonObject.get("message") + "";
            }
        } catch (Exception exception) {
            dataResult = false;
            message = "[sql查询抛出异常],message:" + exception.getMessage();
            logger.info("sql查询抛出异常", exception);
        }
        if (dataResult) {
            //完成操作将任务记录状态更改为完成
            tycTaskMonitorDao.updateMonitorBySerialno((e + serialno), 1, message);
        } else {
            tycTaskMonitorDao.updateMonitorBySerialno((e + serialno), 2, message == null ? null : message.substring(0, 1000));
        }
    }

    //调接口返回数据存入数据库中
    public void insertTableDate(JSONArray items) {
        if (items.size() > 0) {
            logger.info("开始插入sql,items条数：" + items.size());
            List<String> values = new ArrayList<>(items.size());
            JSONObject jo = items.getJSONObject(0);
            String table = jo.getString("table");
            JSONObject firstData = jo.getJSONObject("data");
            List<String> attrs = new ArrayList<>(firstData.keySet());

            items.stream().forEach(e -> {
                JSONObject item = (JSONObject) e;
                JSONObject data = item.getJSONObject("data");
                StringBuilder sb = new StringBuilder();
                sb.append("(");
                for (int i = 0; i < attrs.size() - 1; i++) {
                    String d = data.getString(attrs.get(i));
                    if (d != null) {
                        sb.append("\"" + d.replaceAll("\"", "'") + "\",");
                    } else {
                        sb.append(d + ",");
                    }
                }
                if (data.getString(attrs.get(attrs.size() - 1)) != null) {
                    sb.append("\"" + data.getString(attrs.get(attrs.size() - 1)));
                    sb.append("\")");
                } else {
                    sb.append("" + data.getString(attrs.get(attrs.size() - 1)));
                    sb.append(")");
                }

                values.add(sb.toString());
            });
            if (values.size() > 0) {
                StringBuilder keys = new StringBuilder();
                keys.append("(");
                for (int i = 0; i < attrs.size() - 1; i++) {
                    keys.append("" + attrs.get(i) + ",");
                }
                keys.append("" + attrs.get(attrs.size() - 1) + ")");
                String tableValues = keys.toString();
                String tableAttrs = StringUtils.join(values.toArray(), ",");
                String sql = "insert into " + table + " " + tableValues + " values " + tableAttrs;
                logger.info("【新增" + table + "表记录】：" + sql);
                tycJdbcTemplate.update(sql);
            }

        }


    }

    /**
     * 根据当前时间生产任务流水号
     */
    public String generatorSerialno(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        return sdf.format(date);
    }

    /**
     * 根据当前时间生成上一次的任务流水号
     */
//    public  String getLastTaskSerialno(Date date){
//        Date lastDate = new Date(date.getTime() - 600000);
//        return generatorSerialno(lastDate);
//    }
}
