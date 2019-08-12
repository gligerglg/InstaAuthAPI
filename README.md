# InstaAuthAPI  [![](https://jitpack.io/v/gligerglg/InstaAuthAPI.svg)](https://jitpack.io/#gligerglg/InstaAuthAPI)
## Instagram login and Session management library for android developers

![alt text](https://github.com/gligerglg/InstaAuthAPI/master/login.png)

## Setup
### 1.Project Gradle
Add the code segment given below to the build.gradle(Project) file.
```
allprojects {
    repositories {
      maven { url 'https://jitpack.io' }  
    }
}
```

### 2.App Gradle
Add the code segment given below to the build.gradle(App) file.
```
dependencies {
    implementation 'com.github.gligerglg:InstaAuthAPI:1.0.1'
}
```

### 3.Initialize InstaAuthAPI client
In your activity, create a new InstaAuthAPI client by providing the client_id (Mandatory) and the redirect_url (Optional)

```java
InstaAuthAPI authAPI = new InstaAuthAPI.Builder(this)
                .setClientId("ENTER_YOUR_CLIENT_ID_HERE")
                .setRedirectURL("ENTER_YOUR_REDIRECT_URL_HERE")
                .setRedirectOnSuccess(true)
                .build();
```
In the builder, you will see there is a method called setRedirectOnSuccess(true) by giving true for the value, once the user has authenticated successfully, the app will redirect to the given redirect URL automatically or when you set it as false and then the user has authenticated successfully, the dialog will dismiss automatically. In some cases we really do not want to redirect the user to another page. Now it's time to use the initiated client.

###  4.Usage
```java
authAPI.logIn(new InstagramSessionHandler() {
            @Override
            public void onProfileDataReceived(Profile profile) {
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                finish();
            }

            @Override
            public void onErrorOccurred(String error) {
                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
            }
        });
```
InstaAuthAPI client has a public method called login() By invoking this method you will able to have the profile data of the authenticated user using InstagramSessionHandler() interface and the session will be handled automatically. Once you are done the authentication part, now you will be able to handle the Instagram session from anytime anywhere. Here is a code sample which show you to fetch the user data from InstaAuthAPI session using interfaces.


```java
InstaAuthAPI.getDefaultSession(this,new InstagramSessionHandler() {
            @Override
            public void onProfileDataReceived(Profile profile) {
                Picasso.get().load(profile.getPrfilePicture()).into(profileImage);
                txtBio.setText(profile.getBio());
                txtFullname.setText(profile.getFullName());
                txtUsername.setText(profile.getUsername());
                txtWeb.setText(profile.getWebsite());
            }

            @Override
            public void onErrorOccurred(String error) {
                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
            }
        });
```

In this code segment we will not initiate an another InstaAuthAPI client but we need to provide the activity context as a parameter.

### 5.Check Session Availability
What if you need to check whether the Instagram session is still available in the application. Use the below code segment
```java
InstaAuthAPI.isSessionAvailable(this);
```

### 6.LogOut
To perform Instagram logOut action just use the code segment given below. The sessions will be destroyed automatically.
```java
InstaAuthAPI.logOut(this);
```

## Disable Client-Side (Implicit) Authentication
### (From Instagram Developer Documentation)

The Implicit OAuth Grant flow was created for java-script or mobile clients. Many developers use this flow because of its convenience. Unfortunately, malicious developers can also use this flow to trick people into authorizing your OAuth Client. They can collect access tokens and then make API calls on behalf of your app. When this occurs, your OAuth Client could be banned from the platform by our spam detection systems.

If your app is powered by a server infrastructure, you can disable the Client-Side (Implicit) OAuth flow by checking the Disable implicit OAuth setting in your OAuth Client configuration. If checked, Instagram will reject Client-Side (Implicit) authorization requests and only grant Server-Side (Explicit) authorization requests. This setting helps protect your app because the Server-Side (Explicit) OAuth flow requires the use of your Client Secret, which should be unknown to malicious developers.

Happy Coding!!! :)
