/**
 * Copyright (c) 2017, FinancialForce.com, inc
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 *   are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 * - Neither the name of the FinancialForce.com, inc nor the names of its contributors
 *      may be used to endorse or promote products derived from this software without
 *      specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 *  THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 *  OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 *  OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/

package com.financialforce.orizuru.transport.rabbitmq;

import com.rabbitmq.client.Channel;

import org.apache.avro.generic.GenericContainer;

import com.financialforce.orizuru.AbstractPublisher;
import com.financialforce.orizuru.exception.publisher.OrizuruPublisherException;
import com.financialforce.orizuru.message.Context;

/**
 * RabbitMQ implementation of the Orizuru {@link AbstractPublisher}.
 */
public class DefaultPublisher<O extends GenericContainer> extends AbstractPublisher<O> {

	private Channel channel;

	public DefaultPublisher(Channel channel, String queueName) {
		super(queueName);
		this.channel = channel;
	}

	/* (non-Javadoc)
	 * @see com.financialforce.orizuru.AbstractPublisher#publish(com.financialforce.orizuru.message.Context, org.apache.avro.generic.GenericContainer)
	 */
	@Override
	public byte[] publish(Context context, O message) throws OrizuruPublisherException {

		byte[] outgoingMessage = null;

		try {

			outgoingMessage = super.publish(context, message);

			channel.basicPublish("", queueName, null, outgoingMessage);

		} catch (Exception ex) {
			throw new OrizuruPublisherException(ex);
		}

		return outgoingMessage;

	}

}
