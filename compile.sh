#!/usr/bin/env bash
set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TARGET_DIR="/Users/jose/Desktop/Files/dev/plugins"

if ! command -v mvn >/dev/null 2>&1; then
  echo "Error: Maven is not installed. Install it with: brew install maven"
  exit 1
fi

cd "$PROJECT_DIR"

VERSION="$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout | tail -n 1)"
if [[ -z "$VERSION" ]]; then
  echo "Error: Could not read project version from pom.xml"
  exit 1
fi

JAR_NAME="RealLogin-${VERSION}.jar"

echo "Building RealLogin..."
mvn clean package

SOURCE_JAR="$PROJECT_DIR/target/$JAR_NAME"
if [[ ! -f "$SOURCE_JAR" ]]; then
  SOURCE_JAR="$(ls -t "$PROJECT_DIR"/target/RealLogin-*.jar 2>/dev/null | head -n 1 || true)"
  if [[ -z "$SOURCE_JAR" || ! -f "$SOURCE_JAR" ]]; then
    echo "Error: Built jar not found in $PROJECT_DIR/target"
    exit 1
  fi
  JAR_NAME="$(basename "$SOURCE_JAR")"
fi

mkdir -p "$TARGET_DIR"
cp "$SOURCE_JAR" "$TARGET_DIR/"

echo "Done: Copied $JAR_NAME to $TARGET_DIR"
