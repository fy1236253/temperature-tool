package tool;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import serialException.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.TooManyListenersException;

import static serialPort.SerialTool.*;

/**
 * @author fengya
 */
public class SerialPortClient {
    private static final int BAUDRATE  = 115200;
    //保存串口对象
    private static SerialPort serialPort = null;
    public static void main(String[] args) throws SerialPortParameterFailure, NoSuchPort, PortInUse, NotASerialPort, TooManyListeners, TooManyListenersException, ReadDataFromSerialPortFailure, SerialPortInputStreamCloseFailure {
        ArrayList<String> commList = findPort();
        String commName = "";
        for (int i = 0; i < commList.size(); i++) {
            System.out.println(commList.get(i));
        }
        if (commList.size()>1){
            commName = commList.get(1);
        }
        System.out.println(commName);
        serialPort = openPort(commName,BAUDRATE);
        addListener(serialPort, new SerialListener());
    }


    private static class SerialListener implements SerialPortEventListener {

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

                    //System.out.println("found data");
                    byte[] data = null;

                    try {
                        if (serialPort == null) {
                            JOptionPane.showMessageDialog(null, "串口对象为空！监听失败！", "错误", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            data = readFromPort(serialPort);    //读取数据，存入字节数组
                            //System.out.println(new String(data));

                            //自定义解析过程
                            if (data == null || data.length < 1) {    //检查数据是否读取正确
                                JOptionPane.showMessageDialog(null, "读取数据过程中未获取到有效数据！请检查设备或程序！", "错误", JOptionPane.INFORMATION_MESSAGE);
                                System.exit(0);
                            } else {
                                String dataOriginal = new String(data);    //将字节数组数据转换位为保存了原始数据的字符串
                                System.out.println(dataOriginal);
                                String dataValid = "";    //有效数据（用来保存原始数据字符串去除最开头*号以后的字符串）
                                String[] elements = null;    //用来保存按空格拆分原始字符串后得到的字符串数组
                                //解析数据
                                if (!"".equals(dataOriginal)) {    //当数据的第一个字符是*号时表示数据接收完成，开始解析
                                    dataValid = dataOriginal.substring(1);
                                    elements = dataValid.split(" ");
                                    if (elements == null || elements.length < 1) {    //检查数据是否解析正确
                                        JOptionPane.showMessageDialog(null, "数据解析过程出错，请检查设备或程序！", "错误", JOptionPane.INFORMATION_MESSAGE);
                                        System.exit(0);
                                    } else {
                                        try {
                                            //更新界面Label值
                                            System.out.println("=================================");
                                            System.out.println( String.format(" id:%s,distance:%s,temperature:%s",elements[0],elements[1],elements[2]));
                                            System.out.println("=================================");
                                        } catch (ArrayIndexOutOfBoundsException e) {
                                            JOptionPane.showMessageDialog(null, "数据解析过程出错，更新界面数据失败！请检查设备或程序！", "错误", JOptionPane.INFORMATION_MESSAGE);
                                            System.exit(0);
                                        }
                                    }
                                }
                            }

                        }

                    } catch (ReadDataFromSerialPortFailure | SerialPortInputStreamCloseFailure e) {
                        JOptionPane.showMessageDialog(null, e, "错误", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);    //发生读取错误时显示错误信息后退出系统
                    }

                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + serialPortEvent.getEventType());
            }

        }
    }
}
