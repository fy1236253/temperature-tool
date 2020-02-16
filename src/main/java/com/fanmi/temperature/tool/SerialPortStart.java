package com.fanmi.temperature.tool;

import com.fanmi.temperature.serialException.*;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @author fengya
 */
@Configuration
public class SerialPortStart {
    private static final int BAUDRATE = 115200;
    public static SerialPort serialPort =null;
    @Autowired
    private SerialPortDataTool serialPortDataTool;
    @Bean
    public void SerialPortStart() {
        ArrayList<String> commList = SerialTool.findPort();
        String commName = "";
        for (int i = 0; i < commList.size(); i++) {
            commName = commList.get(i);
            if(!commName.equals("COM3")){
                continue;
            }
            System.out.println(commName);
            try {
                serialPort = SerialTool.openPort(commName, BAUDRATE);
                SerialTool.addListener(serialPort, new SerialListener());
            } catch (SerialPortParameterFailure serialPortParameterFailure) {
                System.out.println("串口参数错误");
                serialPort.close();
                continue;
            } catch (NotASerialPort notASerialPort) {
                System.out.println("不是该串口");
                serialPort.close();
                continue;
            } catch (NoSuchPort noSuchPort) {
                System.out.println("没有这样的串口");
                serialPort.close();
                continue;
            } catch (PortInUse portInUse) {
                System.out.println("串口已经使用");
                serialPort.close();
                System.exit(0);
                continue;
            } catch (TooManyListeners tooManyListeners) {
                System.out.println("太多监听");
                serialPort.close();
                continue;
            }
        }

    }


    private class SerialListener implements SerialPortEventListener {

        /**
         * 处理监控到的串口事件
         */
        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            switch (serialPortEvent.getEventType()) {

                case SerialPortEvent.BI: // 10 通讯中断
                    JOptionPane.showMessageDialog(null, "与串口设备通讯中断", "错误", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case SerialPortEvent.OE: // 7 溢位（溢出）错误

                case SerialPortEvent.FE: // 9 帧错误

                case SerialPortEvent.PE: // 8 奇偶校验错误

                case SerialPortEvent.CD: // 6 载波检测

                case SerialPortEvent.CTS: // 3 清除待发送数据

                case SerialPortEvent.DSR: // 4 待发送数据准备好了

                case SerialPortEvent.RI: // 5 振铃指示

                case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
                    break;

                case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
                    serialPortDataTool.handleSerialPortData();
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + serialPortEvent.getEventType());
            }

        }
    }

}
