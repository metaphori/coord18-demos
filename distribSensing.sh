#!/bin/bash

./gradlew build

java -cp build/libs/demo-coord18-executable.jar it.unibo.demos.coord18.DistributedSensingLauncher