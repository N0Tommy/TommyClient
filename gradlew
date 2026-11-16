#!/usr/bin/env sh
set -eu
VERSION="8.12.1"
if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
fi
ROOT="${GRADLE_USER_HOME:-$HOME/.gradle}/wrapper/dists/tommyclient/$VERSION"
BIN="$ROOT/gradle-$VERSION/bin/gradle"
if [ ! -x "$BIN" ]; then
  mkdir -p "$ROOT"
  ZIP="$ROOT/gradle-$VERSION-bin.zip"
  echo "Downloading Gradle $VERSION..."
  if command -v curl >/dev/null 2>&1; then
    curl -fL "https://services.gradle.org/distributions/gradle-$VERSION-bin.zip" -o "$ZIP"
  elif command -v wget >/dev/null 2>&1; then
    wget -O "$ZIP" "https://services.gradle.org/distributions/gradle-$VERSION-bin.zip"
  else
    echo "Install Gradle $VERSION or install curl/wget to bootstrap it." >&2
    exit 1
  fi
  if command -v unzip >/dev/null 2>&1; then
    unzip -q -o "$ZIP" -d "$ROOT"
  else
    echo "The unzip utility is required to unpack Gradle." >&2
    exit 1
  fi
fi
exec "$BIN" "$@"
