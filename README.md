# TreadIntel-Android-Intent-Sample

## Java
### treadreadersampleintentapi
This example illustrates the recommended method by Zebra for utilizing the Zebra Tread Intel Intent API to measure tire tread depths. It is designed using Java development environment. Use this as a guide to develop your application.

Tread Intel API is designed based on the Android intent architecture, reducing library dependencies and integration time for app developers.
Respective API reference documents can be fount at https://techdocs.zebra.com/

### Prerequisits
This application necessitates the attachment of the Zebra Tread Intel Snap-On to the mobile computer. Additionally, the mobile computer must have the Zebra Tread Intel Android Framework installed.

### API summery:
INITIATE: Initiate Tread Reader framework session.
GET_PARAMS: Get the Tread Reader parameter values.
SET_PARAMS: Set the Tread Reader parameter values.
START: Start measurement and send data back to the application via pending intent.
VERSION: Get the Tread Reader framework version data.
CURRENT_STATE: Query the Tread Reader framework, connection state and framework state
ON_STATE_CHANGE: Register to receive notifications when the framework connection state or framework state changes.
RELEASE: Release the Tread Reader framework session.

The Tread Intel and its framework are designed for the Zebra’s TC7x and TC5x series devices. These terminals provide the necessary hardware and software capabilities to support the advanced functionalities of the Tread Intel.

### Important Points
|-Suported minimum SDK version: 30
|-Make sure that the MANAGE_EXTERNAL_STORAGE permission is enabled to allow full access to external storage.
|-Add queries tag
    For support devices which use Android 13 and above  add the <queries> tag in the AndroidManifest.xml of the application to define package name for Tread Intel. This required to allow the Tread Intel API to communicate with the application.
 
    <queries>    
        <package android:name="com.zebra.intentapis" />
    </queries>