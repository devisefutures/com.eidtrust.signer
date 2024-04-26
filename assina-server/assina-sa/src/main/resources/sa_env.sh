#!/usr/bin/env bash
# Sets up the environment for the SA

# Port this SA run on - must be specified in the client app
export SERVER_PORT=8081
export ASSINA_RSSP_BASE_URL=http://assinarssp.westeurope.cloudapp.azure.com
#export ASSINA_RSSP_BASE_URL=http://13.93.7.40:80
#export ASSINA_RSSP_BASE_URL=http://localhost:8080
# Used by CORS must match the URL that the browser uses for the client
# Must include the port number if it is not 80 or 443, must NOT include it otherwise
export ASSINA_CLIENT_BASE_URL=http://assina.westeurope.cloudapp.azure.com
#export ASSINA_CLIENT_BASE_URL=http://20.101.144.136

export JAVA_HOME=/usr/bin/java

export FILE_UPLOADDIR=./files
# Must point to the redirect on the node client server and port
export LOGS=./logs
mkdir -p $LOGS

mkdir -p $FILE_UPLOADDIR
export RSSP_CSCBASEURL=${ASSINA_RSSP_BASE_URL}/csc/v1

export SPRING_SERVLET_MULTIPART_MAXFILESIZE=1MB
export SPRING_SERVLET_MULTIPART_MAXREQUESTSIZE=1MB
export SPRING_SERVLET_MULTIPART_FILESIZETHRESHOLD=20KB

export FILE_EXTENSIONS=pdf
