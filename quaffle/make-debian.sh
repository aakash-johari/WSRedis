#!/bin/bash -e

SERVICE=quaffle-ws-service

MAJOR_VERSION=1
MINOR_VERSION=$(git log -n1 --format="%ct")
VERSION=${MAJOR_VERSION}.${MINOR_VERSION}
PACKAGE_HOME=${SERVICE}_${VERSION}

echo "===================== Building jar file ===================="

mvn clean install -DskipTests

echo "===================== Copying jar file ====================="
mkdir -p ${PACKAGE_HOME}/usr/share/$SERVICE

cp -r $SERVICE/* ${PACKAGE_HOME}/

cp target/${SERVICE}*.jar ${PACKAGE_HOME}/usr/share/$SERVICE/$SERVICE.jar

echo "===================== Building Deb ========================="

sed -i -e "s/__VERSION__/$VERSION/g" ${PACKAGE_HOME}/DEBIAN/control

fakeroot dpkg-deb --build ${PACKAGE_HOME}

rm -rf ${PACKAGE_HOME}
