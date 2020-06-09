# Popular Movies
Popular movies is a simple app that searches movies using [The Movie Database API](https://www.themoviedb.org/documentation/api?language=en-US).

# Libraries
[Retrofit](https://square.github.io/retrofit/), ViewModel, LiveData, Recyclerview, CardView, [Picasso](https://square.github.io/picasso/), [CircleImageView](https://github.com/hdodenhof/CircleImageView).

# Description
App uses MVVM architecture and makes network calls using Retrofit. Launch screen contains a list of categories (keywords) and a SearchView in a custom ToolBar. After results are fetched, they are displayed in the RecyclerView. Clicking on the movie from the list opens up a second activity for displaying additional information. App has implemented pagination and additional results will be fetched as the user scrolls down.
