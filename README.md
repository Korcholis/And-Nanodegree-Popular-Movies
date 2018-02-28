# Pop Movies

This project is part of the Android Developer Nanodegree.

It uses TheMovieDatabase information to show the most popular and high rated movies of the moment.

## Why _Pop_?

The name was Popular Movies (anybody checking the package name will notice!), but once I added started filtering by Most **popular movies**, it looked too odd. So, yeah, I changed it to Pop.

## Set up

In order to connect to TheMovieDatabase, you need to set up your own API key. Open your `gradle.properties` file and fill the `TMDB_API_KEY` between quotes. Then sync the Gradle files and you're good to go!

## Libraries

The project uses a variety of libraries:

- **Picasso**: To fetch the images.
- **Retrofit + OkHttp + Gson + RxJava2 + RxAndroid + RxRetrofit**: To connect to the TMDb API and get all the information in a reactive way.
- **ButterKnife**: To use the layout items in a quicker fashion.
- **LeakCanary**: To check for any critical memory leak.
- **Palette + PicassoPalette**: To show up colors based on each poster.

## Tutorials

At the same time, I've learned how to use a library or loosely based my code on others:

- **[RxAndroid 2 with Retrofit 2 and GSON](https://medium.com/@mtrax/rxandroid-2-with-retrofit-2-and-gson-3f08d4c2627d)**: As a tutorial to know how to use Retrofit with RxJava.
- **[StackOverflow: Handling Network Error in Rxjava 2 - Retrofit 2
](https://stackoverflow.com/questions/41379815/handling-network-error-in-rxjava-2-retrofit-2)**: To check for network errors when fetching the API.
- **[Seamless Network State Monitoring With Retrofit + OkHttp	](https://stablekernel.com/seamless-network-state-monitoring-with-retrofit-okhttp/)**: Same as the other one.
- **[Adjust RecyclerView item height (example)](https://viksaaskool.wordpress.com/2015/05/08/adjust-recyclerview-item-height-example/)**: To adapt the Poster heights after the layout is loaded (especially useful when the amount of columns varies and the images have to adapt their width).
- **[StackOverflow: Can Retrofit with OKHttp use cache data when offline
](https://stackoverflow.com/questions/23429046/can-retrofit-with-okhttp-use-cache-data-when-offline)**: To cache the results coming from TMDb and making it a little faster.
- **[StackOverflow: Adding ripple effect for View in onClick
Ask Question ](https://stackoverflow.com/questions/38628607/adding-ripple-effect-for-view-in-onclick)**: To add a ripple effect to the posters.


## Icons

The launcher icon and the No internet connection are custom made. The launcher one uses the BadaBoom BB free font, downloaded at [Dafont](https://www.dafont.com/es/badaboom-bb.font?l[]=10&l[]=1&l[]=6&text=Pop%21).

If you use them, or you want to fork the project to make your own app, please, refer to [https://github.com/Korcholis/And-Nanodegree-Popular-Movies](https://github.com/Korcholis/And-Nanodegree-Popular-Movies).
