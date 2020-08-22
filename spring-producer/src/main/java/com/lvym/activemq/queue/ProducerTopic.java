package com.lvym.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ProducerTopic {

    //  linux 上部署的activemq 的 IP 地址 + activemq 的端口号，如果用自己的需要改动
   // public static final String ACTIVEMQ_URL = "failover:(tcp://192.168.168.114:61616,tcp://192.168.168.114:61617,tcp://192.168.168.114:61618)";
    // public static final String ACTIVEMQ_URL = "nio://192.168.17.3:61608";
    public static final String ACTIVEMQ_URL ="tcp://192.168.168.114:61616";
    public static final String TOPIC_NAME = "topic";


    public static void main(String[] args) throws  Exception {

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();

        Session session = connection.createSession(false, 1);
        Topic topic = session.createTopic(TOPIC_NAME);

        MessageProducer producer = session.createProducer(topic);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        connection.start();
        for(int i=0;i<3;i++){
            TextMessage textMessage=session.createTextMessage("message:"+i);
            producer.send(textMessage);
        }
        producer.close();
        session.close();
        connection.close();
        System.out.println("  **** 消息发送到MQ完成 ****");
    }
    }
