package com.vsm.radio18.data.entities;

import org.json.JSONException;
import org.json.JSONObject;

import com.vsm.radio18.RadioConfiguration;

public class ArticleItem {

	private static final String BASE_IMAGE_URL = "http://api.radio18plus.radito.com/uploads/photos/tracks/";
	private static final String MP3_LINK_TAG = "link";
	private static final String NAME_TAG = "name";
	private static final String ID_TAG = "id";
	private long id;
	private long catId;
	private String name;
	private String desc;
	private String mp3Link;
	private String imageId;

	public long getId() {
		return id;
	}

	public long getCatId() {
		return catId;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public String getMp3Link() {
		return mp3Link;
	}
	
	@Override
	public String toString() {
		return "id = " + id + " ### " + "catid = " + catId + " \n "
				+ "name = " + name + "\n" 
				+ "desc = " + desc + "\n"
				+ "mp3Link = " + mp3Link + "\n" 
				+ "imageId = " + imageId ;
			
	}

	public ArticleItem(long id, long catId, String imageId, String name, String desc,String mp3Link) {
		this.id = id;
		this.catId = catId;
		this.imageId = imageId;
		this.name = name;
		this.desc = desc;
		this.mp3Link = mp3Link;
	}

	public static ArticleItem parse(JSONObject jitem) throws JSONException {
		long id = jitem.getLong(ID_TAG);
		String name = jitem.getString(NAME_TAG);
		String mp3Link = jitem.getString(MP3_LINK_TAG); //MP3
		String desc = jitem.getString("description"); //MP3
		String imageId = jitem.getString("image"); //MP3
		long catId = jitem.getLong("cat_id");
		
		return new ArticleItem(id,catId,imageId,name,desc,mp3Link);
	}

	public String getCoverURL() {
		if ( imageId.startsWith("http://") ) return imageId;
		return BASE_IMAGE_URL + imageId + "_" + RadioConfiguration.IMAGE_SIZE + "x" + RadioConfiguration.IMAGE_SIZE + ".jpg";
	}

}
