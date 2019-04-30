# Smoovie-android-TMDb

[![Maintainability](https://api.codeclimate.com/v1/badges/7776eb88681469a9e49d/maintainability)](https://codeclimate.com/github/Davidodari/Smoovie-android-TMDb/maintainability)

Android App Querying TMDb Using REST Libraries:Okhttp,Gson,Retrofit and Picasso for image loading

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
 PROJECT_API_KEY=API_KEY_VALUE
 ```
 
 3. **Configure build.gradle**
 
 Then In the `app` module build.gradle add the following function under defaultconfig/release/debug closures depending on your preference
 to have access to the API key.
 
 ```groovy
  def API_KEY = hasProperty('PROJECT_API_KEY')? PROJECT_API_KEY : System.getenv("PROJECT_API_KEY")
   buildConfigField("String", "API_KEY", "\"${project.hasProperty("SMOOVIE_API_KEY") ? SMOOVIE_API_KEY : System.getenv("SMOOVIE_API_KEY")}\"")
  ```

### Screenshots


| [![Screen1](https://github.com/Davidodari/Smoovie-android-TMDb/blob/master/screenshots/Screenshot1.png)]()  | [![Screen2](https://github.com/Davidodari/Smoovie-android-TMDb/blob/master/screenshots/Screenshot_2.png)]() | [![Screen3](https://github.com/Davidodari/Smoovie-android-TMDb/blob/master/screenshots/Screenshot_5.png)]() |[![Screen4](https://github.com/Davidodari/Smoovie-android-TMDb/blob/master/screenshots/Screenshot_6.png)]() |[![Screen5](https://github.com/Davidodari/Smoovie-android-TMDb/blob/master/screenshots/Screenshot_7.png)]() |
|:---:|:---:|:---:|:---:|:---:|
| Detail Activity | Main Activity | Detail Activity | Favourite Activity |Favouite Activity |

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