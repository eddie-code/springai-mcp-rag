package org.dromara.controller;

import jakarta.annotation.Resource;
import org.dromara.service.SearXngService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lee
 * @description
 */
@RestController
@RequestMapping("/internet")
public class InternetController {

    @Resource
    private SearXngService searXngService;

    @GetMapping("/test")
    public Object test(@RequestParam("query") String query) {
        // 请求： http://127.0.0.1:8080/internet/test?query=吾爱破解
        // 打印： http://192.168.56.101:6080/search?q=吾爱破解&format=json
        return searXngService.search(query);
    }


}
