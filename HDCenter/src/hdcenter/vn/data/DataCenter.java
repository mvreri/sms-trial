package hdcenter.vn.data;

import hdcenter.vn.data.requests.ReqMovieDetail;
import hdcenter.vn.data.requests.ReqNewMovies;
import hdcenter.vn.data.requests.ReqRecommendMovies;
import hdcenter.vn.data.requests.ReqSearch;
import android.os.Handler;


public class DataCenter {

	public static void requestRecommendMovies(int page, IRequestListener listener, Handler handler) {
		RequestWorker.add(new ReqRecommendMovies(page),listener,handler);
	}

	public static void requestNewMovies(int page, IRequestListener listener, Handler handler) {
		RequestWorker.add(new ReqNewMovies(page),listener,handler);
	}

	public static void requestSearch(
			IRequestListener listener, 
			String keyword,
			int page, 
			Handler handler) {
		RequestWorker.add(new ReqSearch(keyword, page), listener, handler);
	}

	public static void requestMovieDetails(IRequestListener listener,String id, Handler handler) {
		RequestWorker.add(new ReqMovieDetail(id), listener, handler);
	}
	
}
