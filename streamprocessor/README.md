Run The Stream Pipeline
```
mvn compile exec:java -X\
    -Dexec.mainClass=com.imrenagi.streamprocessor.pipeline.RSVPStreamPipeline\
    -Dexec.args="--project=<GCP_PROJECT_NAME>\
          --zone=<GCP_ZONE>\
          --outputBucket=<BUCKET_OUTPUT_NAME>\
          --subscriptionId=<PUBSUB_FULL_SUBSCRIPTION_NAME>\
          --maxNumWorkers=1\
          --bigtableProjectId=<BIGTABLE_PROJECT_ID>\
          --bigtableInstanceId=<BIGTABLE_INSTANCE_ID>\
          --bigtableTableId=<BIGTABLE_TABLE_NAME>"
```

Run The Batch Pipeline
```
mvn compile exec:java -X\
    -Dexec.mainClass=com.imrenagi.streamprocessor.pipeline.RSVPSBatchPipeline\
    -Dexec.args="--project=<GCP_PROJECT_NAME>\
          --zone=<GCP_ZONE>\
          --inputBucket=<GCS_INPUT_BUCKET>\
          --inputPrefix=2018/05/27/*.json\
          --maxNumWorkers=3\
          --bigtableProjectId=<BIGTABLE_PROJECT_ID>\
          --bigtableInstanceId=<BIGTABLE_INSTANCE_ID>\
          --bigtableTableId=<BIGTABLE_TABLE_NAME>"
```
