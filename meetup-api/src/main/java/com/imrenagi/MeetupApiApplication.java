package com.imrenagi;

import com.imrenagi.db.BigTableConnectionHelper;
import com.imrenagi.resources.HealthCheckResource;
import com.imrenagi.resources.RSVPAnalyticsResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.io.IOException;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;

public class MeetupApiApplication extends Application<MeetupApiConfiguration> {

  public static void main(final String[] args) throws Exception {
    new MeetupApiApplication().run(args);
  }

  @Override
  public String getName() {
    return "meetup-api";
  }

  @Override
  public void initialize(final Bootstrap<MeetupApiConfiguration> bootstrap) {
  }

  @Override
  public void run(final MeetupApiConfiguration configuration,
      final Environment environment) throws IOException {
    Connection bigTableConnection = BigTableConnectionHelper
        .getConnection(configuration.getBigTableProjectId(), configuration.getBigTableInstanceId());

    Table table = BigTableConnectionHelper.getTable(bigTableConnection, configuration.getRsvpCountTable());

    final RSVPAnalyticsResource resource = new RSVPAnalyticsResource(table);
    final HealthCheckResource healthCheck = new HealthCheckResource(configuration.getTemplate());

    environment.healthChecks().register("template", healthCheck);
    environment.jersey().register(resource);
  }

}
