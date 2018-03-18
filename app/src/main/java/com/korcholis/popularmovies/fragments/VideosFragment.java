package com.korcholis.popularmovies.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.korcholis.popularmovies.R;
import com.korcholis.popularmovies.adapters.VideosAdapter;
import com.korcholis.popularmovies.data.TMDbApi;
import com.korcholis.popularmovies.exceptions.ConnectionNotAvailableException;
import com.korcholis.popularmovies.models.Video;
import com.korcholis.popularmovies.models.VideoList;
import com.korcholis.popularmovies.utils.ConnectionChecker;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnVideosFragmentListener} interface
 * to handle interaction events.
 * Use the {@link VideosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideosFragment extends Fragment {
    private static final String ARG_MOVIE_ID = "param1";
    @BindView(R.id.list)
    RecyclerView videosList;
    @BindView(R.id.empty_layout)
    View emptyLayout;
    @BindView(R.id.loadingPb)
    ProgressBar loadingPb;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int movieId;
    private OnVideosFragmentListener mListener;
    private VideosAdapter adapter;

    public VideosFragment() {

    }

    public static VideosFragment newInstance(int movieId) {
        VideosFragment fragment = new VideosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieId = getArguments().getInt(ARG_MOVIE_ID);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onVideosInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVideosFragmentListener) {
            mListener = (OnVideosFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new VideosAdapter(getContext());
        videosList.setAdapter(adapter);
        showLoading();
        compositeDisposable.add(
                TMDbApi.instance(getContext()).videosForMovie(movieId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<VideoList, List<Video>>() {

                            @Override
                            public List<Video> apply(VideoList videoList) {
                                return videoList.getResults();
                            }
                        })
                        .subscribe(new Consumer<List<Video>>() {
                            @Override
                            public void accept(List<Video> videos) {
                                if (videos == null) {
                                    if (!ConnectionChecker.isNetworkAvailable(getContext())) {
                                    } else {
                                    }
                                } else {
                                    adapter.swapContent(videos);
                                    if (videos.isEmpty()) {
                                        showEmpty();
                                    } else {
                                        showList();
                                    }
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                if (throwable instanceof ConnectionNotAvailableException) {
                                } else {
                                }
                            }
                        }));
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnVideosFragmentListener {
        void onVideosInteraction(Uri uri);
    }

    private void showLoading() {
        loadingPb.setVisibility(View.VISIBLE);
        videosList.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
    }

    private void showList() {
        loadingPb.setVisibility(View.GONE);
        videosList.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
    }

    private void showEmpty() {
        loadingPb.setVisibility(View.GONE);
        videosList.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
    }
}
