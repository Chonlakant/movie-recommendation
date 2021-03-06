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

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayDeque;
import java.util.Queue;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import sk.kasper.movieapp.R;
import sk.kasper.movieapp.Utils;
import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.models.MovieDislike;
import sk.kasper.movieapp.models.MovieLike;
import sk.kasper.movieapp.ui.BaseActivity;

public class MovieActivity extends BaseActivity implements IMovieView {
    private static final String TAG = "MovieActivity";
    @Bind(R.id.tvMovieName)
    TextView tvMovieName;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
	@Bind(R.id.ivCover)
	ImageView ivCover;
	@Bind(R.id.tvImdb)
	TextView tvImdb;
	@Bind(R.id.tvMetascore)
	TextView tvMetascore;
	@Bind(R.id.tvPLot)
	TextView tvPlot;
    @Bind(R.id.bLike)
    Button bLike;
    @Bind(R.id.bDislike)
    Button bDislike;

    private MoviePresenter presenter;

    /**
     * Movies prepared to be shown
     */
    private Queue<Movie> movieQueue = new ArrayDeque<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

		presenter = new MoviePresenter(this, new MovieSuggestionEngineInteractor(Utils.getTasteKidApi(), Utils.getOmdbApi()));
	}

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onResume();
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void addMovieCard(Movie movie) {
		movieQueue.add(movie);
	}

	@Override
	public void showNextMovie() {
		movieQueue.remove();
		final Movie movie = movieQueue.element();
		tvMovieName.setText(movie.name);
		tvPlot.setText(movie.plot);
		tvImdb.setText("IMDB " + movie.imdbScore);
		tvMetascore.setText("Metascore " + movie.metascore);
		Picasso.with(this)
				.load(movie.coverUrl)
				.resize(ivCover.getWidth(), ivCover.getHeight())
				.centerCrop()
				.into(ivCover);
	}

    @Override
    public Observable<MovieLike> getMovieLikeStream() {
        return Observable.create(new Observable.OnSubscribe<MovieLike>() {
            @Override
            public void call(Subscriber<? super MovieLike> subscriber) {
                bLike.setOnClickListener(v -> subscriber.onNext(new MovieLike(movieQueue.element())));
            }
        });
    }

    @Override
    public Observable<MovieDislike> getMovieDislikeStream() {
        return Observable.create(new Observable.OnSubscribe<MovieDislike>() {
            @Override
            public void call(Subscriber<? super MovieDislike> subscriber) {
                bDislike.setOnClickListener(v -> subscriber.onNext(new MovieDislike(movieQueue.element())));
            }
        });
    }
}
