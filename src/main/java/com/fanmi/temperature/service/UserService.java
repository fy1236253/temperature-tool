package com.fanmi.temperature.service;

import com.fanmi.temperature.entity.Temperature;
import com.fanmi.temperature.entity.User;
import com.fanmi.temperature.entity.UserTemperature;
import com.fanmi.temperature.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author fengya
 */
@Repository("userService")
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    /**
     * @param temperature
     * 插入整个
     * */
    public void recordTemperature(Temperature temperature){
        userMapper.insertTemperatureInfo(temperature);
    }

    public User findUserInfobyId(String id){
        return userMapper.selectUserById(id);
    }
    public void insertUserInfo(User user){
        userMapper.insertUserInfo(user);
    }
    public List<Temperature> findUserTemperatureLimit(String id, int time) {
        return userMapper.selectTemperatureByLimitTime(id,time);
    }

    public HashMap findAllUserCount(){
        return userMapper.selectAllUserCount();
    }
    public List<UserTemperature> findTodayFeverUserTemperature(){
        return userMapper.selectTodayFeverUsers();
    }
    public HashMap findTodayUserCount(){
        return userMapper.selectTodayAllUserCount();
    }

    public HashMap findFeverUserCount(){
        return userMapper.selectFeverUser();
    }
    public List<User> findTodayAllUsers(){
        return userMapper.selectTodayAllUsers();
    }

    public List<Temperature> findTemperatureOfDay(){
        return userMapper.selectTemperatureOfDay();
    }

    public List<Temperature> findTemperatureOfWeek(String id){
        return userMapper.selectTemperatureOfWeek(id);
    }

    public List<User> findMissUsers() {
        return userMapper.selectMissUsers();
    }
}
