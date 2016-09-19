/**
 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.jms.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.NamingException;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;
import org.hornetq.jms.client.HornetQConnectionFactory;
import org.opencps.util.PortletUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.SystemException;

/**
 * @author trungnt
 */
public class JMSHornetqContext {

	/**
	 * @param companyId
	 * @param code
	 * @param remote
	 * @param channelName
	 * @param queueName
	 * @param lookup
	 * @param mom
	 * @throws NamingException
	 * @throws SystemException
	 * @throws Exception
	 */
	public JMSHornetqContext(
		long companyId, String code, boolean remote, String channelName,
		String queueName, String lookup, String mom)
		throws NamingException, SystemException, Exception {

		init(companyId, code, remote, channelName, queueName, lookup, mom);
	}

	/**
	 * @param companyId
	 * @param code
	 * @param remote
	 * @param channelName
	 * @param queueName
	 * @param lookup
	 * @param mom
	 * @throws SystemException
	 * @throws NamingException
	 * @throws JMSException
	 */
	protected void init(
		long companyId, String code, boolean remote, String channelName,
		String queueName, String lookup, String mom)
		throws SystemException, NamingException, JMSException {

		Properties properties =
			PortletUtil.getJMSContextProperties(
				companyId, code, remote, channelName, queueName, lookup, mom);

		if (properties != null) {
			System.out.println("////////// " +
				properties.getProperty(WebKeys.JMS_PROVIDER_PORT));
			System.out.println("////////// " +
				properties.getProperty(WebKeys.JMS_PROVIDER_URL));
			System.out.println("////////// " +
				properties.getProperty(WebKeys.JMS_QUEUE));
			System.out.println("////////// " +
				properties.getProperty(Context.SECURITY_PRINCIPAL));
			System.out.println("////////// " +
				properties.getProperty(Context.SECURITY_CREDENTIALS));

			Map<String, Object> connectionParams =
				new HashMap<String, Object>();
			connectionParams.put(
				TransportConstants.PORT_PROP_NAME,
				properties.getProperty(WebKeys.JMS_PROVIDER_PORT));
			connectionParams.put(
				TransportConstants.HOST_PROP_NAME,
				properties.getProperty(WebKeys.JMS_PROVIDER_URL));
			TransportConfiguration transportConfiguration =
				new TransportConfiguration(
					NettyConnectorFactory.class.getName(), connectionParams);

			HornetQConnectionFactory connectionFactory =
				HornetQJMSClient.createConnectionFactoryWithoutHA(
					JMSFactoryType.CF, transportConfiguration);

			Queue queue =
				HornetQJMSClient.createQueue(properties.getProperty(WebKeys.JMS_QUEUE));

			Connection connection =
				connectionFactory.createConnection(
					properties.getProperty(Context.SECURITY_PRINCIPAL),
					properties.getProperty(Context.SECURITY_CREDENTIALS));

			Session session =
				connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			this.setConnectionFactory(connectionFactory);
			this.setProperties(properties);
			this.setConnection(connection);
			this.setQueue(queue);
			this.setSession(session);
		}
	}

	/**
	 * @throws Exception
	 */
	public void createConsumer()
		throws Exception {

		MessageConsumer consumer = _session.createConsumer(_queue);

		setMessageConsumer(consumer);
	}

	/**
	 * @throws Exception
	 */
	public void createProducer()
		throws Exception {

		MessageProducer producer = _session.createProducer(_queue);
		setMessageProducer(producer);

	}

	/**
	 * @throws Exception
	 */
	public void createByteMessage()
		throws Exception {

		BytesMessage bytesMessage = _session.createBytesMessage();
		setBytesMessage(bytesMessage);
	}

	/**
	 * @throws Exception
	 */
	public void createMapMessage()
		throws Exception {

		MapMessage mapMessage = _session.createMapMessage();
		setMapMessage(mapMessage);
	}

	/**
	 * @throws Exception
	 */
	public void createObjectMessage()
		throws Exception {

		ObjectMessage objectMessage = _session.createObjectMessage();
		setObjectMessage(objectMessage);
	}

	/**
	 * @throws Exception
	 */
	public void createStreamMessage()
		throws Exception {

		StreamMessage streamMessage = _session.createStreamMessage();
		setStreamMessage(streamMessage);
	}

	/**
	 * @throws Exception
	 */
	public void createTextMessage()
		throws Exception {

		TextMessage textMessage = _session.createTextMessage();
		setTextMessage(textMessage);
	}

	/**
	 * @throws JMSException
	 * @throws NamingException
	 */
	public void destroy()
		throws JMSException, NamingException {

		stop();

		if (_connection != null) {
			_connection.close();;
		}

		if (_messageConsumer != null) {
			_messageConsumer.close();
		}

		if (_messageProducer != null) {
			_messageProducer.close();
		}

		if (_session != null) {
			_session.close();
		}

		_connectionFactory = null;

		_bytesMessage = null;

		_objectMessage = null;

		_streamMessage = null;

		_textMessage = null;

		_properties = null;

		_queue = null;

		_mapMessage = null;

	}

	/**
	 * @throws JMSException
	 */
	public void start()
		throws JMSException {

		if (_connection != null) {
			_connection.start();
		}

	}

	/**
	 * @throws JMSException
	 */
	protected void stop()
		throws JMSException {

		if (_connection != null) {
			_connection.stop();
		}

	}

	public BytesMessage getBytesMessage() {

		return _bytesMessage;
	}

	public Connection getConnection() {

		return _connection;
	}

	public HornetQConnectionFactory getConnectionFactory() {

		return _connectionFactory;
	}

	public MapMessage getMapMessage() {

		return _mapMessage;
	}

	public MessageConsumer getMessageConsumer() {

		return _messageConsumer;
	}

	public MessageProducer getMessageProducer() {

		return _messageProducer;
	}

	public ObjectMessage getObjectMessage() {

		return _objectMessage;
	}

	public Properties getProperties() {

		return _properties;
	}

	public Queue getQueue() {

		return _queue;
	}

	public Session getSession() {

		return _session;
	}

	public void setBytesMessage(BytesMessage bytesMessage) {

		this._bytesMessage = bytesMessage;
	}

	public void setConnection(Connection connection) {

		this._connection = connection;
	}

	public void setConnectionFactory(HornetQConnectionFactory connectionFactory) {

		this._connectionFactory = connectionFactory;
	}

	public void setMapMessage(MapMessage mapMessage) {

		this._mapMessage = mapMessage;
	}

	public void setMessageConsumer(MessageConsumer messageConsumer) {

		this._messageConsumer = messageConsumer;
	}

	public void setMessageProducer(MessageProducer messageProducer) {

		this._messageProducer = messageProducer;
	}

	public void setObjectMessage(ObjectMessage objectMessage) {

		this._objectMessage = objectMessage;
	}

	public void setProperties(Properties properties) {

		this._properties = properties;
	}

	public void setQueue(Queue queue) {

		this._queue = queue;
	}

	public void setSession(Session session) {

		this._session = session;
	}

	public StreamMessage getStreamMessage() {

		return _streamMessage;
	}

	public void setStreamMessage(StreamMessage streamMessage) {

		this._streamMessage = streamMessage;
	}

	public TextMessage getTextMessage() {

		return _textMessage;
	}

	public void setTextMessage(TextMessage textMessage) {

		this._textMessage = textMessage;
	}

	private BytesMessage _bytesMessage;

	private Connection _connection;

	private HornetQConnectionFactory _connectionFactory;

	private MapMessage _mapMessage;

	private MessageConsumer _messageConsumer;

	private MessageProducer _messageProducer;

	private ObjectMessage _objectMessage;

	private Properties _properties;

	private Queue _queue;

	private Session _session;

	private StreamMessage _streamMessage;

	private TextMessage _textMessage;

}
