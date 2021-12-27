package cn.dcube.ahead.module;

import cn.dcube.ahead.dynamicDS.DynamicDataSourceRegister;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {"cn.dcube.ahead"})
@MapperScan(basePackages = {"cn.dcube"})
@Import({DynamicDataSourceRegister.class})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true) 
public class ApplicationMain{
	
	public static void main(String[] args) {
		SpringApplication.run(ApplicationMain.class, args);
	}

}
