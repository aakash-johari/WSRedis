#!/bin/bash -e

SERVICE=quaffle-frontend-service

MAJOR_VERSION=1
MINOR_VERSION=$(git log -n1 --format="%ct")
VERSION=${MAJOR_VERSION}.${MINOR_VERSION}
PACKAGE_HOME=${SERVICE}_${VERSION}

echo "================= Updating the NPM cache ==============="
npm install

echo "===================== Building code ===================="
npm run build

echo "===================== Copying js/html/css files ====================="
mkdir -p ${PACKAGE_HOME}/usr/share/$SERVICE
mkdir -p ${PACKAGE_HOME}/usr/share/$SERVICE/dist

cp -r $SERVICE/* ${PACKAGE_HOME}/

cp dist/* ${PACKAGE_HOME}/usr/share/$SERVICE/dist/

cp server.js ${PACKAGE_HOME}/usr/share/$SERVICE/$SERVICE.js

npm install --prefix ${PACKAGE_HOME}/usr/share/$SERVICE express

echo "===================== Building Deb ========================="

sed -i -e "s/__VERSION__/$VERSION/g" ${PACKAGE_HOME}/DEBIAN/control

fakeroot dpkg-deb --build ${PACKAGE_HOME}

rm -rf ${PACKAGE_HOME}
