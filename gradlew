#!/bin/sh
if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
fi
echo "Gradle command not found. In AndroidIDE, use its installed Gradle/JDK or configure Gradle 7.5." >&2
exit 1
