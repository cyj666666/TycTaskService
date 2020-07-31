package amarsoft.com.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Tycdb数据源配置
 * 对应application-*.yml 下的 spring.datasource.tycdb
 * @author lfu1
 */
@Configuration
public class TycJdbcConfig {
	@Value("${spring.datasource.tycdb.url}")
    private String dbUrl;

    @Value("${spring.datasource.tycdb.username}")
    private String username;

    @Value("${spring.datasource.tycdb.password}")
    private String password;

    @Value("${spring.datasource.tycdb.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.tycdb.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.tycdb.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.tycdb.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.tycdb.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.tycdb.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.tycdb.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.tycdb.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.tycdb.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.tycdb.testOnReturn}")
    private boolean testOnReturn;
    
    @Bean("tycdb")
    @Qualifier("tycdb")
    @Primary
    public DruidDataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        return dataSource;
    }
    
    @Bean("tycdbJdbcTemplate")
    @Primary
    public JdbcTemplate jdbcTemplate(@Qualifier("tycdb") DruidDataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
