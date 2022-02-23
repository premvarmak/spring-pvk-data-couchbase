# spring-pvk-data-couchbase - Demo of multi bucket & multi cluster connections

This is a sample application which connects to a couchbase cluster setup locally.

I have used couchbase default docker setup and added 3 buckets to my cluster.

I am using Spring Data Couchbase default setup to connect to Couchbase.

Please check application.yml for DB details & NephosCbConfig.java where we are connecting to Database.

I have also created NephosCb2Config.java to connect to another cluster but that connection is not being recognised by Spring.

For each bucket I have created, reposiroty, Service, Controller classes in respective packages.

