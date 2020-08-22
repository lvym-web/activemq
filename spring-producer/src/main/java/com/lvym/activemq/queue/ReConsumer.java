package com.lvym.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;

import javax.jms.*;

public class ReConsumer {
    //  linux 上部署的activemq 的 IP 地址 + activemq 的端口号，如果用自己的需要改动
   // public static final String ACTIVEMQ_URL = "failover:(tcp://192.168.168.114:61616,tcp://192.168.168.114:61617,tcp://192.168.168.114:61618)";
    // public static final String ACTIVEMQ_URL = "nio://192.168.17.3:61608";
    public static final String ACTIVEMQ_URL ="tcp://192.168.168.114:61616";
    public static final String QUEUE_NAME = "requeue";


    public static void main(String[] args) throws  Exception {

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
       //重试次数
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(3);
        activeMQConnectionFactory.setRedeliveryPolicy(redeliveryPolicy);

        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);
        MessageConsumer consumer = session.createConsumer(queue);

        while (true){
            TextMessage receive = (TextMessage) consumer.receive(4000L);
            if (receive!=null){
                System.out.println("消费者:"+receive.getText());
            }else {
                break;
            }
        }

        // 保证控制台不灭  不然activemq 还没连上就关掉了连接
       // System.in.read();
        consumer.close();
        session.close();
        connection.close();
    }
}
