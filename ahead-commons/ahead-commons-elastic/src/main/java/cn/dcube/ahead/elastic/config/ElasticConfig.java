package cn.dcube.ahead.elastic.config;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: wenlong
 * @CreateTime: 2020-06-09 09:38
 * @Description: ES配置类
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "elastic", name = {"urls" })
public class ElasticConfig {

	/**
	 * ES地址配置，多个以英文逗号分隔 配置示例： https://192.168.31.12:9200,https://192.168.31.14:9200
	 */
	@Value("${elastic.urls}")
	private String[] urls;

	/**
	 * ES认证证书
	 */
	@Value("${elastic.ssl.enable:true}")
	private Boolean sslEnable;

	/**
	 * ES认证用户名
	 */
	@Value("${elastic.ssl.user:elastic}")
	private String sslUser;

	/**
	 * ES认证密码
	 */
	@Value("${elastic.ssl.pwd:123456}")
	private String sslPwd;

	/**
	 * p12认证文件
	 */
	@Value("${elastic.ssl.p12}")
	private String p12File;

	/**
	 * 文档数量达到指定阈值时提交
	 */
	@Value("${elastic.bulk_processor.btach_size:1000}")
	private int batchSize;

	/**
	 * 总文档体积达到5M时提交
	 */
	@Value("${elastic.bulk_processor.bulk_size:5}")
	private long bulkSize;

	/**
	 * 每5S提交一次(无论文档数量、体积是否达到阈值)
	 */
	@Value("${elastic.bulk_processor.flush_interval:5}")
	private long flushInterval;

	/**
	 * 发送bulk的并发线程数
	 */
	@Value("${elastic.bulk_processor.concurrent_requests:6}")
	private Integer concurrentRequests;

	private RestHighLevelClient client;

	@Bean
	public RestHighLevelClient getRestHighLevelClient() {
		try {
			HttpHost[] httpHosts = Stream.of(urls).map(this::createHttpHost).collect(Collectors.toList())
			        .toArray(new HttpHost[1]);
			RestClientBuilder builder = RestClient.builder(httpHosts);
			if (sslEnable) {
				final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
				credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(sslUser, sslPwd));

				Path keyStorePath = Paths.get(p12File);
				KeyStore truststore = KeyStore.getInstance("PKCS12");
				InputStream is = Files.newInputStream(keyStorePath);
				truststore.load(is, sslPwd.toCharArray());

				SSLContextBuilder sslBuilder = SSLContexts.custom().loadTrustMaterial(truststore, null);
				final SSLContext sslContext = sslBuilder.build();

				builder.setHttpClientConfigCallback(
				        httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
				                .setSSLContext(sslContext).setSSLHostnameVerifier(new HostnameVerifier() {
					                @Override
					                public boolean verify(String s, SSLSession sslSession) {
						                return SSLConnectionSocketFactory.getDefaultHostnameVerifier()
						                        .verify("instance", sslSession);
					                }
				                }));
			}
			client = new RestHighLevelClient(builder);

			log.info("RestHighLevelClient初始化完成");
			return client;
		} catch (Exception e) {
			log.error("RestHighLevelClient初始化失败：", e);
		}
		return null;
	}

	/**
	 * 初始化ES Bulk
	 *
	 * @return
	 */
	BulkProcessor.Listener listener = new BulkProcessor.Listener() {
		@Override
		public void beforeBulk(long l, BulkRequest bulkRequest) {
			// log.info("准备提交");
		}

		@Override
		public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
			// log.info("提交完成");
		}

		@Override
		public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
			// log.info("提交发生异常");
		}
	};

	@Bean
	public BulkProcessor myBulkProcessor() {
		return BulkProcessor
		        .builder((request, bulkListener) -> client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
		                listener)
		        // 文档数量达到指定阈值时提交
		        .setBulkActions(batchSize)
		        // 总文档体积达到5M时提交
		        .setBulkSize(new ByteSizeValue(bulkSize, ByteSizeUnit.MB))
		        // 每5S提交一次(无论文档数量、体积是否达到阈值)
		        .setFlushInterval(TimeValue.timeValueSeconds(flushInterval))
		        // 默认是1，表示积累bulk requests和发送bulk是异步的，其数值表示发送bulk的并发线程数，设置为0表示二者同步的
		        .setConcurrentRequests(concurrentRequests)
		        // 当ES由于资源不足发生异常 EsRejectedExecutionException重試策略
		        .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3)).build();
	}

	private HttpHost createHttpHost(String ip) {
		return HttpHost.create(ip);
	}
}
