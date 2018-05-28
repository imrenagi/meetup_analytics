package com.imrenagi.publisher;

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.processor.LineStringProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.OAuth1;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeetupOpenEventsPublisher {

  private final static String HOST_ADDRESS = "http://stream.meetup.com";
  private final static String ENDPOINT = "/2/open_events";

  private final static Logger logger = LoggerFactory.getLogger(MeetupOpenEventsPublisher.class);

  public static void main(String[] args) throws Exception {

    String projectId = System.getenv("GCP_PROJECT_ID");
    String topic = System.getenv("GCP_OPEN_EVENTS_TOPIC_NAME");

    ProjectTopicName topicName = ProjectTopicName.of(projectId, topic);
    Publisher publisher = null;
    publisher = Publisher.newBuilder(topicName).build();

    BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(10000);
    ClientBuilder builder = new ClientBuilder()
        .name("Meetup-open-events-stream")                              // optional: mainly for the logs
        .hosts(new HttpHosts(HOST_ADDRESS))
        .authentication(new OAuth1("consumerKey",
            "consumerSecret",
            "token",
            "secret"))
        .endpoint(ENDPOINT, "GET")
        .processor(new LineStringProcessor(msgQueue));
    BasicClient hosebirdClient = builder.build();
    hosebirdClient.connect();

    while (!hosebirdClient.isDone()) {
      String msg = msgQueue.poll(10, TimeUnit.SECONDS);
      if (msg == null) {
        logger.info("Did not receive a message in 10 seconds");
      } else {
        ByteString data = ByteString.copyFromUtf8(msg);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
        ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
        logger.info("published with message ID: " + messageIdFuture.get());
      }
    }

    if (hosebirdClient.isDone()) {
      logger.error("Client connection closed unexpectedly: " + hosebirdClient.getExitEvent().getMessage());
    }

    hosebirdClient.stop();
    if (publisher != null) {
      publisher.shutdown();
    }
  }

}
