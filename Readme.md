## Developer Guide ##

This guide explains how to prepare your computer to develop BlueVia applications for Android devices.

## Getting started ##

This guide explains how to prepare your environment to develop BlueVia applications for Android devices. First check out the system requirements that your computer must meet, and then follow the installation steps. Once you have finished you will be able to develop your first Android application using the functionality provided by Bluevia APIs.

## System requirements ##

The Bluevia library for Android is prepared and has been tested to develop applications under Android SDK 1.5 to 2.3 versions. Android SDK is the only system requirement for the Bluevia library. The following system requirements are the ones your computer needs to meet to be able to work with the Android SDK:

### Supported Operating Systems ###

- Windows XP (32-bit) or Vista (32- or 64-bit)
- Mac OS X 10.5.8 or later (x86 only)
- Linux (tested on Linux Ubuntu Hardy Heron)

### Eclipse IDE ###

- Eclipse 3.4 or greater
- Eclipse JDT plugin (included in most Eclipse IDE packages)
- JDK 5 or JDK 6 (JRE alone is not sufficient)
- Android Development Tools plugin (optional)

For complete information visit the [system requirements](http://developer.android.com/sdk/requirements.html) described in Android Developers.

## Step 1: Preparing the Android environment ##

The first step to start developing applications is setting up your Android environment. You have to download the [Android SDK](http://developer.android.com/sdk/index.html) and the [ADT plugin](http://developer.android.com/sdk/eclipse-adt.html), in case you choose to use Eclipse as your IDE. After installing the starter package you may use the [SDK tools](http://developer.android.com/sdk/adding-components.html) to add the Android platforms you consider. If you have already prepared your computer to develop Android applications you can skip to step 2; otherwise follow the next instructions:

Prepare your development computer and ensure it meets the system requirements.
Install the SDK starter package from the table above. (If you're on Windows, download the installer for help with the initial setup).
Install the ADT Plugin for Eclipse (if you'll be developing in Eclipse).
Add Android platforms and other components to your SDK.
Visit Android Developers for complete instructions: [downloading](http://developer.android.com/sdk/index.html) and [installing Android SDK](http://developer.android.com/sdk/installing.html).

## Step 2: Downloading Bluevia library for Android ##

There are two ways to work with the Bluevia APIs: getting the library itself (JAR file or source code), using it with the standard SDK; or getting an add-on for the SDK in case you are developing applications for 'Bluevia-enabled' devices.

### Downloading the Bluevia library ###

This is the way to get the Bluevia library to be used in any Android device. You only have to download the library and add it to your project so your application will include it, built with the standard Android SDK. Follow the next steps to set up your Android project:

1. Download Bluevia Library and save it in your hard disk: Bluevia SDK for Android
2. Create your Android Project in Eclipse: select File > New > Android Project.
3. Add the Bluevia Library to your project:
    - As JAR library:
        - Select Project > Properties.
        - In Java Build Path section, click on Libraries tab.
        - Finally, click on Add External JARs and select the path where you put the Bluevia Library JAR.
    - As Android library project:
        - Import the Android library project in Eclipse: 
          File > Import... > Existing projects into workspace... and search the BlueviaSDK project where you downloaded it.
        - Select your application project and click on Project > Properties.
         - In Android section, under Library options, click on Add button.
         - Select BlueviaSDK project and accept.

## Programming guidelines ##

This section is a basic introduction to the Bluevia framework. This guide explains the library behavior and architecture, its working modes and the security model, based in OAuth, and how to start developing and testing Android applications using the Bluevia APIs. In the API guides section several complete code examples for each API will be provided.

In order to complete the documentation of the Bluevia library, you should check the Reference section for API specifications.

### Bluevia library framework ###

The Bluevia library for Android architecture is mainly composed of three modules:

- An abstract data client, or connector, that performs the low level operations required to retrieve data from the remote servers. This layer is responsible for generating and propagating exceptions or error codes to the upper levels, were they needed.
- A client interface layer. This layer provides the interfaces, abstract classes and generic classes needed to implement specific clients for the different APIs. Every API in Bluevia inherits from the same abstract class, implementing the particular logic for each of the APIs in the final, non-abstract class that is also the interface to the user of the API.
- A parsing and serializing layer. This layer is responsible for parsing any data received from the remote end and populate the data model objects in the library with any data received, as well as for generating output streams that can be sent to the remote end from data contained in the library data model objects whenever it is necessary to send information to the server.

### Clients basics ###

The main component of the Bluevia library is the Client, which represents the client side in a classic client-server schema. The library specify a REST Client for each one of the supported APIs (i.e. SmsClient, DirectoryClient), and defines the operations provided by their respective Bluevia APIs, using a Java based data model according to the XML data type definitions of Bluevia.

According to this, the Bluevia library presents an easy programming model, concealing the communication mechanism from the developer. The way to work is:

- First, instantiate the corresponding client. You must provide the communication mechanism (as will be seen below) and the authorization information.
- Now use the operations defined by the client.
- Finally, call the close method in order to free the resources used by the client.

### Working modes ###

The Bluevia library provides two modes for each REST client: HTTP and TEST. The first one is the actual communication mechanism with the BlueVia, which provides the functionality described by the APIs. The TEST mode is a test mechanism to allow the developer to validate the correction of the requests and parse a static sample response. In addition, wrong responses and exceptions are provided to test error cases.

These are the available working modes, defined in the AbstractRestClient class. The working mode must be selected in the instantiation of the client:

- AbstractRestClient.Mode.HTTP 

The client works against a Live Bluevia server.

- AbstractRestClient.Mode.HTTP_SANDBOX 

The client works against a Sandbox Bluevia server.

- AbstractRestClient.Mode.TEST_OK 

The client works against a mock Bluevia server which always serves properly any request.

- AbstractRestClient.Mode.TEST_UNAUTHORIZED 

The client works against a mock Bluevia server which always responds with an BlueviaClientException informing the user is not authorized to perform that request.

- AbstractRestClient.Mode.TEST_ERROR_IOEXCEPTION 

The client works against a mock Bluevia server which always responds with an BlueviaClientException informing the user is unable to connect the server due to an in/out exception.

- AbstractRestClient.Mode.TEST_ERROR_HTTPEXCEPTION 

The client works against a mock Bluevia server which always responds with a BlueviaClientException informing the user is unable to connect the server due a http exception.

### API Authentication ###

BlueVia requires both the user and your application to be identified and authenticated to allow applications use its APIs. BlueVia supports OAuth Protocol, a token-passing mechanims where users never have to reveal their passwords or credentials to the application. Bluevia library implements an OAuth client to supply developers an easy access to authentication procedure: Oauth reference.

For more information visit the API authentication section of BlueVia reference.

### API guides ###

The following guides explain the behavior of each Bluevia API, including code samples to start developing a simple application in an easy way:

- SMS API
- MMS API
- Directory API
- Advertising API
- Location API
- Payment API

These can be found as part of the library documentation!
