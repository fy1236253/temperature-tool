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
    //���洮�ڶ���
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
         * �����ص��Ĵ����¼�
         */
        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            switch (serialPortEvent.getEventType()) {

                case SerialPortEvent.BI: // 10 ͨѶ�ж�
                    JOptionPane.showMessageDialog(null, "�봮���豸ͨѶ�ж�", "����", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case SerialPortEvent.OE: // 7 ��λ�����������

                case SerialPortEvent.FE: // 9 ֡����

                case SerialPortEvent.PE: // 8 ��żУ�����

                case SerialPortEvent.CD: // 6 �ز����

                case SerialPortEvent.CTS: // 3 �������������

                case SerialPortEvent.DSR: // 4 ����������׼������

                case SerialPortEvent.RI: // 5 ����ָʾ

                case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 ��������������
                    break;

                case SerialPortEvent.DATA_AVAILABLE: // 1 ���ڴ��ڿ�������

                    //System.out.println("found data");
                    byte[] data = null;

                    try {
                        if (serialPort == null) {
                            JOptionPane.showMessageDialog(null, "���ڶ���Ϊ�գ�����ʧ�ܣ�", "����", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            data = readFromPort(serialPort);    //��ȡ���ݣ������ֽ�����
                            //System.out.println(new String(data));

                            //�Զ����������
                            if (data == null || data.length < 1) {    //��������Ƿ��ȡ��ȷ
                                JOptionPane.showMessageDialog(null, "��ȡ���ݹ�����δ��ȡ����Ч���ݣ������豸�����", "����", JOptionPane.INFORMATION_MESSAGE);
                                System.exit(0);
                            } else {
                                String dataOriginal = new String(data);    //���ֽ���������ת��λΪ������ԭʼ���ݵ��ַ���
                                System.out.println(dataOriginal);
                                String dataValid = "";    //��Ч���ݣ���������ԭʼ�����ַ���ȥ���ͷ*���Ժ���ַ�����
                                String[] elements = null;    //�������水�ո���ԭʼ�ַ�����õ����ַ�������
                                //��������
                                if (!"".equals(dataOriginal)) {    //�����ݵĵ�һ���ַ���*��ʱ��ʾ���ݽ�����ɣ���ʼ����
                                    dataValid = dataOriginal.substring(1);
                                    elements = dataValid.split(" ");
                                    if (elements == null || elements.length < 1) {    //��������Ƿ������ȷ
                                        JOptionPane.showMessageDialog(null, "���ݽ������̳��������豸�����", "����", JOptionPane.INFORMATION_MESSAGE);
                                        System.exit(0);
                                    } else {
                                        try {
                                            //���½���Labelֵ
                                            System.out.println("=================================");
                                            System.out.println( String.format(" id:%s,distance:%s,temperature:%s",elements[0],elements[1],elements[2]));
                                            System.out.println("=================================");
                                        } catch (ArrayIndexOutOfBoundsException e) {
                                            JOptionPane.showMessageDialog(null, "���ݽ������̳������½�������ʧ�ܣ������豸�����", "����", JOptionPane.INFORMATION_MESSAGE);
                                            System.exit(0);
                                        }
                                    }
                                }
                            }

                        }

                    } catch (ReadDataFromSerialPortFailure | SerialPortInputStreamCloseFailure e) {
                        JOptionPane.showMessageDialog(null, e, "����", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);    //������ȡ����ʱ��ʾ������Ϣ���˳�ϵͳ
                    }

                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + serialPortEvent.getEventType());
            }

        }
    }
}
