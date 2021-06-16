# ibm-cloudant-bigdocs
Java CLI tool that checks if in given IBM Cloudant cluster there are documents larger than defined size and if they are any then it prints the database name where they are located in standard system out.
Useful for example when migrating from single tenant IBM Cloudant cluster to the multi-tenant cluster where doc limit is currently max. 1MB.

Useful links:
* https://github.com/cloudant/java-cloudant

TODO:
* unit tests
* parallelize even more so each db will be scanned on dedicated thread ?
* make db skipper configurable
* support various authentication strategies
