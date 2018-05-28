package com.imrenagi.publisher;

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

public class MeetupRSVPPublisher {

  private final static String HOST_ADDRESS = "http://stream.meetup.com";
  private final static String ENDPOINT = "/2/rsvps";

  private final static Logger logger = LoggerFactory.getLogger(MeetupRSVPPublisher.class);

  public static void main(String[] args) throws Exception {

    final String projectId = System.getenv("GCP_PROJECT_ID");
    final String topic = System.getenv("GCP_RSVP_TOPIC_NAME");
    final Producer producer = new PubsubStringPublisher(projectId, topic);

    BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(10000);
    ClientBuilder builder = new ClientBuilder()
        .name("Meetup-rsvp")                              // optional: mainly for the logs
        .hosts(new HttpHosts(HOST_ADDRESS))
        .authentication(new OAuth1("consumerKey",
            "consumerSecret",
            "token",
            "secret"))
        .endpoint(ENDPOINT, "GET")
        .processor(new LineStringProcessor(msgQueue));
    final BasicClient hosebirdClient = builder.build();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      logger.info("RSVP Publisher is shutting down. Freeing some resources...");
      hosebirdClient.stop(); // free-ing streaming listener resource
      producer.shutdown(); // free-ing publisher resource
    }));

    hosebirdClient.connect();

    while (!hosebirdClient.isDone()) {
      String msg = msgQueue.poll(10, TimeUnit.SECONDS);
      if (msg == null) {
        logger.info("Did not receive a message in 10 seconds");
      } else {
        String publishedId = producer.publish(msg);
        logger.info("published with message ID: " + publishedId);
      }
    }

    if (hosebirdClient.isDone()) {
      logger.error("Client connection closed unexpectedly: " + hosebirdClient.getExitEvent().getMessage());
    }
  }
}

