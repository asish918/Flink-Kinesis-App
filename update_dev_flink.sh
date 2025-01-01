#!/bin/bash

# Variables
GRADLE_COMMAND=":analytics:build -x test -x check --stacktrace -PsourceCompatibility=11 -PtargetCompatibility=11 -PcallerProject=analytics"   # Replace with your desired Gradle task
JAR_PATH="analytics/build/libs/analytics.jar" # Replace with the path to the generated JAR file
S3_BUCKET="artifacts-ccab-fltestuseast1"        # Replace with your S3 bucket name
S3_OBJECT_KEY="engine/fltest/analytics/analytics.jar" # Replace with the S3 object key

cd /Users/asishmahapatra/Desktop/Developer/Company/ccab

# Run the Gradle command
echo "Running Gradle command: $GRADLE_COMMAND"
./gradlew $GRADLE_COMMAND
if [ $? -ne 0 ]; then
  echo "Gradle command failed. Exiting."
  exit 1
fi

# Check if the JAR file was generated
if [ ! -f "$JAR_PATH" ]; then
  echo "JAR file not found at $JAR_PATH. Exiting."
  exit 1
fi

echo "Gradle build succeeded and JAR file generated."

# Delete the existing object from S3
echo "Deleting object from S3: s3://$S3_BUCKET/$S3_OBJECT_KEY"
aws s3 rm "s3://$S3_BUCKET/$S3_OBJECT_KEY"
if [ $? -ne 0 ]; then
  echo "Failed to delete object from S3. Exiting."
  exit 1
fi

echo "Object deleted successfully."

# Upload the new JAR file to S3
echo "Uploading JAR file to S3: s3://$S3_BUCKET/$S3_OBJECT_KEY"
aws s3 cp "$JAR_PATH" "s3://$S3_BUCKET/$S3_OBJECT_KEY"
if [ $? -ne 0 ]; then
  echo "Failed to upload JAR file to S3. Exiting."
  exit 1
fi

echo "JAR file uploaded successfully."

exit 0
