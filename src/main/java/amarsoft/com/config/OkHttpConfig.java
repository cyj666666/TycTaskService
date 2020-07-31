package amarsoft.com.config;

import com.alibaba.druid.pool.DruidDataSource;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Tycdb数据源配置
 * 对应application-*.yml 下的 spring.datasource.tycdb
 *
 * @author lfu1
 */
@Configuration
public class OkHttpConfig {
    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        return builder.build();
    }

//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("https://data.tianyancha.com/dblog.json?_accessKey=cHl0UQYhUQUerfOW&_sign=d766767709c3ef3de651aeea7e86d809&_timestamp=1596011002771&table=company_change&startTime=1595865600000&endTime=1595948400000&ps=500&enableScroll=true&scrollTimeout=3600")
//                .build();
//        try (Response response = client.newCall(request).execute()) {
//            System.out.println(response.body().string());
//        }
}
