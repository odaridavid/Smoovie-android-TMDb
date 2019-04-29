# Smoovie-android-TMDb
Android App Querying TMDb Using REST Libraries:Okhttp,Gson,Retrofit and Picasso for image loading

## PREREQUISITES

```
TMDb Api Key
```

An Api key is required to run GET Responses

Use in TMDB Class

###### Setting Up API Key

Find the `.gradle` file in the following directory

* Windows: C:\Users\<Your Username>\.gradle
* Mac: /Users/<Your Username>/.gradle
* Linux: /home/<Your Username>/.gradle

In the `gradle.properties` file add your Api Key(Create One if there is none)

```
API_KEY = "Your API KEY"
```

In the `app` module `build.gradle` setup the Api Key 


  debug { 
   
    ...
    buildConfigField ("String", "ApiKey", API_KEY)
    ...
    
  }
  
  release {
  
     ...
    buildConfigField ("String", "ApiKey", API_KEY)
    ...
    
  } 

#### Authors

- David Odari


### Screenshots


| [![Screen1](https://github.com/Davidodari/Smoovie-android-TMDb/blob/master/screenshots/Screenshot1.png)]()  | [![Screen2](https://github.com/Davidodari/Smoovie-android-TMDb/blob/master/screenshots/Screenshot_2.png)]() | [![Screen3](https://github.com/Davidodari/Smoovie-android-TMDb/blob/master/screenshots/Screenshot_5.png)]() |[![Screen4](https://github.com/Davidodari/Smoovie-android-TMDb/blob/master/screenshots/Screenshot_6.png)]() |[![Screen5](https://github.com/Davidodari/Smoovie-android-TMDb/blob/master/screenshots/Screenshot_7.png)]() |
|:---:|:---:|:---:|:---:|:---:|
| Detail Activity | Main Activity | Detail Activity | Favourite Activity |Favouite Activity |

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

