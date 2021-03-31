package cn.dcube.ahead.core;

import org.springframework.boot.SpringApplication;

import cn.dcube.ahead.core.annotation.AheadApplication;
import cn.dcube.ahead.core.context.SpringContext;

@AheadApplication
public class ApplicationMain {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationMain.class);
		System.out.println(SpringContext.getContext());
	}

}
