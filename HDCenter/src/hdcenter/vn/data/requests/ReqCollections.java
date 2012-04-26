package hdcenter.vn.data.requests;

import hdcenter.vn.entities.CollectionItem;
import hdcenter.vn.entities.MovieCollectionsList;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReqCollections extends Request {

	private static final int FIRST_PAGE = 1;
	private static final String PAGE = "page";
	private static final String ID = "id";
	private static final String ALL = "all";
	private static final String API_COLLECTION = "collection";
	private static final String TOTAL = "total";
	private static final String RESULT = "result";

	@Override
	protected String provideAPIName() {
		return API_COLLECTION;
	}

	@Override
	protected void provideParameters(HashMap<String, String> parameters) {
		parameters.put(PAGE, String.valueOf(FIRST_PAGE));
		parameters.put(ID, ALL);
	}

//	static final String RES = "{\"total\":\"2\",\"result\":[{\"id\":\"15\",\"ename\":\"Horror movies based on true stories\",\"vname\":\"Những phim kinh dị dựa trên chuyện có thật\",\"movies\":\"16\"}{\"id\":\"14\",\"ename\":\"IMDB Top 250\",\"vname\":\"250 bộ phim hay nhất mọi thời đại\",\"movies\":\"127\"}{\"id\":\"13\",\"ename\":\"10 good movies for women\",\"vname\":\"10 bộ phim hay dành cho phái đẹp\",\"movies\":\"10\"}{\"id\":\"12\",\"ename\":\"The 84th Annual Academy Awards\",\"vname\":\"Giải Oscars lần thứ 84 (2012)\",\"movies\":\"37\"}{\"id\":\"11\",\"ename\":\"Charlie Chaplin collection\",\"vname\":\"Tuyển tập hề Sác lô\",\"movies\":\"7\"}{\"id\":\"10\",\"ename\":\"Tuyển tập điệp viên 007\",\"vname\":\"007 collection\",\"movies\":\"23\"}{\"id\":\"9\",\"ename\":\"Donnie Yen movies collection\",\"vname\":\"Tuyển tập phim Chung Tử Đơn\",\"movies\":\"24\"}{\"id\":\"8\",\"ename\":\"Back to the Future trilogy\",\"vname\":\"Tuyển tập Trở lại tương lai\",\"movies\":\"3\"}{\"id\":\"7\",\"ename\":\"Stieg Larssons Millennium trilogy\",\"vname\":\"Bộ tiểu thuyết Millenium của nhà văn Stieg Larsson\",\"movies\":\"3\"}{\"id\":\"6\",\"ename\":\"Chan Wook Parks vengeance trilogy\",\"vname\":\"Bộ ba phim Báo thù của đạo diễn Park Chan Wook\",\"movies\":\"3\"}]}";
	@Override
	protected MovieCollectionsList parseData(String resultString) throws JSONException {
		MovieCollectionsList list = new MovieCollectionsList();
//		String escapeString = StringHelpers.escapeString(resultString);
//		Logger.logInfo(escapeString);
		//TODO: debug
		JSONObject object = new JSONObject(resultString);
		int total = object.getInt(TOTAL);
		if ( total != 0 ) {
			JSONArray array = object.getJSONArray(RESULT);
			for(int i = 0 ; i < array.length() ; i++) {
				CollectionItem item = CollectionItem.create(array.getJSONObject(i));
				list.add(item);
			}			
		}
		return list;
	}

}
