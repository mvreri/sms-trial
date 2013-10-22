package dtd.phs.lib.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import dtd.phs.lib.utils.Logger.CodeModes;

public class Helpers {

	public static View inflate(Context context, int layout, ViewGroup parent) {
		LayoutInflater inf = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inf.inflate(layout, parent);
	}

	public static void showOnlyView(FrameLayout mainFrames, int id) {
		for (int i = 0; i < mainFrames.getChildCount(); i++) {
			int disp = View.INVISIBLE;
			if (i == id)
				disp = View.VISIBLE;
			mainFrames.getChildAt(i).setVisibility(disp);
		}
	}

	public static float dp2px(Context context, int i) {
		Resources r = context.getResources();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i,
				r.getDisplayMetrics());
	}

	public static void hideSoftKeyboard(Activity act, View view) {
		InputMethodManager imm = (InputMethodManager) act
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
	}

	public static void hideSoftKeyboard(Activity activity) {
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	public static String MD5(String inputString) {
		if (inputString == null) {
			Logger.logInfo("Input string is NULL");
			return null;
		}
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			byte[] array = md.digest(inputString.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}

	public static void showToast(final Activity act, final int stringResId) {
		act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(act, stringResId, Toast.LENGTH_LONG).show();
			}
		});

	}

	public static void showToastContext(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void showToast(final Activity act, final String message) {
		act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(act, message, Toast.LENGTH_LONG).show();
			}
		});

	}

	public static void showToastContext(Context context, int messageId) {
		Toast.makeText(context, messageId, Toast.LENGTH_LONG).show();
	}

	public static void showToast(final Activity act, final String message,
			final int time) {
		act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(act, message, time).show();
			}
		});

	}

	public static void showToast(final Activity act, final int messageId,
			final int time) {
		act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(act, messageId, time).show();
			}
		});

	}

	public static void showToastContext(Context context, String message,
			int time) {
		Toast.makeText(context, message, time).show();
	}

	public static void launchHomeScreen(Activity activity) {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(startMain);
	}

	private static final int CONNECT_TIMEOUT = 3000;
	private static final int READ_TIME_OUT = 2000;

	static public Bitmap downloadBitmap(String urlString)
			throws MalformedURLException, IOException {
		InputStream is = null;
		try {
			// Logger.logInfo("URL: " + url +" -- is being loaded !");
			URL url = new URL(urlString);
			URLConnection con = url.openConnection();
			con.setConnectTimeout(CONNECT_TIMEOUT);
			con.setReadTimeout(READ_TIME_OUT);
			is = con.getInputStream();
			// InputStream is = (InputStream) new URL(url).getContent();
			try {
				Bitmap bm = BitmapFactory.decodeStream(is);
				return bm;
			} catch (OutOfMemoryError e) {
				Logger.logError(e.toString());
				return null;
			}
		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}
		}
	}

	static public String getLocalIP() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface
					.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface current = interfaces.nextElement();

				Enumeration<InetAddress> addresses = current.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress current_addr = addresses.nextElement();
					if (current_addr.isLoopbackAddress())
						continue;
					if (current_addr instanceof Inet4Address) {
						String addr = current_addr.toString();
						if (addr.startsWith("/"))
							return addr.substring(1);
						return addr;
					}
				}
			}
			return null;
		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static short CRC16_CCITT(byte[] bytes) {
		int crc = 0xFFFF; // initial value
		int polynomial = 0x1021; // 0001 0000 0010 0001 (0, 5, 12)

		// byte[] testBytes = "123456789".getBytes("ASCII");
		for (byte b : bytes) {
			for (int i = 0; i < 8; i++) {
				boolean bit = ((b >> (7 - i) & 1) == 1);
				boolean c15 = ((crc >> 15 & 1) == 1);
				crc <<= 1;
				if (c15 ^ bit)
					crc ^= polynomial;
			}
		}

		crc &= 0xffff;
		return (short) crc;
	}

	public static void assertCondition(boolean b) {
		if (Logger.CODE_MODE == CodeModes.DEBUG) {
			Assert.assertTrue(b);
		} else if (Logger.CODE_MODE == CodeModes.TEST) {
			if (!b)
				Logger.logError("Assert false");
		} else {
			// in production mode : do nothing
		}
	}

	public static String byte2KByte(long totalBytes) {
		long kB = totalBytes / 1024;
		String format = NumberFormat.getInstance().format(kB);
		return format;
	}

	/**
	 * Copies elements from {@code original} into a new array, from indexes
	 * start (inclusive) to end (exclusive). The original order of elements is
	 * preserved. If {@code end} is greater than {@code original.length}, the
	 * result is padded with the value {@code (byte) 0}.
	 * 
	 * @param original
	 *            the original array
	 * @param start
	 *            the start index, inclusive
	 * @param end
	 *            the end index, exclusive
	 * @return the new array
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code start < 0 || start > original.length}
	 * @throws IllegalArgumentException
	 *             if {@code start > end}
	 * @throws NullPointerException
	 *             if {@code original == null}
	 * @since 1.6
	 */
	public static byte[] copyOfRange(byte[] original, int start, int end) {
		if (start > end) {
			throw new IllegalArgumentException();
		}
		int originalLength = original.length;
		if (start < 0 || start > originalLength) {
			throw new ArrayIndexOutOfBoundsException();
		}
		int resultLength = end - start;
		int copyLength = Math.min(resultLength, originalLength - start);
		byte[] result = new byte[resultLength];
		System.arraycopy(original, start, result, 0, copyLength);
		return result;
	}

	public static byte[] mergeArrays(byte[] header, byte[] dataBytes) {
		byte[] replyBytes = new byte[header.length + dataBytes.length];
		int i = 0;
		for (i = 0; i < header.length; i++)
			replyBytes[i] = header[i];
		for (int j = 0; j < dataBytes.length; j++) {
			replyBytes[i++] = dataBytes[j];
		}
		return replyBytes;
	}

	public static byte[] getFromBuffer(ByteBuffer buffer, int startIndex,
			int count) {
		byte[] data = new byte[count];
		for (int i = 0; i < count; i++)
			data[i] = buffer.get(startIndex + i);
		return data;
	}

	static public ByteBuffer createBuffer(byte[] replyBytes, int totalLength) {
		ByteBuffer buffer = ByteBuffer.allocate(totalLength);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(replyBytes, 0, totalLength);
		buffer.position(0);
		return buffer;
	}

	public static void postText(final TextView tv, final String text) {
		tv.post(new Runnable() {
			@Override
			public void run() {
				tv.setText(text);
			}
		});
	}

	public static void showSoftKeyboard(Context context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);

	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	// static final int[] ratingResources = {
	// R.drawable.star0, R.drawable.star1,
	// R.drawable.star2, R.drawable.star3,
	// R.drawable.star4, R.drawable.star5,
	// R.drawable.star6, R.drawable.star7,
	// R.drawable.star8, R.drawable.star9,
	// R.drawable.star10
	// };
	//
	// static public int getRatingResource(int rating) {
	// //Logger.logInfo("Rating: " + rating);
	// Helpers.assertCondition(rating >= 0 && rating <= 10);
	// return ratingResources[rating];
	// }

	/**
	 * 
	 * @param rb
	 * @return [0..10]
	 */
	static public int getRating(RatingBar rb) {
		return (int) (rb.getRating() * 2);
	}

	/**
	 * 
	 * @param rb
	 * @param rating
	 *            [0..10]
	 */
	static public void setRating(RatingBar rb, int rating) {
		rb.setRating((1.0f * rating) / 2);
	}

	private static final String YOUTUBE_ID_PATTERN = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

	public static String parseIDFromYoutubeLink(String link) {
		// reference:
		// http://stackoverflow.com/questions/7730328/getting-the-youtube-id-from-a-link
		if (link == null) {
			return null;
		}
		Pattern compiledPattern = Pattern.compile(YOUTUBE_ID_PATTERN);
		Matcher matcher = compiledPattern.matcher(link);

		if (matcher.find())
			return matcher.group();
		return null;
	}

	static final String YOUTUBE_PREFIX = "http://img.youtube.com/vi/";

	public static String youtubeImage(String id) {
		if (id == null) {
			return null;
		}
		String result = YOUTUBE_PREFIX + id + "/0.jpg";
		return result;
	}

	public static String youtubeAvatar(String link) {
		return youtubeImage(parseIDFromYoutubeLink(link));
	}

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static boolean isValidEmail(String email) {
		// Reference:
		// http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean isValidDisplayName(String name) {
		if (name == null)
			return false;
		return (name.trim().length() > 0);
	}

	public static Bitmap getCircularBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				Math.min(bitmap.getWidth() / 2, bitmap.getHeight() / 2), paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static String convert2ReadalbeTime(int currentTimeMils) {
		int seconds = (currentTimeMils % 60000) / 1000;
		int minutes = currentTimeMils / 60000;
		String min = Integer.toString(minutes);
		// if (min.length() == 1) min = "0" + min;
		String sec = Integer.toString(seconds);
		if (sec.length() == 1)
			sec = "0" + sec;
		return min + ":" + sec;
	}
}
