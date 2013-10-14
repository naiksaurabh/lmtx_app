package org.lmtx;

import java.util.List;

import org.lmtx.data.RssItem;
import org.lmtx.listeners.ListListener;
import org.lmtx.util.RssReader;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.itcuties.multicategoryrssreader.R;

public class RssChannelActivity extends Activity {
	
	// A reference to this activity
    private RssChannelActivity local;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_rss_channel);
		
		String rssUrl = "http://www.lmtx-beta.appspot.com/rss.xml";
		
		// Set reference to this activity
        local = this;
         
        GetRSSDataTask task = new GetRSSDataTask();
         
        // Start process RSS task
        task.execute(rssUrl);
		
	}

	private class GetRSSDataTask extends AsyncTask<String, Void, List<RssItem> > {
        @Override
        protected List<RssItem> doInBackground(String... urls) {
            try {
                // Create RSS reader
                RssReader rssReader = new RssReader(urls[0]);
             
                // Parse RSS, get items
                return rssReader.getItems();
             
            } catch (Exception e) {
                Log.e("RssChannelActivity", e.getMessage());
            }
             
            return null;
        }
         
        @Override
        protected void onPostExecute(List<RssItem> result) {
             
            // Get a ListView from the RSS Channel view
            ListView itcItems = (ListView) findViewById(R.id.rssChannelListView);
                         
            // Create a list adapter
            ArrayAdapter<RssItem> adapter = new ArrayAdapter<RssItem>(local,android.R.layout.simple_list_item_1, result);
            // Set list adapter for the ListView
            itcItems.setAdapter(adapter);
                         
            // Set list view item click listener
            itcItems.setOnItemClickListener(new ListListener(result, local));
        }
        
    }
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
	
	
}
