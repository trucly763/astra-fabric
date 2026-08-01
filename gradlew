#!/bin/bash
# Download and run gradle
GRADLE_VERSION=8.8
GRADLE_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}"
GRADLE_DIR="$GRADLE_HOME/gradle-${GRADLE_VERSION}"

if [ ! -d "$GRADLE_DIR" ]; then
  echo "Installing Gradle ${GRADLE_VERSION}..."
  mkdir -p "$GRADLE_HOME"
  cd "$GRADLE_HOME"
  curl -s -L "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" -o gradle-${GRADLE_VERSION}-bin.zip
  unzip -q gradle-${GRADLE_VERSION}-bin.zip
  rm gradle-${GRADLE_VERSION}-bin.zip
fi

exec "$GRADLE_DIR/bin/gradle" "$@"
