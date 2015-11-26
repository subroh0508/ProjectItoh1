package com.example.root.projectitoh1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class YouTubeFragment extends Fragment {
    // API Key
    private static final String API_KEY = "AIzaSyC7fz1t355kk_ggPNbYs74ljmm7V1Ffa4c";

    private EditText etWord;
    private Button btSearch;
    private ListView lvResultList;

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
    private static YouTube youtube;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_youtube, container, false);

        etWord = (EditText)rootView.findViewById(R.id.search_word);
        btSearch = (Button)rootView.findViewById(R.id.search_youtube);
        lvResultList = (ListView)rootView.findViewById(R.id.result_layout);

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = etWord.getText().toString();
                if(!keyword.isEmpty()){
                    try{
                        youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                            public void initialize(HttpRequest request) throws IOException {}
                        }).setApplicationName("projectitoh1").build();

                        YouTube.Search.List search = youtube.search().list("id,snippet");
                        search.setKey(API_KEY);
                        search.setQ(keyword);
                        search.setType("video");
                        search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
                        search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
                        SearchListResponse searchResponse = search.execute();
                        Log.d("t", "search.execute");

                        List<SearchResult> searchResultList = searchResponse.getItems();

                        if (searchResultList != null) {
                            SearchResult(searchResultList.iterator());
                        }
                    } catch (GoogleJsonResponseException e) {
                        Log.d("JsonError", "There was a service error: " + e.getDetails().getCode() + " : "
                                + e.getDetails().getMessage());
                    } catch (IOException e) {
                        Log.d("IOError", "There was an IO error: " + e.getCause() + " : " + e.getMessage());
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });

        return rootView;
    }

    private void SearchResult(Iterator<SearchResult> iteratorSearchResults) {
        ArrayList<String> titleList = new ArrayList<String>();

        while(iteratorSearchResults.hasNext()) {
            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            if(rId.getKind().equals("youtube#video")) {
                titleList.add(singleVideo.getSnippet().getTitle());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_1, titleList);
        lvResultList.setAdapter(adapter);

    }
}
