package amarsoft.com.dao;


import amarsoft.com.bean.TYCTaskMonitor;
import amarsoft.com.utils.LogsUtils;
import amarsoft.com.utils.ObjectTools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author lwp
 * @Date 2020/7/29 13:46
 * @Version 天眼查 任务监控记录表
 */
@Component
public class TYCTaskMonitorDao1 {
    private final Logger logger = LoggerFactory.getLogger(TYCTaskMonitorDao1.class);

    @Autowired
    @Qualifier("tycdbJdbcTemplate")
    private JdbcTemplate tycJdbcTemplate;

    //增 改 查

    /**
     * tyc 查询根据流水号查询任务记录
     */
    public TYCTaskMonitor selectMonitorBySerialno(String serialno) {
        String sql = "select * from tyc_task_monitor1 where serialno=?";
        logger.info("查询接口数据写入任务：" + LogsUtils.getSqlLog(sql, serialno));
        List<TYCTaskMonitor> list = tycJdbcTemplate.query(sql, new Object[]{serialno}, new BeanPropertyRowMapper<TYCTaskMonitor>(TYCTaskMonitor.class));
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public TYCTaskMonitor selectNewestMonitor(String tablename) {
        String sql = "select * from tyc_task_monitor1 where tablename=? order by inputtime desc limit 1";
        logger.info("查询上次tyc接口数据写入任务状态：" + LogsUtils.getSqlLog(sql, tablename));
        List<TYCTaskMonitor> list = tycJdbcTemplate.query(sql, new Object[]{tablename}, new BeanPropertyRowMapper<TYCTaskMonitor>(TYCTaskMonitor.class));
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * tyc 更改任务记录状态以及记录message
     */
    public void updateMonitorBySerialno(String serialno, Integer status, String message) {
        String sql = "update  tyc_task_monitor1 set status=?, message = ? where serialno =? ";
        logger.info("更新tyc接口数据写入任务状态：" + LogsUtils.getSqlLog(sql, status, message, serialno));
        tycJdbcTemplate.update(sql, new Object[]{status, message, serialno});
    }

    /**
     * 跑批数据完成时 记录点用接口传递参数
     */
    public void updateMonitorParams(String serialno, String params, Integer status, String message) {
        String sql = "update tyc_task_monitor1 set inputparams = ? , status =? ,message = ? where serialno=?";
        logger.info("更新tyc接口数据写入任务状态：" + LogsUtils.getSqlLog(sql, params, status, message, serialno));
        tycJdbcTemplate.update(sql, new Object[]{params, status, message, serialno});
    }

    /**
     * 新增任务记录
     */
    public void insertTaskMonitor(TYCTaskMonitor t) {
        Map<String, String> map = ObjectTools.foreachBean(t);
        String keys = "";
        String values = "";
        int size = 0;
        for (Map.Entry<String, String> it : map.entrySet()) {
            String value = it.getValue();
            if (StringUtils.isBlank(value)) continue;
            if (size == 0) {
                keys += it.getKey();
                values += "'" + value + "'";
            } else {
                keys += "," + it.getKey();
                values += "," + "'" + value + "'";
            }
            size++;
        }
        String sql = "INSERT INTO tyc_task_monitor1 (" + keys + ") VALUES " + "(" + values + ")";
        logger.info("【新增任务记录】：" + sql);
        tycJdbcTemplate.update(sql);
    }
}
