#!/bin/bash

java -cp fuseki-server.jar:./run/extra/* jena.textindexer  --desc=run/configuration/sempic.ttl
