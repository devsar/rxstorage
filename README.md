# RxStorage
[![Release](https://jitpack.io/v/devsar/rxstorage.svg)](https://jitpack.io/#devsar/rxstorage)

Android SharedPreferences wrapper that works with RxJava 2 through a simple annotation api

# Installation
Add jitpack as a repository in your top-level `build.gradle` file

```gradle
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

Add the RxStorage dependency and annotation processor to your module-level `build.gradle` file

```gradle
dependencies {
    ...
    compile 'com.github.devsar.rxstorage:rxstorage-lib:x.y.z'
    annotationProcessor 'com.github.devsar.rxstorage:rxstorage-processor:x.y.z'
    ...
}
```
