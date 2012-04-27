package hdcenter.vn.data.requests;

import java.util.HashMap;

public class ReqMoviesInCollection extends RequestMoviesList {
	
	private static final String API_NAME = "collection";
	private static final String PAGE = "page";
	private static final String ID = "id";

	private String id;

	public ReqMoviesInCollection(String id, int page) {
		super(page);
		this.id = id;
	}

	@Override
	protected String provideAPIName() {
		return API_NAME;
	}

	@Override
	protected void provideParameters(HashMap<String, String> parameters) {
		parameters.put(ID,this.id);
		parameters.put(PAGE, String.valueOf(this.getPage()));
	}
}
