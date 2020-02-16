package com.fanmi.temperature.mapper;

import com.fanmi.temperature.entity.Temperature;
import com.fanmi.temperature.entity.User;
import com.fanmi.temperature.entity.UserTemperature;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @author fengya
 */
@Repository("userMapper")
@Mapper
public interface UserMapper {
    /**
     * @param temperature
     * 向数据库中插入历史值
     */
    void insertTemperatureInfo(Temperature temperature);

    /**
     * @return User
     * @param id
     */
    User selectUserById(@Param("id") String id);

    HashMap selectAllUserCount();

    /**
     * @return 返回当天所有用户数
     */
    HashMap selectTodayAllUserCount();

    /**
     * @return 返回发烧的用户
     */
    HashMap selectFeverUser();

    /**
     * @param user
     */
    void insertUserInfo(User user);

    /**
     * @param id
     * @param time
     * @return
     */
    List<Temperature> selectTemperatureByLimitTime(@Param("id")String id,@Param("timeUnit")int time);

    List<Temperature> selectTemperatureOfWeek(@Param("id") String id);

    List<Temperature> selectTemperatureOfDay();

    /**
     * @return UserTemperature
     *  返回今天发烧人员
     */
    List<UserTemperature> selectTodayFeverUsers();
    /**
     * @return UserTemperature
     *  返回今天所有人员信息
     */
    List<User> selectTodayAllUsers();

    List<User> selectMissUsers();
}
