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

/**
 * = Vert.x AMQP Bridge
 *
 * This component provides AMQP 1.0 producer and consumer support via a bridging layer implementing the Vert.x event bus
 * MessageProducer and MessageConsumer APIs over the top of link:https://github.com/vert-x3/vertx-proton/[vertx-proton].
 *
 * WARNING: this module has the tech preview status, this means the API can change between versions.
 *
 * == Using Vert.x AMQP Bridge
 *
 * To use Vert.x AMQP Bridge, add the following dependency to the _dependencies_ section of your build descriptor:
 *
 * * Maven (in your `pom.xml`):
 *
 * [source,xml,subs="+attributes"]
 * ----
 * <dependency>
 *   <groupId>${maven.groupId}</groupId>
 *   <artifactId>${maven.artifactId}</artifactId>
 *   <version>${maven.version}</version>
 * </dependency>
 * ----
 *
 * * Gradle (in your `build.gradle` file):
 *
 * [source,groovy,subs="+attributes"]
 * ----
 * compile ${maven.groupId}:${maven.artifactId}:${maven.version}
 * ----
 *
 * == Getting Started
 *
 * === Sending a Message
 *
 * Here is a simple example of creating a {@link io.vertx.core.eventbus.MessageProducer} and sending a message with it.
 * First, an {@link io.vertx.amqpbridge.AmqpBridge} is created and started to establish the underlying AMQP connection,
 * then when this is complete the producer is created and a message sent using it. You can optionally supply a username
 * and password when starting the bridge, as well as supplying {@link io.vertx.amqpbridge.AmqpBridgeOptions} in order
 * to configure various options such as for using SSL connections.
 *
 * [source,$lang]
 * ----
 * {@link examples.VertxAmqpBridgeExamples#example1}
 * ----
 *
 * === Receiving a Message
 *
 * Here is a simple example of creating a {@link io.vertx.core.eventbus.MessageConsumer} and registering a handler with it.
 * First, an {@link io.vertx.amqpbridge.AmqpBridge} is created and started to establish the underlying AMQP connection,
 * then when this is complete the consumer is created and a handler registered that prints the body of incoming AMQP
 * messages.
 *
 * [source,$lang]
 * ----
 * {@link examples.VertxAmqpBridgeExamples#example2}
 * ----
 * Receipt of the AMQP message is accepted automatically as soon as the consumer's handler returns upon delivering the
 * message to the application.
 *
 * == Message Payload
 *
 * === Overview
 *
 * The message payload is passed as a JsonObject with elements representing various sections of the
 * link:http://docs.oasis-open.org/amqp/core/v1.0/os/amqp-core-messaging-v1.0-os.html#section-message-format[AMQP
 * message].
 *
 * The top-level elements supported are:
 *
 * * **body**: The content for the body section of the AMQP message.
 * * **body_type**: An optional String used to indicate whether the "body" element represents an AmqpValue (default), Data, or AmqpSequence section. The values used are "value", "data", and "sequence" respectively.
 * * **header**: An optional  JsonObject representing the elements of the message Header section. Expanded below.
 * * **properties**: An optional JsonObject representing the elements of the message Properties section. Expanded below.
 * * **application_properties**: An optional JsonObject containing any application defined properties(/headers).
 * * **message_annotations**: An optional JsonObject representing any message annotations.
 *
 * The elements of the optional "header" sub-element are:
 *
 * * **durable**: optional boolean indicating whether the message is durable (default false).
 * * **priority**: optional short indicating the message priority (default 4).
 * * **ttl**: optional long indicating ttl in milliseconds (no default). See also 'properties' absolute expiry time.
 * * **first_acquirer**: boolean indicating if this is the first acquirer of the message (default false)
 * * **delivery_count**: long indicating the number of previous _failed_ delivery attempts for message.
 *
 * The elements of the optional "properties" sub-element are:
 *
 * * **to**: optional string with address message is being sent to (no default).
 * * **reply_to**: optional string with address for replies (no default). Set automatically when sent with reply handler.
 * * **message_id**: optional string with message id (no default). Set automatically when sending with reply handler.
 * * **correlation_id**: optional string with correlation id (no default). Set automatically when implicit reply is sent.
 * * **subject**: optional string with message subject (no default).
 * * **group_id**: optional string with message group id (no default).
 * * **group_sequence**: optional long with message group sequence (no default).
 * * **reply_to_group_id**: optional string with message reply to group id (no default).
 * * **content_type**: optional string with message content type (no default). Only for use with Data body sections.
 * * **content_encoding**: optional string with message content encoding (no default).
 * * **creation_time**: optional long with message creation time in milliseconds since the unix epoch (no default).
 * * **absolute_expiry_time**: optional long with absolute expiry time as milliseconds since the unix epoch (no default).
 * * **user_id**: optional string with the id of the user sending the message (no default).
 *
 * === Application Properties
 *
 * To send a message with application properties, the "application_properties" element is added to the payload,
 * containing a JsonObject whose contents represent the application property entries, which have string keys and a
 * object representing a simple value such as String, Boolean, Integer, etc. For example, adding a property to a sent
 * message could look something like:
 *
 * [source,$lang]
 * ----
 * {@link examples.VertxAmqpBridgeExamples#example3}
 * ----
 *
 * When receiving a message with application properties, the "application_properties" element is added to the JsonObject
 * payload returned, containing a JsonObject whose contents represent the application property entries. For example,
 * retrieving an application-property from a received message might look like:
 *
 * [source,$lang]
 * ----
 * {@link examples.VertxAmqpBridgeExamples#example4}
 * ----
 *
 *== Connecting using SSL
 *
 * You can also optionally supply {@link io.vertx.amqpbridge.AmqpBridgeOptions} when creating the bridge in order to
 * configure various options, the most typically used of which are around behaviour for SSL connections.
 *
 * The following is an example of using configuration to create a bridge connecting to a server using SSL,
 * authenticating with a username and password, and supplying a PKCS12 based trust store to verify trust of the server
 * certificate:
 *
 * ----
 * {@link examples.VertxAmqpBridgeExamples#example5}
 * ----
 *
 * The following is an example of using configuration to create a bridge connecting to a server requiring SSL Client
 * Certificate Authentication, supplying both a PKCS12 based trust store to verify trust of the server certificate and
 * also a PKCS12 based key store containing an SSL key and certificate the server can use to verify the client:
 *
 * ----
 * {@link examples.VertxAmqpBridgeExamples#example6}
 * ----
 */
@Document(fileName = "index.adoc")
@ModuleGen(name = "vertx-amqp-bridge", groupPackage = "io.vertx")
package io.vertx.amqpbridge;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;
