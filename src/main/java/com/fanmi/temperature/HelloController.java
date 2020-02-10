package com.fanmi.temperature;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengya
 */
@Controller
public class HelloController {
    @RequestMapping("/hello")
    public String getHello() {
        System.out.println("fengya");
        return "index";
    }
}
