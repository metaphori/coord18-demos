#!/bin/bash

if [ -e build/libs/demo-coord18-executable.jar ]
then
    :
else
    echo 'Building the project.'
    ./gradlew build
fi

java -jar build/libs/demo-coord18-executable.jar $1