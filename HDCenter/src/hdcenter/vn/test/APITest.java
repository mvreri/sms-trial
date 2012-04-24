package hdcenter.vn.test;

import hdcenter.vn.R;
import hdcenter.vn.data.DataCenter;
import hdcenter.vn.data.IRequestListener;
import hdcenter.vn.data.requests.ReqMovieDetail;
import hdcenter.vn.data.requests.Request;
import hdcenter.vn.entities.MovieDetailsItem;
import hdcenter.vn.entities.MovieItem;
import hdcenter.vn.entities.MoviesList;
import hdcenter.vn.ui.MovieAdapter;
import hdcenter.vn.utils.Logger;

import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class APITest 
	extends Activity
	implements IRequestListener
{

	private Button bt;
	private ListView lv;
	private MovieAdapter adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
//	    simpleTest();
	    dataTest();
	    
	}

	private void dataTest() {
		setContentView(R.layout.image_loader_test);
	    lv = (ListView) findViewById(R.id.lvMovies);
//	    DataCenter.requestRecommendMovies(0,this, new Handler());
	    DataCenter.requestNewMovies(0,this, new Handler());
//	    Request request = new ReqRecommendMovies(0);
//	    Object requestData;
//		try {
//			requestData = request.requestData();
//		    if ( requestData != null ) {
//		    	adapter = new MovieAdapter(getApplicationContext(), (MoviesList)requestData);
//		    	lv.setAdapter(adapter);
//		    } else {
//		    	Logger.logInfo("Null data returned !");
//		    }
//		} catch (IOException e) {
//			Logger.logError(e);
//		} catch (JSONException e) {
//			Logger.logError(e);
//		}
	}

//	private void simpleTest() {
//		setContentView(R.layout.test_api);
//	    bt = (Button) findViewById(R.id.button1);
////	    tv = (TextView) findViewById(R.id.textView1);
//	    bt.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Request r = new ReqMovieDetail("1002");
//				try {
//					Object result = r.requestData();
//					if ( result == null ) {
//						
//					} else if ( result instanceof MoviesList) {
//						MoviesList list = (MoviesList) result;
//						for(MovieItem item : list) {
//							Logger.logInfo(item.toString());
//						}
//					} else if (result instanceof MovieDetailsItem){
//						MovieDetailsItem item = (MovieDetailsItem) result;
//						Logger.logInfo(item.toString());
//					}
//				} catch (IOException e) {
//					Logger.logError(e);
//				} catch (JSONException e) {
//					Logger.logError(e);
//				}
//			}
//		});
//	}

	@Override
	public void onRequestError(Exception e) {
		Logger.logError(e);
	}

	@Override
	public void onRequestSuccess(Object data) {
    	adapter = new MovieAdapter(getApplicationContext(), (MoviesList)data, new Handler());
    	lv.setAdapter(adapter);
	}

}
