package com.lvym.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.AsyncCallback;

import javax.jms.*;
import java.util.UUID;

public class ProducerAsync {
    public static final String ACTIVEMQ_URL ="tcp://192.168.168.114:61616";
    public static final String QUEUE_NAME = "async";


    public static void main(String[] args) throws  Exception {

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //开启异步投递
        activeMQConnectionFactory.setUseAsyncSend(true);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, 1);
        Queue queue = session.createQueue(QUEUE_NAME);
        //向上转型到ActiveMQMessageProducer
        ActiveMQMessageProducer producer = (ActiveMQMessageProducer) session.createProducer(queue);
        for(int i=0;i<6;i++){
            TextMessage textMessage=session.createTextMessage("message:"+i);
            textMessage.setJMSMessageID(UUID.randomUUID().toString()+"-lvym");
            final String jmsMessageID = textMessage.getJMSMessageID();
            //使用ActiveMQMessageProducer的发送消息,可以创建回调
            producer.send(textMessage, new AsyncCallback() {
                public void onSuccess() {
                    System.out.println(jmsMessageID + ":发送成功");
                }
                public void onException(JMSException exception) {
                    System.out.println(jmsMessageID + ":发送失败");
                }
            });
        }
        producer.close();
        session.close();
        connection.close();
        System.out.println("  **** 消息发送到MQ完成 ****");
    }
}
