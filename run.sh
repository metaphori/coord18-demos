#!/bin/bash

./gradlew build

java -jar build/libs/coord18-demos-executable.jar $1
