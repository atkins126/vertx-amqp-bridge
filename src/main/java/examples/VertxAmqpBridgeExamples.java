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
package examples;

import io.vertx.amqp.bridge.Bridge;
import io.vertx.amqp.bridge.MessageHelper;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.JsonObject;
import io.vertx.docgen.Source;

@Source(translate = false)
public class VertxAmqpBridgeExamples {

  /*
   * Basic example of creating a producer and sending a message.
   */
  public void example1(Vertx vertx) {
    Bridge bridge = Bridge.bridge(vertx);
    // Start the bridge, then use the event loop thread to process things thereafter.
    bridge.start("localhost", 5672, res -> {
      // Set up a producer using the bridge, send a message with it.
      MessageProducer<JsonObject> producer = bridge.createProducer("myAmqpAddress");

      JsonObject body = new JsonObject();
      body.put(MessageHelper.BODY, "myStringContent");

      producer.send(body);
    });
  }

  /*
   * Basic example of creating a consumer, registering a handler, and printing out received message.
   */
  public void example2(Vertx vertx) {
    Bridge bridge = Bridge.bridge(vertx);
    // Start the bridge, then use the event loop thread to process things thereafter.
    bridge.start("localhost", 5672, res -> {
      // Set up a consumer using the bridge, register a handler for it.
      MessageConsumer<JsonObject> consumer = bridge.createConsumer("myAmqpAddress");
      consumer.handler(vertxMsg -> {
        JsonObject amqpPayload = vertxMsg.body();
        Object amqpBody = amqpPayload.getValue(MessageHelper.BODY);

        System.out.println("Got msg with content:" + amqpBody);
      });
    });
  }
}
