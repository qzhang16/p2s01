package com.asg.p2s01;

import java.util.concurrent.CountDownLatch;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class PayrollApp implements MessageListener {
    private static final CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) {

        try {
            InitialContext initialContext = new InitialContext();
            Topic empTopic = (Topic) initialContext.lookup("topic/topic01");

            try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61616", "admin",
                    "admin");
                    JMSContext jmsContext = cf.createContext()) {

                // ObjectMessage msg = jmsContext.createObjectMessage(emp);
                // jmsContext.createProducer().send(empTopic, emp);
                // Employee emp = jmsContext.createConsumer(empTopic).receiveBody(Employee.class);
                JMSConsumer consumer = jmsContext.createSharedConsumer(empTopic,"paytopic");
                consumer.setMessageListener(new PayrollApp());

                latch.await();

                // System.out.println("Payroll: received " + emp.getName() + " from topic");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (NamingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("Payroll " + Thread.currentThread().getId() + " : received "
                    + message.getBody(Employee.class).getName() + " from topic");
                    if (message.getBody(Employee.class).getName().equalsIgnoreCase("Bob")) latch.countDown();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}
