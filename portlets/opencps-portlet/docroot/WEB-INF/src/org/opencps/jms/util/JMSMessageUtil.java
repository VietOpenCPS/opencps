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

package org.opencps.jms.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import javax.jms.BytesMessage;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import org.opencps.jms.context.JMSContext;
import org.opencps.jms.context.JMSContextFactory;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 * @author trungnt
 */
public class JMSMessageUtil {

	/**
	 * @param context
	 * @return
	 */
	public static BytesMessage createByteMessage(JMSContext context) {

		try {
			context.createByteMessage();
			return context.getBytesMessage();

		}
		catch (Exception e) {
			_log.error(e);
			return null;
		}

	}

	/**
	 * @param companyId
	 * @param code
	 * @param remote
	 * @param channelName
	 * @return
	 */
	public static JMSContext createConsumer(
		long companyId, String code, boolean remote, String channelName) {

		JMSContext context = null;
		try {
			context =
				JMSContextFactory.getInstance(companyId, code, remote,
					channelName);
			context.createConsumer();
			context.start();

		}
		catch (Exception e) {
			_log.error(e);
		}

		return context;
	}

	/**
	 * @param context
	 * @return
	 */
	public static MapMessage createMapMessage(JMSContext context) {

		try {
			context.createMapMessage();
			return context.getMapMessage();

		}
		catch (Exception e) {
			_log.error(e);
			return null;
		}

	}

	/**
	 * @param context
	 * @return
	 */
	public static ObjectMessage createObjectMessage(JMSContext context) {

		try {
			context.createObjectMessage();
			return context.getObjectMessage();

		}
		catch (Exception e) {
			_log.error(e);
			return null;
		}

	}

	/**
	 * @param companyId
	 * @param code
	 * @param remote
	 * @param channelName
	 * @return
	 */
	public static JMSContext createProducer(
		long companyId, String code, boolean remote, String channelName) {

		JMSContext context = null;
		try {
			context =
				JMSContextFactory.getInstance(companyId, code, remote,
					channelName);
			context.createProducer();
			context.start();

		}
		catch (Exception e) {
			_log.error(e);
		}

		return context;
	}

	/**
	 * @param context
	 */
	public static StreamMessage createStreamMessage(JMSContext context) {

		try {
			context.createStreamMessage();
			return context.getStreamMessage();

		}
		catch (Exception e) {
			_log.error(e);
			return null;
		}

	}

	/**
	 * @param context
	 */
	public static TextMessage createTextMessage(JMSContext context) {

		try {
			context.createTextMessage();
			return context.getTextMessage();

		}
		catch (Exception e) {
			_log.error(e);
			return null;
		}

	}

	/**
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static byte[] convertObjectToByteArray(Object object)
		throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] bytes = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(object);
			bytes = bos.toByteArray();

		}
		catch (Exception e) {
			_log.error(e);
		}
		finally {
			if (out != null) {
				out.close();
			}
			bos.close();
		}

		return bytes;
	}

	/**
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	public static Object convertByteArrayToObject(byte[] bytes)
		throws IOException {

		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

		ObjectInput in = null;
		Object object = null;
		try {
			in = new ObjectInputStream(bis);

			object = in.readObject();
		}
		catch (Exception e) {
			_log.error(e);
		}
		finally {

			bis.close();
			if (in != null) {

				in.close();
			}
		}
		return object;
	}

	/**
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] convertInputStreamToByteArray(InputStream inputStream)
		throws IOException {

		byte[] buffer = new byte[8192];
		byte[] bytes = null;
		int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
		bytes = output.toByteArray();
		output.close();
		inputStream.close();
		return bytes;
	}

	/**
	 * @param bytes
	 * @param path
	 * @throws IOException
	 */
	public static void byteArrayToFile(byte[] bytes, String path)
		throws IOException {

		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(path);
			stream.write(bytes);
		}
		catch (Exception e) {
			_log.error(e);
		}
		finally {
			if (stream != null) {
				stream.flush();
				stream.close();
			}
		}
	}

	private static Log _log =
		LogFactoryUtil.getLog(JMSMessageUtil.class.getName());
}
