package com.murglin.consulting.cloudant.bigdocs;

import com.cloudant.client.api.ClientBuilder;
import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;

import static com.murglin.consulting.cloudant.bigdocs.DatabaseSkipper.shouldSkipDb;

@Slf4j
public class CloudantDatabasesChecker {

    public static void printDatabasesWithDocsBiggerThanGivenSizeForGivenCluster(final Triple<String, String, String> credentials, final int maxDocSizeBytes) {

        final var cloudantClusterName = credentials.getLeft();
        final var userName = credentials.getMiddle();
        final var password = credentials.getRight();

        try {
            final var client = ClientBuilder.account(userName)
                    .username(userName)
                    .password(password)
                    .build();

            log.info("Starting checking documents sizes for cluster '{}'", cloudantClusterName);
            final var allExistingDatabasesNames = client.getAllDbs();
            for (final var dbName : allExistingDatabasesNames) {
                if (shouldSkipDb(dbName)) {
                    log.info("Skipping checking db with name '{}' in cluster '{}'", dbName, cloudantClusterName);
                    continue;
                }
                log.info("Starting checking documents sizes for database '{}' in cluster '{}'", dbName, cloudantClusterName);
                final var db = client.database(dbName, false);
                final var allDocsIds = db.getAllDocsRequestBuilder().build().getResponse().getDocIds();
                log.info("There are '{}' documents to check for db '{}' in cluster '{}'", allDocsIds.size(), dbName, cloudantClusterName);
                for (final var docId : allDocsIds) {
                    final var docSizeBytes = ByteStreams.toByteArray(db.find(docId)).length;
                    if (docSizeBytes > maxDocSizeBytes) {
                        // report db in the sys out if theres at least one doc with size > maxSize and escape from the loop to another db
                        log.warn("Database '{}' in cluster '{}' contains document with size '{}' > '{}' (max size specified) bytes. Doc id is '{}'", dbName, cloudantClusterName, docSizeBytes, maxDocSizeBytes, docId);
                        break;
                    }
                }
                log.info("Finished checking documents sizes for database '{}' in cluster '{}'", dbName, cloudantClusterName);
            }
            log.info("Finished checking documents sizes for all dbs in cluster '{}'", cloudantClusterName);
        } catch (final Exception e) {
            log.error("Cannot check documents sizes for cluster '{}'", cloudantClusterName, e);
        }
    }
}
