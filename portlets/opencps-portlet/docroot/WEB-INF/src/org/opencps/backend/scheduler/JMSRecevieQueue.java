/**
 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.backend.scheduler;

import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;


/**
 * @author khoavd
 *
 */
public class JMSRecevieQueue implements MessageListener{
	/* (non-Javadoc)
	 * @see com.liferay.portal.kernel.messaging.MessageListener#receive(com.liferay.portal.kernel.messaging.Message)
	 */
	
    private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION = "java:/jms/queue/demoQueue";
    private static final String DEFAULT_USERNAME = "user1";
    private static final String DEFAULT_PASSWORD = "fds@123456";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "remote://localhost:4447";
    private static final Logger _log = Logger.getLogger(JMSRecevieQueue.class.getName());

	
	@Override
	public void receive(Message message)
	    throws MessageListenerException {
	
        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Session session = null;
        //MessageProducer producer = null;
        MessageConsumer consumer = null;
        Destination destination = null;
        TextMessage textMessage = null;
        BytesMessage bytesMessage = null;
        Context context = null;

        try {
            // Set up the context for the JNDI lookup
            final Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
            env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
            env.put(Context.SECURITY_PRINCIPAL, System.getProperty("username", DEFAULT_USERNAME));
            env.put(Context.SECURITY_CREDENTIALS, System.getProperty("password", DEFAULT_PASSWORD));
            context = new InitialContext(env);

            // Perform the JNDI lookups
            String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
            _log.log(Level.INFO, "Attempting to acquire connection factory \"{0}\"", connectionFactoryString);
            connectionFactory = (ConnectionFactory) context.lookup(connectionFactoryString);

            _log.log(Level.INFO, "Found connection factory \"{0}\" in JNDI", connectionFactoryString);

            String destinationString = System.getProperty("destination", DEFAULT_DESTINATION);

            Queue queue = (Queue) context.lookup(destinationString);
            _log.log(Level.INFO, "Attempting to acquire destination \"{0}\"", destinationString);
            destination = (Destination) context.lookup(destinationString);
            _log.log(Level.INFO, "Found destination \"{0}\" in JNDI", destinationString);

            // Create the JMS connection, session, producer, and consumer
            connection = connectionFactory.createConnection(System.getProperty("username", DEFAULT_USERNAME), System.getProperty("password", DEFAULT_PASSWORD));
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            QueueBrowser browser = session.createBrowser(queue);

            int count = 0;
            Enumeration<?> messages = browser.getEnumeration();
            while (messages.hasMoreElements()) {
            	 javax.jms.Message browsedMsg = ( javax.jms.Message) messages.nextElement();
                System.out.println(browsedMsg.getJMSMessageID());
                count++;
            }
            System.out.println("org.opencps.web.controler.ReceiveAction.receive(): Queue size: " + count);

            browser.close();

            consumer = session.createConsumer(destination);
            // consumer = session.createConsumer(destination);
            connection.start();

            while (true) {
            	 javax.jms.Message m = consumer.receive();
                if (m != null) {
                    if (m instanceof TextMessage) {
                        textMessage = (TextMessage) m;
                        System.out.println("Reading message: "
                                + textMessage.getText());
                    } else {
                        System.out.println("Object message!...");

                    }
                }
            }

        } catch (Exception e) {
            _log.severe(e.getMessage());

        } finally {
            if (context != null) {
                try {
	                context.close();
                }
                catch (NamingException e) {
	                e.printStackTrace();
                }
            }

            // closing the connection takes care of the session, producer, and consumer
            if (connection != null) {
	                try {
	                    connection.close();
                    }
                    catch (JMSException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
                    }
            }
        }
	}
	   
}
