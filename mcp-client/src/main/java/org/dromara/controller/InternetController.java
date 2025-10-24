package org.dromara.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.dromara.bean.ChatEntity;
import org.dromara.service.IChatService;
import org.dromara.service.ISearXngService;
import org.springframework.web.bind.annotation.*;

/**
 * @author lee
 * @description
 */
@RestController
@RequestMapping("/internet")
public class InternetController {

    @Resource
    private ISearXngService ISearXngService;

    @Resource
    private IChatService chatService;

    @GetMapping("/test")
    public Object test(@RequestParam("query") String query) {
        // 请求： http://127.0.0.1:8080/internet/test?query=吾爱破解
        // 打印： http://192.168.56.101:6080/search?q=吾爱破解&format=json
        return ISearXngService.search(query);
    }

    @PostMapping("/search")
    public void search(@RequestBody ChatEntity chatEntity, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        chatService.doInternetSearch(chatEntity);
    }

}
