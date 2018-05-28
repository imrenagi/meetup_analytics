package com.imrenagi.publisher;

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class PubsubStringPublisher implements Producer<String> {

  private Publisher publisher;

  public PubsubStringPublisher(String projectId, String topic) throws IOException {
    ProjectTopicName topicName = ProjectTopicName.of(projectId, topic);
    this.publisher = com.google.cloud.pubsub.v1.Publisher.newBuilder(topicName).build();
  }

  @Override
  public String publish(String message) throws ExecutionException, InterruptedException {
    ByteString data = ByteString.copyFromUtf8(message);
    PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
    ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
    return messageIdFuture.get();
  }

  @Override
  public void shutdown() {
    if (this.publisher != null) {
      try {
        this.publisher.shutdown();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
