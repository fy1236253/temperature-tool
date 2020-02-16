package com.fanmi.temperature.controller;

import com.alibaba.fastjson.JSON;
import com.fanmi.temperature.entity.Temperature;
import com.fanmi.temperature.entity.User;
import com.fanmi.temperature.entity.UserTemperature;
import com.fanmi.temperature.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author fengya
 */
@Controller
public class MonitorController {
    @Qualifier("userService")
    @Autowired
    private UserService userService;

    @RequestMapping("/monitor")
    public ModelAndView monitorController(Model model) {
        Long count = (Long) userService.findAllUserCount().get("count");
        Long feverUser = (Long) userService.findFeverUserCount().get("fever");
        Long todayCount = (Long) userService.findTodayUserCount().get("count");
        //初始化大盘数据
        List<Temperature> singleUser = null;
        List<Temperature> temperatureList = userService.findTemperatureOfDay();
        if (temperatureList.size() > 0) {
            singleUser = userService.findTemperatureOfWeek(temperatureList.get(0).getId());
        }
        //fever user
        List<UserTemperature> todayFeverTemperature = userService.findTodayFeverUserTemperature();
        model.addAttribute("temperatureList", JSON.toJSONString(temperatureList));
        model.addAttribute("singleUserData", JSON.toJSONString(singleUser));
        model.addAttribute("todayUserInfo", JSON.toJSONString(todayFeverTemperature));
        model.addAttribute("UserCount", count);
        model.addAttribute("feverUser", feverUser);
        model.addAttribute("todayCount", todayCount);
        System.out.println(feverUser);
        return new ModelAndView("index", "userInfos", model);
    }

    @RequestMapping("/login")
    public ModelAndView loginController() {
        return new ModelAndView("login", "isSuccess", false);
    }

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public ModelAndView loginUserController(@RequestParam("id") String id,
                                            @RequestParam("extInfo") String extInfo,
                                            @RequestParam("sex") String sex,
                                            @RequestParam("className") String className,
                                            @RequestParam("name") String name) {
        User user = new User();
        user.setId(id);
        user.setClassName(className);
        System.out.println(sex);
        //1男 0女
        int sexInfo = sex.equals("1") ? 1 : 0;
        System.out.println(sexInfo);
        user.setSex(sexInfo);
        user.setName(name);
        user.setExtInfo(extInfo);
        try {
            userService.insertUserInfo(user);
        } catch (DuplicateKeyException e) {
            System.out.println(e);
            return new ModelAndView("login", "isSuccess", false);

        }
        return new ModelAndView("login", "isSuccess", true);
    }

}
