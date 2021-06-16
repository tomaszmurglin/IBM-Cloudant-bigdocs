package com.murglin.consulting.cloudant.bigdocs;

import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.concurrent.Executors;

public class App {

    // User of this tool has to take into account any potential difference of the document size due to different encoding between this tool in Cloudant
    private static final int MAX_DOC_SIZE_BYTES = 800000; //0.8MB

    public static void main(final String[] args) {
        //TODO move to some secretes store like Vault etc.
        final var cloudantCredentialsForAllClusters = List.of(
                Triple.of("Staging", "accountNameGoesHere", "passwordGoesHere"),
                Triple.of("SharedQA", "accountNameGoesHere", "passwordGoesHere"),
                Triple.of("Production EU", "accountNameGoesHere", "passwordGoesHere"),
                Triple.of("Production US", "accountNameGoesHere", "passwordGoesHere")
        );
        final var executor = Executors.newFixedThreadPool(cloudantCredentialsForAllClusters.size());
        cloudantCredentialsForAllClusters
                .forEach(cloudantCredentials -> executor.submit(() -> CloudantDatabasesChecker.printDatabasesWithDocsBiggerThanGivenSizeForGivenCluster(cloudantCredentials, MAX_DOC_SIZE_BYTES)));
        executor.shutdown();
    }
}
