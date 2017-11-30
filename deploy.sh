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

required_params="STRIDE_CREDENTIALS MEDIA_CREDENTIALS USER_CREDENTIALS \
  DARK_SKY_KEY GOOGLE_GEOCODING_KEY APP_CLASS_NAME S3_BUCKET STACK_NAME"

if [ ! -f env.sh ]
then
  echo "You need to create an env.sh file setting values for $required_params\n\nDON'T add env.sh to version control."
  exit 1
else
  . ./env.sh
fi
verifySet $required_params
mvn package && \
sam package \
    --template-file template.yaml \
    --s3-bucket ${S3_BUCKET:?}  \
    --output-template-file packaged.yaml &&
aws cloudformation deploy \
    --template-file packaged.yaml \
    --stack-name ${STACK_NAME:?} \
    --parameter-overrides \
        StrideCredentials=${STRIDE_CREDENTIALS:?} \
        MediaCredentials=${MEDIA_CREDENTIALS:?} \
        UserCredentials=${USER_CREDENTIALS:?} \
        DynamoDBEndpoint= \
        DarkSkyKey=${DARK_SKY_KEY:?} \
        GoogleGeocodingKey=${GOOGLE_GEOCODING_KEY:?} \
        AppClassName=${APP_CLASS_NAME:?} \
    --capabilities CAPABILITY_IAM
echo "\nApplication Descriptor URL: \c"

aws cloudformation describe-stacks --stack-name ${STACK_NAME} --query 'Stacks[0].Outputs[?OutputKey==`BotApplicationDescriptorUrl`].OutputValue' --output text
BUCKET=$(aws cloudformation describe-stacks --stack-name ${STACK_NAME} --query 'Stacks[0].Outputs[?OutputKey==`StaticContentBucketRef`].OutputValue' --output text)

aws s3 sync static s3://$BUCKET --acl public-read
