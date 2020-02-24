package com.fanmi.temperature.controller;

import com.alibaba.fastjson.JSON;
import com.fanmi.temperature.entity.User;
import com.fanmi.temperature.entity.UserTemperature;
import com.fanmi.temperature.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author fengya
 */
@RestController
public class SerialPortMonitorController {
    @Qualifier("userService")
    @Autowired
    private UserService userService;
    /***
    *  获取大盘数据
     * @return
     */
    @RequestMapping(value = {"/user/list"},method = {RequestMethod.GET})
    @ResponseBody
    public String getUserInfos(ModelMap model){
        Long count = (Long) userService.findAllUserCount().get("count");
        Long feverUser = (Long) userService.findFeverUserCount().get("fever");
        Long todayCount = (Long) userService.findTodayUserCount().get("count");
        model.addAttribute("userCount",count);
        model.addAttribute("feverUser",feverUser);
        model.addAttribute("todayCount",todayCount);
        return JSON.toJSONString(model);
    }

    /***
     *  获取当天在线人员
     * @return
     */
    @RequestMapping(value = {"/today/user/list"},method = {RequestMethod.GET})
    @ResponseBody
    public String getTodayUserInfos(ModelMap model){
        return JSON.toJSONString(userService.findTodayAllUsers());
    }
    /***
     *  获取当天发烧人员
     * @return
     */
    @RequestMapping(value = {"/today/fever/list"},method = {RequestMethod.GET})
    @ResponseBody
    public String getTodayFeverUserInfos(ModelMap model){
        return JSON.toJSONString(userService.findTodayFeverUserTemperature());
    }


    /***
     *  获取整体人员数据
     * @return
     */
    @RequestMapping(value = {"/temperature/fever/list"},method = {RequestMethod.GET})
    @ResponseBody
    public String getUserTemperature(){
        return JSON.toJSONString(userService.findTodayFeverUserTemperature());
    }    /***
     *  获取当天温度数据
     * @return
     */
    @RequestMapping(value = {"/temperature/today/{id}"},method = {RequestMethod.GET})
    @ResponseBody
    public String getTodayTemperature(@PathVariable("id")String id){
        List<UserTemperature> userInfo = userService.findUserTemperatureOfDay(id);
        return JSON.toJSONString(userInfo);
    }
    /***
     *  获取整体人员数据
     * @return
     */
    @RequestMapping(value = {"/userInfo/{id}"},method = {RequestMethod.GET})
    @ResponseBody
    public String getUserInfo(@PathVariable("id")String id){
        return JSON.toJSONString(userService.findUserInfobyId(id));
    }



    /***
     *  获取整体人员数据
     * @return
     */
    @RequestMapping(value = {"/echart/update/{id}"},method = {RequestMethod.GET})
    @ResponseBody
    public String getEchartDate( @PathVariable("id") String id){
        System.out.println(id);
        List<UserTemperature> temperatureList = null;
        if ("all".equals(id)){
            temperatureList = userService.findTemperatureOfDay();
        }else {
            temperatureList = userService.findTemperatureOfWeek(id);
        }
        return JSON.toJSONString(temperatureList);
    }

    @RequestMapping("/miss/users")
    public String getMissUsers(){
        List<User> userList = null;
        Long todayCount = (Long) userService.findTodayUserCount().get("count");

        userList = userService.findMissUsers();

        return JSON.toJSONString(userList);
    }

}
