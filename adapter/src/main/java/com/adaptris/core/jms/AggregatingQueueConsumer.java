/*
 * Copyright 2015 Adaptris Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package com.adaptris.core.jms;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.annotation.AutoPopulated;
import com.adaptris.annotation.DisplayOrder;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.ConsumeDestination;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceException;
import com.adaptris.core.services.aggregator.AggregatingConsumerImpl;
import com.adaptris.core.services.aggregator.ConsumeDestinationGenerator;
import com.adaptris.util.TimeInterval;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * {@link com.adaptris.core.services.aggregator.AggregatingConsumer} implementation that allows you to read a separate message(s) from a queue that need to be aggregated
 * with the current message.
 * <p>
 * The JMS Queue and message selector are created from the generated {@link ConsumeDestination#getDestination()} and
 * {@link ConsumeDestination#getFilterExpression()} respectively.
 * <ul>
 * <li>If the first message is received within the correct timeframe (based on {@link #getTimeout()}), then additional messages are
 * waited for based on the same timeout. Once the timeout expires then all the messages are aggregated using the configured
 * aggregator.</li>
 * <li>If the first message is not received within the correct timeframe than an exception is thrown</li>
 * <li>In the worst case scenario, then this consumer will take 2*Timeout to process a single message (e.g. you wait 59 seconds for
 * the first message, and then subsequently wait for another minute if the Timeout is 1 minute).</li>
 * </ul>
 * </p>
 * 
 * @config aggregating-queue-consumer
 * 
 */
@XStreamAlias("aggregating-queue-consumer")
@DisplayOrder(order = {"destination", "messageAggregator", "messageTranslator", "timeout"})
public class AggregatingQueueConsumer extends AggregatingConsumerImpl<AggregatingJmsConsumeService> implements
    AggregatingJmsConsumer {

  private transient Logger log = LoggerFactory.getLogger(this.getClass());
  private static final TimeInterval DEFAULT_TIMEOUT = new TimeInterval(30L, TimeUnit.SECONDS);

  @NotNull
  @AutoPopulated
  private MessageTypeTranslator messageTranslator;
  private TimeInterval timeout;

  public AggregatingQueueConsumer() {
    super();
    setMessageTranslator(new AutoConvertMessageTranslator());
  }

  public AggregatingQueueConsumer(ConsumeDestinationGenerator d) {
    this();
    setDestination(d);
  }

  public void aggregateMessages(AdaptrisMessage msg, AggregatingJmsConsumeService cfg) throws ServiceException {
    ConsumeDestination dest = getDestination().generate(msg);
    MessageConsumer consumer = null;
    ArrayList<AdaptrisMessage> result = new ArrayList<>();
    try {
      startMessageTranslator(cfg, msg.getFactory());
      consumer = cfg.getConnection().retrieveConnection(JmsConnection.class).configuredVendorImplementation()
          .createQueueReceiver(dest, cfg);
      Message first = firstMessage(consumer);
      result.add(getMessageTranslator().translate(first));
      Message next = nextMessage(consumer);
      while (next != null) {
        result.add(getMessageTranslator().translate(next));
        next = nextMessage(consumer);
      }
      getMessageAggregator().joinMessage(msg, result);
    }
    catch (CoreException | JMSException e) {
      rethrowServiceException(e);
    }
    finally {
      close(consumer);
      stop(messageTranslator);
    }
  }

  private Message firstMessage(MessageConsumer consumer) throws JMSException, ServiceException {
    Message result = nextMessage(consumer);
    if (result == null) {
      throw new ServiceException("No Message availabe to read within the expected timeframe");
    }
    return result;
  }

  private Message nextMessage(MessageConsumer consumer) throws JMSException {
    return consumer.receive(timeoutMs());
  }

  @Override
  public void prepare() throws CoreException {
  }

  /**
   * @return the timeout
   */
  public TimeInterval getTimeout() {
    return timeout;
  }

  /**
   * Set the timeout to wait for the correlated message.
   * 
   * @param t the timeout to set, if not specified then it defaults to 30 seconds.
   */
  public void setTimeout(TimeInterval t) {
    this.timeout = t;
  }

  long timeoutMs() {
    return getTimeout() != null ? getTimeout().toMilliseconds() : DEFAULT_TIMEOUT.toMilliseconds();
  }

  private void close(MessageConsumer c) {
    try {
      if (c != null) {
        c.close();
      }
    }
    catch (JMSException ignored) {
      ;
    }
  }

  protected void startMessageTranslator(JmsActorConfig cfg, AdaptrisMessageFactory factory) throws CoreException {
    messageTranslator.registerSession(cfg.currentSession());
    messageTranslator.registerMessageFactory(factory);
    messageTranslator.prepare();
    start(messageTranslator);
  }

  public MessageTypeTranslator getMessageTranslator() {
    return messageTranslator;
  }

  /**
   * Set the jms message translator.
   * 
   * @param translator the translator.
   */
  public void setMessageTranslator(MessageTypeTranslator translator) {
    this.messageTranslator = translator;
  }
}
