#!/bin/bash

file="pom.xml"
apt-get install -y xmlstarlet

VERSION=$(xmlstarlet sel -N p=http://maven.apache.org/POM/4.0.0 -t -v "/p:project/p:version" pom.xml)

echo "    "
echo ${VERSION}
echo "    "
echo "    "
IFS='.' read -r -a arr <<< "$VERSION"
((arr[2]++))
result=$(IFS="."; echo "${arr[*]}")

echo "NewVersion"
echo $result
echo "    "
echo " "

xmlstarlet ed -L -N p=http://maven.apache.org/POM/4.0.0 -u "/p:project/p:version" -v $result pom.xml

apt-get remove xmlstarlet -y
