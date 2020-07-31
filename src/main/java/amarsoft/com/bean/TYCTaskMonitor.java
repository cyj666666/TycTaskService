package amarsoft.com.bean;

import java.util.Date;

/**
 * @Author lwp
 * @Date 2020/7/29 13:54
 * @Version
 */
public class TYCTaskMonitor {
    /**
     * 任务流水号
     */
    private String serialno;
    /**
     * 任务调用接口开始时间
     */
    private String starttime;
    /**
     *  任务调用接口结束时间
     */
    private String endtime;
    /**
     *  任务表名
     */
    private String tablename;
    /**
     *  任务状态  0未结束 1结束（成功） 2结束（失败）
     */
    private Integer status;
    /**
     *  录入时间
     */
    private String inputtime;

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getInputtime() {
        return inputtime;
    }

    public void setInputtime(String inputtime) {
        this.inputtime = inputtime;
    }
}
