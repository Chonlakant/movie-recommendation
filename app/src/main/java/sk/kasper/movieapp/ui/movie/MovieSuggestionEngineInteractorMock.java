/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Valter Kasper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package sk.kasper.movieapp.ui.movie;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sk.kasper.movieapp.models.Movie;

/**
 * Provides movie data
 */
public class MovieSuggestionEngineInteractorMock implements IMovieSuggestionEngineInteractor {
    List<Movie> movies = new ArrayList<>();
    private int nextMoviePosition = 0;

    public MovieSuggestionEngineInteractorMock() {
        String[] names = {"Rush", "Bláznivá dovolená", "Cesta vzhůru", "Hitman: Agent 47", "RYTMUS sídliskový sen"};

        for (String name : names) {
            movies.add(new Movie(1L, name));
        }
    }

    @Override
    public Observable<Movie> getSuggestionStream() {
        nextMoviePosition++;
        return Observable.just(movies.get(nextMoviePosition % movies.size()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void movieLiked(Movie movie) {

    }

    @Override
    public void movieDisliked(Movie movie) {

    }
}
