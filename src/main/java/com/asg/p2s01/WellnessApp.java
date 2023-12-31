package com.asg.p2s01;

import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class WellnessApp {
     public static void main(String[] args) {

        try {
            InitialContext initialContext = new InitialContext();
            Topic empTopic = (Topic) initialContext.lookup("topic/topic01");

            try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61616", "admin",
                    "admin");
                    JMSContext jmsContext = cf.createContext()) {

                // ObjectMessage msg = jmsContext.createObjectMessage(emp);
                // jmsContext.createProducer().send(empTopic, emp);
                Employee emp = jmsContext.createConsumer(empTopic).receiveBody(Employee.class);

                System.out.println("Wellness: received " + emp.getName() + " from topic");

                

            }

        } catch (NamingException e) {
            e.printStackTrace();
        }

    }
    
}
