# React native - native module tests

This repository contains three projects which demonstrate how to prepare, react native application with native component. The component uses OpenGL ES 2.0 to render a square with texture. The texture is generated from virtual bitmap that contain text that comes from component props.

The bitmap size is related with OpenGL square proportions and text font size and text length - so the final output always looks nice and text is centered on it.

The projects was developed on Windows 10 machine, and was designed to work on Android devices.
Each project is described in details below.

## Setup working environment

### Setup working environment on windows 10

1. Install Android Studio
2. Insatll Java JDK 8 (optional; You can also use Android studio JRE path in JAVA_HOME)
3. Install nvm for windows: [https://github.com/coreybutler/nvm-windows](https://github.com/coreybutler/nvm-windows)
4. Install node by nvm: `nvm install 12.14.0`
5. In Android Studio install SDK 29 and emulator (e.g. Pixel 2)

### Expose port on android emulator for react native development server (optional)

```text
telnet localhost 5554
auth <token from file: ~/.emulator_console_auth_token>
redir add tcp:<host-port>:<emu-port>
```

and / or

```text
adb -s <emulator-name> reverse tcp:<host-port> tcp:<emu-port>
```

### Set environment variables

```text
ANDROID_HOME - point to SDK path
JAVA_HOME - point to JDK or Android Studio JRE path
NVM_HOME - should be set by nvm
NVM_SYMLINK - should be set by nvm
```

### Add those to PATH

```text
Path to java bin
%ANDROID_HOME%\tools
%ANDROID_HOME%\tools\bin
%ANDROID_HOME%\platform-tools
%ANDROID_HOME%\emulator
%NVM_HOME%
%NVM_SYMLINK%
```

## Projects description

### openglTest

This is a normal android application that uses OpenGL ES 2.0

It was prepared aside to test how to achieve final component features:

- render OpenGL object
- use vertex / pixel shaders
- texturing
- creating textures base on bitmap with text
- object animation (rectangle bounces along 3 axis with different speed and angle range)

### react-native-opengl-native

This is android native ui component written in Java.\
The project is published on npm: [https://www.npmjs.com/package/react-native-opengl-native](https://www.npmjs.com/package/react-native-opengl-native)\
It expose `MyGLBox` component.

Basic usage:

```text
import MyGLBox from 'react-native-opengl-native';

...

<View style={{ flex: 1, width: '100%', height: '100%', overflow: 'hidden' }}>
  <MyGLBox
    style={{ flex: 1, width: '100%', height: '100%' }}
    text="Hello world!!"}
  />
</View>

...
```

### openglText

This is final project that shows how to use 'react-native-opengl-native'.\
User can input text and press button. The text will be set to MyGLBox props and component will recreate OpenGL texture with the text.

You can run the project by running command:

```text
react-native run-android --port <exposed-port>
```

Below You can find screenshots from working application.

## Release apk

You can find already build release apk in `release` folder in this repository.

## Install custom APK from command line

```text
adb install example.apk
```

## Useful links

### React native - native module / ui component

[https://facebook.github.io/react-native/docs/native-modules-setup](https://facebook.github.io/react-native/docs/native-modules-setup)\
[https://facebook.github.io/react-native/docs/native-modules-android](https://facebook.github.io/react-native/docs/native-modules-android)\
[https://facebook.github.io/react-native/docs/native-components-android](https://facebook.github.io/react-native/docs/native-components-android)

### OpenGL ES 2.0 on Android

[https://developer.android.com/guide/topics/graphics/opengl#java](https://developer.android.com/guide/topics/graphics/opengl#java)\
[https://github.com/JimSeker/opengl/tree/master/HelloOpenGLES20/app/src/main/java/com/example/android/opengl](https://github.com/JimSeker/opengl/tree/master/HelloOpenGLES20/app/src/main/java/com/example/android/opengl)

### OpenGL ES 2.0 Textures and Font rendering

[https://stackoverflow.com/questions/15750542/android-opengles-2-0-texturing](https://stackoverflow.com/questions/15750542/android-opengles-2-0-texturing)\
[https://arm-software.github.io/opengl-es-sdk-for-android/high_quality_text.html](https://arm-software.github.io/opengl-es-sdk-for-android/high_quality_text.html)

### OpenGL ES - OpenGL queueEvent for rendering update from outside thread

[https://stackoverflow.com/questions/25388929/android-opengl-queueevent-why](https://stackoverflow.com/questions/25388929/android-opengl-queueevent-why)

### React native - generate release APK for android

[https://stackoverflow.com/questions/35935060/how-can-i-generate-an-apk-that-can-run-without-server-with-react-native](https://stackoverflow.com/questions/35935060/how-can-i-generate-an-apk-that-can-run-without-server-with-react-native)

### React native - clear native cache

[https://stackoverflow.com/questions/46878638/how-to-clear-react-native-cache](https://stackoverflow.com/questions/46878638/how-to-clear-react-native-cache)

### Publish npm package

[https://zellwk.com/blog/publish-to-npm/](https://zellwk.com/blog/publish-to-npm/)

### NPM link

[https://medium.com/dailyjs/how-to-use-npm-link-7375b6219557](https://medium.com/dailyjs/how-to-use-npm-link-7375b6219557)

### React native - link

[https://stackoverflow.com/questions/49874385/what-is-react-native-link](https://stackoverflow.com/questions/49874385/what-is-react-native-link)

## Screenshots

![Hello img](/images/screen1.png "Hello World!!")

![Hello img](/images/screen2.png "Hello World!!")

![Hello img](/images/screen3.png "Hello World!!")
