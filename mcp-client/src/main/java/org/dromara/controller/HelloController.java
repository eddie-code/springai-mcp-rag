package org.dromara.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lee
 * @description
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("/world")
    public String hello() {
        return "hello Spring Ai";
    }

}
