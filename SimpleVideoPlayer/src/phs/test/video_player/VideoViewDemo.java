/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package phs.test.video_player;

import viettel.sonph5.test.R;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoViewDemo extends Activity {

	private static final String URL_VIDEO = "rtsp://192.168.17.78:5544/stream";
	/**
	 * TODO: Set the path variable to a streaming video URL or a local media
	 * file path.
	 */
	private VideoView mVideoView;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.videoview);
		mVideoView = (VideoView) findViewById(R.id.surface_view);

		//        if (path == "") {
		//            // Tell the user to provide a media file URL/path.
		//            Toast.makeText(
		//                    VideoViewDemo.this,
		//                    "Please edit VideoViewDemo Activity, and set path"
		//                            + " variable to your media file URL/path",
		//                    Toast.LENGTH_LONG).show();
		//
		//        } else {
		//
		mVideoView.setVideoURI(Uri.parse(URL_VIDEO));
		//            mVideoView.setVideoPath(path);
		mVideoView.setMediaController(new MediaController(this));
		mVideoView.requestFocus();
		mVideoView.setOnPreparedListener(new OnPreparedListener() {
			
			public void onPrepared(MediaPlayer mp) {
				mVideoView.start();
				
			}
		});
	}
}
