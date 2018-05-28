package com.imrenagi.publisher;

import java.util.concurrent.ExecutionException;

public interface Producer<T> {

  String publish(T message) throws ExecutionException, InterruptedException;
  void shutdown();

}
