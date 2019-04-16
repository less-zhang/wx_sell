package com.xmcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class WxSellApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxSellApplication.class, args);
    }

}


/**
 * git remote add origin https://github.com/less-zhang/wx_sell.git
 * git push -u origin master
 */
