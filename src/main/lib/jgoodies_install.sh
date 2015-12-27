#!/bin/bash

mvn install:install-file -Dfile=jgoodies-common-1.7.0.jar -DgroupId=com.jgoodies -DartifactId=common -Dversion=1.7.0 -Dpackaging=jar
mvn install:install-file -Dfile=jgoodies-forms-1.7.2.jar -DgroupId=com.jgoodies -DartifactId=forms -Dversion=1.7.2 -Dpackaging=jar
