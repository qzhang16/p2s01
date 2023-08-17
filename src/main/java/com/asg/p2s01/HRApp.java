package com.asg.p2s01;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class HRApp {
    public static void main(String[] args) {

        try {
            InitialContext initialContext = new InitialContext();
            Topic empTopic = (Topic) initialContext.lookup("topic/topic01");

            try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61616", "admin",
                    "admin");
                    JMSContext jmsContext = cf.createContext()) {

                Employee emp = null;

                JMSProducer producer = jmsContext.createProducer();

                for (int i = 0; i < 10; i++) {
                    emp = new Employee();
                    emp.setId(i);
                    emp.setName("Joe" + i);
                    emp.setDesignation("Engineer");

                    // ObjectMessage msg = jmsContext.createObjectMessage(emp);
                    producer.send(empTopic, emp);
                    System.out.println("sent " + emp.getName() + " to topic");

                }

                emp = new Employee();
                emp.setId(1000);
                emp.setName("Bob");
                emp.setDesignation("Engineer");

                // ObjectMessage msg = jmsContext.createObjectMessage(emp);
                jmsContext.createProducer().send(empTopic, emp);
                System.out.println("sent " + emp.getName() + " to topic");

            }

        } catch (NamingException e) {
            e.printStackTrace();
        }

    }

}
