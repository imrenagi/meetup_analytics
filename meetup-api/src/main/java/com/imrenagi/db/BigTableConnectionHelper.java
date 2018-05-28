package com.imrenagi.db;

import com.google.cloud.bigtable.hbase.BigtableConfiguration;
import com.google.cloud.bigtable.hbase.BigtableOptionsFactory;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;

public class BigTableConnectionHelper {

  public static Connection getConnection(String projectId, String instanceId) {
    Configuration bigTableConfiguration = new Configuration();
    bigTableConfiguration.set(BigtableOptionsFactory.PROJECT_ID_KEY, projectId);
    bigTableConfiguration.set(BigtableOptionsFactory.INSTANCE_ID_KEY, instanceId);

    return BigtableConfiguration.connect(bigTableConfiguration);
  }

  public static Table getTable(Connection conn, String tableName)
      throws IOException {
    return conn.getTable(TableName.valueOf(tableName));
  }

}
