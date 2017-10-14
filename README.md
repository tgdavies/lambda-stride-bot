# README #

This README explains how to use this project to create an Atlassian Stride bot which runs on AWS Lambda.

It does not (yet) demonstrate much of the Stride API, just receiving bot mentions and
sending messages.

## Getting Started ##

### Get set up with AWS ###
* [Create an AWS account](https://aws.amazon.com/free).
* Install the [AWS Command Line Interface](https://aws.amazon.com/cli) either using an installer 
(for Windows) or with `pip install awscli` on OS X or Linux.
* Set up your AWS credentials with `aws configure`.
* Install the [AWS Serverless Application Model](https://github.com/awslabs/aws-sam-local) tools 
with `npm install -g aws-sam-local`.

### Create a Stride Application ###
* Sign in to [Create an app](https://developer.atlassian.com/apps/create).
* Create an app.
* Enable the Stride API (and optionally the User and Media APIs, although there is no support for them in this repository yet) -- you will put the ids and secrets
 in the `env.sh` file you create next.

### Create an env.sh file ###
* Copy `env.sh.skeleton` to `env.sh` and fill in the environment variables.
* The api ids and secrets come from the app creation process above.
* If you want to run the sample 'Forecast' bot you will need to get Google geocoding and Dark Sky 
API keys.
* The base URL can only be set once you have done a deployment.

### Create your own bot implementation ###
* Create a class which implements `org.kablambda.framework.App` see `org.kablambda.forecast.ForecastApp` for an example.
* Set `APP_CLASS_NAME` in `env.sh` to the name of your bot class.

### Deploy to AWS ###
* Run `./deploy.sh`.
* TODO: Find the URL your lambda deployed to
* Set the `BASE_URL` in `env.sh` to your lambda's Application Gateway URL.
* Deploy again.
* TODO: magically fix the things in AWS which didn't work

### Register your descriptor with Stride ###
* Go to the 'Install' tab of your app and put the value of `$BASE_URL/app-descriptor.json` into the 
'Descriptor URL' field.
* Click 'Refresh'.
* Now you can use the 'Installation URL' to install your bot into Stride, using the 'Apps' icon in 
the sidebar for the room you want to install into.

## Who do I talk to? ##
* You can email tgdavies@gmail.com if you need help following these instructions.