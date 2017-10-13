#!/bin/sh

function verifySet {
    for i in $*
    do
        if [ -z ${!i} ]
        then
           echo "$i is not set"
           exit 1
        fi
    done
}

if [ ! -f env.sh ]
then
  echo "You need to create an env.sh file setting values for STRIDE_CREDENTIALS, MEDIA_CREDENTIALS, USER_CREDENTIALS, \
  BASE_URL, DARK_SKY_KEY, GOOGLE_GEOCODING_KEY and APP_CLASS_NAME\nDON'T add env.sh to version control."
  exit 1
else
  . ./env.sh
fi
verifySet STRIDE_CREDENTIALS MEDIA_CREDENTIALS USER_CREDENTIALS \
  BASE_URL DARK_SKY_KEY GOOGLE_GEOCODING_KEY APP_CLASS_NAME
mvn package && \
sam package \
    --template-file template.yaml \
    --s3-bucket lambda-test.kablambda.org  \
    --output-template-file packaged.yaml &&
aws cloudformation deploy \
    --template-file packaged.yaml \
    --stack-name tdavies-test-stack \
    --parameter-overrides \
        StrideCredentials=${STRIDE_CREDENTIALS:?} \
        MediaCredentials=${MEDIA_CREDENTIALS:?} \
        UserCredentials=${USER_CREDENTIALS:?} \
        DynamoDBEndpoint= \
        BaseUrl=${BASE_URL:?} \
        DarkSkyKey=${DARK_SKY_KEY:?} \
        GoogleGeocodingKey=${GOOGLE_GEOCODING_KEY:?} \
        AppClassName=${APP_CLASS_NAME:?} \
    --capabilities CAPABILITY_IAM