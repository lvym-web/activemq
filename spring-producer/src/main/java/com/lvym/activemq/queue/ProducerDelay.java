package com.lvym.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;

import javax.jms.*;

public class ProducerDelay {

    //  linux 上部署的activemq 的 IP 地址 + activemq 的端口号，如果用自己的需要改动
   // public static final String ACTIVEMQ_URL = "failover:(tcp://192.168.168.114:61616,tcp://192.168.168.114:61617,tcp://192.168.168.114:61618)";
    // public static final String ACTIVEMQ_URL = "nio://192.168.17.3:61608";
    public static final String ACTIVEMQ_URL ="tcp://192.168.168.114:61616";
    public static final String QUEUE_NAME = "delay";


    public static void main(String[] args) throws  Exception {

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, 1);
        Queue queue = session.createQueue(QUEUE_NAME);
        MessageProducer producer = session.createProducer(queue);
        long time = 3* 1000;// 延时1min
        long period = 2*1000;// 每个10s
        int repeat = 6;// 6次
        for(int i=0;i<6;i++){
            TextMessage textMessage=session.createTextMessage("delayMessage:"+i);
            //延迟投递
            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,time);
          //重复投递的时间间隔
            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD,period);
            //重复投递次数
            textMessage.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT,repeat);
            producer.send(textMessage);
        }
        producer.close();
        session.close();
        connection.close();
        System.out.println("  **** 消息发送到MQ完成 ****");
    }
    }
