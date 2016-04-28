/*
* Copyright 2016 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package io.vertx.amqp.bridge.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import io.vertx.proton.ProtonConnection;
import io.vertx.proton.ProtonReceiver;

public class AmqpConsumerImpl implements MessageConsumer<JsonObject> {

  private BridgeImpl bridge;
  private ProtonReceiver receiver;
  private MessageTranslatorImpl translator;

  public AmqpConsumerImpl(BridgeImpl bridge, ProtonConnection connection, String amqpAddress) {
    this.bridge = bridge;
    receiver = connection.createReceiver(amqpAddress);
    receiver.open(); // TODO: withhold credit until handler registered? buffer messages arriving before handler?
    translator = new MessageTranslatorImpl();
  }

  @Override
  public MessageConsumer<JsonObject> exceptionHandler(Handler<Throwable> handler) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  @Override
  public MessageConsumer<JsonObject> handler(final Handler<Message<JsonObject>> handler) {
    // TODO: complete
    receiver.handler((delivery, protonMessage) -> {
      JsonObject body = translator.convertToJsonObject(protonMessage);
      Message<JsonObject> vertxMessage = new AmqpMessageImpl(body, bridge, protonMessage);

      handler.handle(vertxMessage);
    });

    return this;
  }

  @Override
  public MessageConsumer<JsonObject> pause() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  @Override
  public MessageConsumer<JsonObject> resume() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  @Override
  public MessageConsumer<JsonObject> endHandler(Handler<Void> endHandler) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  @Override
  public ReadStream<JsonObject> bodyStream() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isRegistered() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  @Override
  public String address() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  @Override
  public MessageConsumer<JsonObject> setMaxBufferedMessages(int maxBufferedMessages) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  @Override
  public int getMaxBufferedMessages() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  @Override
  public void completionHandler(Handler<AsyncResult<Void>> completionHandler) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  @Override
  public void unregister() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }

  @Override
  public void unregister(Handler<AsyncResult<Void>> completionHandler) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException();
  }
}