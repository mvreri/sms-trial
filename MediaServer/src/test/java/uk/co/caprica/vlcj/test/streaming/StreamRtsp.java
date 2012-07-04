/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2009, 2010 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.streaming;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

/**
 * An example of how to stream a media file using RTSP.
 * <p>
 * The client specifies an MRL of <code>rtsp://@127.0.0.1:5555/demo</code>
 */
public class StreamRtsp {

  public static void main(String[] args) throws Exception {
    String media = "D:\\Movies\\720p.mp4";
	NativeLibrary.addSearchPath( RuntimeUtil.getLibVlcLibraryName(), "c:/Program Files/VideoLAN/VLC/");
	Native.loadLibrary( RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
//	LibXUtil.initialise();

    String options = formatRtspStream("127.0.0.1", 5544, "stream");

    System.out.println("Streaming '" + media + "' to '" + options + "'");
    
    MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(args);
    HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newMediaPlayer();
    mediaPlayer.playMedia(media,
      options,
      ":no-sout-rtp-sap", 
      ":no-sout-standard-sap", 
      ":ttl=16",
      ":sout-all", 
      ":sout-keep"
    );

    // Don't exit
    Thread.currentThread().join();
  }
  
//  private static String formatRtspStream(String serverAddress, int serverPort, String id) {
//    StringBuilder sb = new StringBuilder(60);
//    sb.append(":sout=#rtp{sdp=rtsp://@");
////    sb.append(serverAddress);
//    sb.append(':');
//    sb.append(serverPort);
//    sb.append('/');
//    sb.append(id);
//    sb.append("}");
//    return sb.toString();
//  }
//  
	private static String formatRtspStream(String serverAddress, int serverPort, String id) {
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#rtp{sdp=rtsp://"); 
//		sb.append(serverAddress);
		sb.append(':');
		sb.append(serverPort);
		sb.append('/');
		sb.append(id);
		sb.append("}");
//		sb.append(":sout-all :sout-keep");
		return sb.toString();
	}
}
