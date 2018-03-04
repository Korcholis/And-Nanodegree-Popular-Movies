package com.korcholis.popularmovies.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.korcholis.popularmovies.R;
import com.korcholis.popularmovies.models.Movie;
import com.korcholis.popularmovies.utils.Constants;
import com.korcholis.popularmovies.utils.DynamicSizeUtils;
import com.korcholis.popularmovies.utils.MoviesActivity;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OverviewFragment.OnOverviewFragmentListener} interface
 * to handle interaction events.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment {

    private Movie movie;
    private OnOverviewFragmentListener mListener;

    @BindView(R.id.posterIv)
    ImageView posterIv;
    @BindView(R.id.releaseTv)
    TextView releaseTv;
    @BindView(R.id.ratingTv)
    TextView ratingTv;
    @BindView(R.id.synopsisTv)
    TextView synopsisTv;

    public OverviewFragment() {

    }

    public static OverviewFragment newInstance(Movie movie) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.PARAM_MOVIE_OBJ, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable(Constants.PARAM_MOVIE_OBJ);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_overview, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onOverviewInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOverviewFragmentListener) {
            mListener = (OnOverviewFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((MoviesActivity)getActivity()).getSupportActionBar().setTitle(movie.getTitle());
        Picasso.with(getActivity())
                .load(movie.getPoster())
                .placeholder(R.drawable.movie_loading)
                .error(R.drawable.movie_error)
                .into(posterIv);

        int imageWidth = posterIv.getWidth();
        int proportionalHeight = DynamicSizeUtils.posterHeight(imageWidth);
        ViewGroup.LayoutParams params = posterIv.getLayoutParams();
        params.height = proportionalHeight;
        posterIv.setLayoutParams(params);

        synopsisTv.setText(movie.getSynopsis());
        ratingTv.setText(getString(R.string.movie_rating_out_of, movie.getVoteAverage().toString()));



        SimpleDateFormat parser = new SimpleDateFormat(getString(R.string.tmdb_date_format));
        Date date = null;
        try {
            date = parser.parse(movie.getReleaseDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.human_date_format), Locale.getDefault());
        String formattedDate = formatter.format(date);

        releaseTv.setText(formattedDate);

        getView().post(new Runnable() {
            @Override
            public void run() {
                int imageWidth = posterIv.getWidth();
                int proportionalHeight = DynamicSizeUtils.posterHeight(imageWidth);
                ViewGroup.LayoutParams params = posterIv.getLayoutParams();
                params.height = proportionalHeight;
                posterIv.setLayoutParams(params);
            }
        });
    }

    public interface OnOverviewFragmentListener {
        void onOverviewInteraction(Uri uri);
    }

}
