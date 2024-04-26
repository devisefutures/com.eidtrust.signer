# com.eidtrust.signer

com.eidtrust.signer was proposed and managed by Devise Futures, and developped by @pg42819-chris and @FranciscoRosa11 (laboratory project for Master in Informatics Engineering).

This software project is the public domain - it is a free and unencumbered software released into the public domain - .

---------------

com.eidtrust.signer is a remote signing service provider, and client, adhering to the specification defined by the Cloud Signing Consortium (CSC) ​

----------------

### Requirements

- Node version: 14.16.0
- Java: 11
- Maven

----------------

### Running the whole application

In the root directory do the following:

```bash
./runRSSP.sh
./runSA.sh
./runFEND.sh
```

It will install every dependency needed to run the whole app and start both the Frontend and Backend applications.

----------------

### Features

- Creating new User
- Create new credentials for User
- Edit User profile
- Sign document

----------------

### Testing

In order to test the features, please use the following username/password combination and PIN (required to sign documents):

- username: carlos
- password: carlos
- pin: 1234

If, however, you wish to test using a Google account, please signup using Google and then set your PIN in the profile page.

You need to have at least 1 credential in order to request the signing of a document.

If the signing is successful, you will be redirected to a download page where you can download your signed pdf file.
