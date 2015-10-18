package com.raffaelcavaliere.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.raffaelcavaliere.popularmovies.local.FavoriteActivity;
import com.raffaelcavaliere.popularmovies.remote.FetchActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Button btn_browse_online = (Button)view.findViewById(R.id.btn_browse_online);
        Button btn_view_favorites = (Button)view.findViewById(R.id.btn_view_favorites);

        btn_browse_online.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FetchActivity.class);
                getActivity().startActivity(intent);
            }
        });

        btn_view_favorites.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FavoriteActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }
}
