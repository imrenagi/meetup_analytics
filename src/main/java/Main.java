import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.*;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.LineStringProcessor;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * Created by imrenagi on 2/24/17.
 */
public class Main {

    public static void main(String[] args) {
        try {
            test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test() throws InterruptedException {
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(10000);

        Hosts test = new HttpHosts("http://stream.meetup.com");

        Authentication hosebirdAuth = new OAuth1("consumerKey", "consumerSecret", "token", "secret");

        ClientBuilder builder = new ClientBuilder()
                .name("Meetup-open-event")                              // optional: mainly for the logs
                .hosts(test)
                .authentication(hosebirdAuth)
                .endpoint("/2/open_events", "GET")
                .processor(new LineStringProcessor(msgQueue));

        BasicClient hosebirdClient = builder.build();
        // Attempts to establish a connection.
        hosebirdClient.connect();

        // on a different thread, or multiple different threads....
        // Do whatever needs to be done with messages
        while (!hosebirdClient.isDone()) {
            String msg = msgQueue.poll(10, TimeUnit.SECONDS);
            if (msg == null) {
                System.out.println("Did not receive a message in 5 seconds");
            } else {
                System.out.println(msg);
            }
        }

        if (hosebirdClient.isDone()) {
            System.out.println("Client connection closed unexpectedly: " + hosebirdClient.getExitEvent().getMessage());
        }

        hosebirdClient.stop();
    }
}
