package com.yxgy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(scanBasePackages = {"com.yxgy", "org.n3r.idworker"})
@MapperScan("com.yxgy.mapper")  //是导入tk.mybatis不是org
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
