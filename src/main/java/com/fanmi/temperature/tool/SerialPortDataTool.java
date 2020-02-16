package com.fanmi.temperature.tool;

import com.alibaba.fastjson.JSON;
import com.fanmi.temperature.entity.Temperature;
import com.fanmi.temperature.entity.User;
import com.fanmi.temperature.model.UserInfo;
import com.fanmi.temperature.serialException.ReadDataFromSerialPortFailure;
import com.fanmi.temperature.serialException.SerialPortInputStreamCloseFailure;
import com.fanmi.temperature.service.UserService;
import com.fanmi.temperature.websocket.CustomWebSocket;
import gnu.io.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author fengya
 */
@Service
public class SerialPortDataTool {
    @Qualifier("userService")
    @Autowired
    public UserService userService;

    public static Logger logger = LoggerFactory.getLogger(SerialPortDataTool.class);

    public void handleSerialPortData(){
        SerialPort serialPort = SerialTool.serialPort;
        System.out.println(serialPort);
        byte[] data = null;
        try {
            if (serialPort == null) {
                System.exit(0);
            } else {
                data = SerialTool.readFromPort(serialPort);
                if (data == null || data.length < 1) {
                    System.exit(0);
                } else {
                    String dataOriginal = new String(data);
                    String[] elements = null;
                    dataOriginal = replaceBlank(dataOriginal);
                    //解析数据
                    if (!"".equals(dataOriginal)) {
                        elements = dataOriginal.split(" ");
                        if (elements == null || elements.length < 1) {
                            System.exit(0);
                        } else {
                            try {
                                System.out.println(dataOriginal);
                                String id = replaceBlank(elements[0]);
                                String temperature = replaceBlank(elements[2]);
                                User user = userService.findUserInfobyId(id);
                                //从数据库中能够读取到数据
                                if (user!=null) {
                                    List<Temperature> frequency = userService.findUserTemperatureLimit(id,1);
                                    //十分钟内已经采集过数据了就需要过滤
                                    logger.info(String.valueOf(frequency.size()));
                                    if (frequency.size()>1){
                                        logger.info("bad temperature");
                                        return;
                                    }
                                    Temperature t = new Temperature();
                                    t.setId(id);
                                    logger.info("temp:"+temperature);
                                    t.setTemperature(Double.parseDouble(temperature));
                                    userService.recordTemperature(t);
                                    UserInfo userInfo = new UserInfo();
                                    userInfo.setId(id);
                                    userInfo.setSex(user.getSex());
                                    userInfo.setName(user.getName());
                                    userInfo.setClassName(user.getClassName());
                                    userInfo.setCardId(user.getCardId());
                                    userInfo.setTemperature(Double.parseDouble(temperature));
                                    userInfo.setGmtCreate(new Date());
                                    CustomWebSocket.sendInfo(JSON.toJSONString(userInfo),"10");
                                    logger.info(JSON.toJSONString(userInfo));
                                } else {
                                    System.out.println("insert user success");
                                }
                                System.out.println(String.format(" id:%s,distance:%s,temperature:%s", id, elements[1], temperature));
                                System.out.println("=================================");

                            } catch (ArrayIndexOutOfBoundsException e) {
                                JOptionPane.showMessageDialog(null, "数据解析过程出错，更新界面数据失败！请检查设备或程序！", "错误", JOptionPane.INFORMATION_MESSAGE);
                                System.exit(0);
                            } catch (Exception e) {
                                logger.info("Exception",e);
                            }
                        }
                    }
                }

            }

        } catch (ReadDataFromSerialPortFailure | SerialPortInputStreamCloseFailure e) {
            System.exit(0);
        }
    }

    /**
     * @param str
     * @return String
     * 处理从串口中读取的数据中的换行符空格等
     *
     * */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
