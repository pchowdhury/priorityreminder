package com.phoenix2k.priorityreminder;

import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.widget.DataBufferAdapter;

/**
 * Created by Pushpan on 03/02/17.
 */

public class LoginActivity extends DriveBaseActivity {


    private ListView mListView;
    private DataBufferAdapter<Metadata> mResultsAdapter;
    private String mNextPageToken;
    private boolean mHasMore;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_listfiles);

        mHasMore = true; // initial request assumes there are files results.

        mListView = (ListView) findViewById(R.id.listViewResults);
        mResultsAdapter = new ResultsAdapter(this);
        mListView.setAdapter(mResultsAdapter);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            /**
             * Handles onScroll to retrieve next pages of results
             * if there are more results items to display.
             */
            @Override
            public void onScroll(AbsListView view, int first, int visible, int total) {
                if (mNextPageToken != null && first + visible + 5 < total) {
                    retrieveNextPage();
                }
            }
        });
    }

    /**
     * Clears the result buffer to avoid memory leaks as soon
     * as the activity is no longer visible by the user.
     */
    @Override
    protected void onStop() {
        super.onStop();
        mResultsAdapter.clear();
    }

    /**
     * Handles the Drive service connection initialization
     * and inits the first listing request.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        retrieveNextPage();
    }

    /**
     * Retrieves results for the next page. For the first run,
     * it retrieves results for the first page.
     */
    private void retrieveNextPage() {
        // if there are no more results to retrieve,
        // return silently.
        if (!mHasMore) {
            return;
        }
        // retrieve the results for the next page.
        Query query = new Query.Builder()
                .setPageToken(mNextPageToken)
                .build();
        Drive.DriveApi.query(getGoogleApiClient(), query)
                .setResultCallback(metadataBufferCallback);
    }

    /**
     * Appends the retrieved results to the result buffer.
     */
    private final ResultCallback<DriveApi.MetadataBufferResult> metadataBufferCallback = new
            ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(DriveApi.MetadataBufferResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Problem while retrieving files");
                        return;
                    }
                    mResultsAdapter.append(result.getMetadataBuffer());
                    mNextPageToken = result.getMetadataBuffer().getNextPageToken();
                    mHasMore = mNextPageToken != null;
                }
            };

}
