package amarsoft.com.service;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * @Author lwp
 * @Date 2020/8/3 13:54
 * @Version
 */
public class TycTestAll {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String a;
        a=getMessageByUrlToken("http://open.api.tianyancha.com/services/open/ic/baseinfo/2.0?id=199557844"
                ,"174021dc-1be9-4059-bb5a-da6a97441a0a");
        System.out.println(a);


    }
    public static String getMessageByUrlToken(String path,String token){
        String result="";
        try{
            //根据地址获取请求
            HttpGet request=new HttpGet(path);
            //获取当前客户端对象
            request.setHeader("Authorization",token);
            HttpClient httpClient=new DefaultHttpClient();
            //通过请求对象获取响应对象
            HttpResponse response=httpClient.execute(request);
            //判断网络连接状态码是否正常
            if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                result= EntityUtils.toString(response.getEntity(),"utf-8");

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
