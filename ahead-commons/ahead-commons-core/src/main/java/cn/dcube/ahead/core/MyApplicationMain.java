package cn.dcube.ahead.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cn.dcube.ahead.core.annotation.AheadApplication;
import cn.dcube.ahead.core.context.SpringContext;

@AheadApplication
public class MyApplicationMain {

	public static void main(String[] args) {
		SpringApplication.run(MyApplicationMain.class);
		System.out.println(SpringContext.getContext());
	}

}
