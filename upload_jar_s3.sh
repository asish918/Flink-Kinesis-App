#!/bin/bash

# Check if S3 bucket name is provided
if [ -z "$1" ]; then
  echo "Usage: $0 <s3-bucket-name>"
  exit 1
fi

S3_BUCKET=$1

# Build the shadow JAR using Gradle
./gradlew shadowJar

# Find the generated JAR file
JAR_FILE=$(find build/libs -name '*-all.jar')

# Check if the JAR file was found
if [ -z "$JAR_FILE" ]; then
  echo "Error: JAR file not found."
  exit 1
fi

# Delete the existing object from S3
echo "Deleting object from S3: s3://$S3_BUCKET/$JAR_FILE"
aws s3 rm "s3://$S3_BUCKET/$JAR_FILE"
if [ $? -ne 0 ]; then
  echo "Failed to delete object from S3. Exiting."
  exit 1
fi

echo "Object deleted successfully."

# Upload the JAR file to the specified S3 bucket
aws s3 cp "$JAR_FILE" "s3://$S3_BUCKET/"

# Check if the upload was successful
if [ $? -eq 0 ]; then
  echo "JAR file successfully uploaded to s3://$S3_BUCKET/"
else
  echo "Error: Failed to upload JAR file to S3."
  exit 1
fi