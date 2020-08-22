package com.lvym.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ConsumerTopic {
    //  linux 上部署的activemq 的 IP 地址 + activemq 的端口号，如果用自己的需要改动
   // public static final String ACTIVEMQ_URL = "failover:(tcp://192.168.168.114:61616,tcp://192.168.168.114:61617,tcp://192.168.168.114:61618)";
    // public static final String ACTIVEMQ_URL = "nio://192.168.17.3:61608";
    public static final String ACTIVEMQ_URL ="tcp://192.168.168.114:61616";
    public static final String TOPIC_NAME = "topic";


    public static void main(String[] args) throws  Exception {

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
       connection.setClientID("lvym");

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);
        TopicSubscriber subscriber = session.createDurableSubscriber(topic, "lvym");

       connection.start();
        Message receive = subscriber.receive();
        while (receive!=null){
            TextMessage textMessage = (TextMessage)receive;
            System.out.println(" 收到的持久化 topic ："+textMessage.getText());
            receive = subscriber.receive();
        }
        // 保证控制台不灭  不然activemq 还没连上就关掉了连接


        session.close();
        connection.close();
    }
}
