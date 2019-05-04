# Smoovie-android-TMDb

![Travis Buils](https://travis-ci.org/Davidodari/Smoovie-android-TMDb.svg?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a3842e79b83445e9a05f10cee37d7e4a)](https://app.codacy.com/app/Davidodari/Smoovie-android-TMDb?utm_source=github.com&utm_medium=referral&utm_content=Davidodari/Smoovie-android-TMDb&utm_campaign=Badge_Grade_Dashboard)
[![Maintainability](https://api.codeclimate.com/v1/badges/7776eb88681469a9e49d/maintainability)](https://codeclimate.com/github/Davidodari/Smoovie-android-TMDb/maintainability)

Smoovie is an Android app Querying TMDb API to provide information on popular, trending, upcoming movies
with an option of saving ones favourite movies in a wishlist.

#### Prerequisites
 
 1. **Obtain Key**
 
 Create an account with TMDB to obtain an API key.
 
 2. **Configure gradle.properties**
 
 Once you have your TMDB key,you will need to add it to a global ```gradle.properties``` file so as not to add it to
 version control and expose your key and configure the projects app module build.gradle to reflect the name you gave your
 API Key.
 To Access the global ```gradle.properties``` ,Locate the `.gradle` file in the following directory
 
  > Windows: C:\Users\<Your Username>\.gradle
  
  > Mac: /Users/<Your Username>/.gradle
  
  > Linux: /home/<Your Username>/.gradle 
 
 If it doesn't exist create one and add in your API key like so
 
 ```properties
 
 PROJECT_API_KEY = 'API_KEY_VALUE'
 
 ```
 
 3. **Configure build.gradle**
 
 Then In the `app` module build.gradle add the following function under defaultconfig/release/debug closures depending on your preference
 to have access to the API key.
 
 ```groovy
 
  buildConfigField("String", "API_KEY", "\"${project.hasProperty("SMOOVIE_API_KEY") ? SMOOVIE_API_KEY : System.getenv("SMOOVIE_API_KEY")}\"")
 
  ```

### Screenshots

| [![Screen1](https://github.com/Davidodari/Smoovie-android-TMDb/blob/master/screenshots/screenshot_1.png)]()  | [![Screen2](https://github.com/Davidodari/Smoovie-android-TMDb/blob/master/screenshots/screenshot_2.png)]() | [![Screen3](https://github.com/Davidodari/Smoovie-android-TMDb/blob/master/screenshots/screenshot_3.png)]() |[![Screen4](https://github.com/Davidodari/Smoovie-android-TMDb/blob/master/screenshots/screenshot_4.png)]() |[![Screen5](https://github.com/Davidodari/Smoovie-android-TMDb/blob/master/screenshots/screenshot_5.png)]() |
|:---:|:---:|:---:|:---:|:---:|
| |  |  |  | |

### Libraries Used

 - [Leak Canary](https://github.com/square/leakcanary) - Capture Memory Leaks
 - [Retrofit](https://square.github.io/retrofit/) - Http Client for Api Calls
 - [Gson](https://github.com/google/gson) - Serialization/Deserialization Library
 - [Okhttp](https://github.com/square/okhttp) - An HTTP+HTTP/2 client for Android 
 - [Picasso](https://square.github.io/picasso/) - Image Loading Library
 - [Butterknife](http://jakewharton.github.io/butterknife/) - View Binding Library
 - [Styleable Toast](https://github.com/Muddz/StyleableToast) - Custom Android Toast Library
 - [Architecture Components](https://developer.android.com/topic/libraries/architecture) - Room ORM
 - [Parceler](https://github.com/johncarl81/parceler)
 - [Palette]()

## License

```
MIT License

Copyright (c) 2018 David Odari

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```