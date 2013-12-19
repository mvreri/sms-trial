package com.vsm.radio18;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.sax.StartElementListener;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vsm.radio18.data.ReqBuyItem;
import com.vsm.radio18.data.ReqCreateUser;
import com.vsm.radio18.data.db.DBCenter;
import com.vsm.radio18.data.db.QueryWorker;
import com.vsm.radio18.data.entities.ArticleItem;
import com.vsm.radio18.ui.DialogRetry;
import com.vsm.radio18.ui.DialogRetry.IDialogRetryListener;
import com.vsm.radio18.ui.DialogWarningSMS;
import com.vsm.radio18.ui.DialogWarningSMS.IDialogSMSListener;

import dtd.phs.lib.data_framework.IDataListener;
import dtd.phs.lib.data_framework.IRequest;
import dtd.phs.lib.data_framework.RequestWorker;
import dtd.phs.lib.ui.images_loader.ImageCache;
import dtd.phs.lib.ui.images_loader.ImageLoader;
import dtd.phs.lib.utils.Helpers;
import dtd.phs.lib.utils.Logger;
import dtd.phs.lib.utils.PreferenceHelpers;

public class ArticlesAdapter extends BaseAdapter {

	private static final String DIALOG_WARNING_SMS = "DIALOG_WARNING_SMS";
	private static final String DIALOG_RETRY = "DIALOG_RETRY";
	private ArrayList<ArticleItem> list;
	private ImageLoader imageLoader;
	private FragmentActivity act;
	private Handler handler = new Handler();
	private ArrayList<OnClickListener> onItemBuyClick;
	protected boolean[] isPaid;

	public ArticlesAdapter(FragmentActivity activity) {
		this.act = activity;
		list = new ArrayList<ArticleItem>();
		onItemBuyClick = new ArrayList<OnClickListener>();
	}

	public void onResume() {
		imageLoader = new ImageLoader(act, handler, null);
	}

	public void onPause() {
		imageLoader.stop();
		imageLoader = null;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public ArticleItem getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder = null;
		if (v == null) {
			v = Helpers.inflate(act, R.layout.article_item, null);
			holder = new ViewHolder();
			holder.ivCover = (ImageView) v.findViewById(R.id.ivCover);
			holder.tvName = (TextView) v.findViewById(R.id.tvName);
			holder.tvDesc = (TextView) v.findViewById(R.id.tvDesc);
			holder.btBuy = (Button) v.findViewById(R.id.btBuy);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		ArticleItem item = getItem(position);
		holder.tvName.setText(item.getName());
		holder.tvDesc.setText(item.getDesc());
		String coverURL = item.getCoverURL();
		Bitmap bm = ImageCache.getCacheImage(coverURL);
		if (bm != null) {
			holder.ivCover.setImageBitmap(bm);
		} else if (imageLoader != null) {
			holder.ivCover.setImageBitmap(null);
			imageLoader.loadImage(coverURL, holder.ivCover,
					RadioConfiguration.USING_ROUNDED_IMAGE);
		}
		if ( isPaid[position] ) {
			holder.btBuy.setVisibility(View.VISIBLE);
			holder.btBuy.setOnClickListener(onItemBuyClick.get(position));
		} else {
			holder.btBuy.setVisibility(View.GONE);
			holder.btBuy.setOnClickListener(null);
		}
		return v;
	}

	public class ViewHolder {
		ImageView ivCover;
		TextView tvName;
		TextView tvDesc;
		Button btBuy;
	}

	public void refreshData(ArrayList<ArticleItem> list) {
		this.list.clear();
		this.onItemBuyClick.clear();
		this.isPaid = new boolean[list.size()];
		Arrays.fill(isPaid, false);
		for (int i = 0; i < list.size(); i++) {
			this.list.add(list.get(i));
			final int position = i;
			this.onItemBuyClick.add(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ArticleItem item = getItem(position);
					buyItem(item);
				}
			});
		}
		notifyDataSetChanged();
		checkPaidStatus();	
	}

	protected void buyItem(final ArticleItem item) {

		String userId = PreferenceHelpers.getUserId(act);
		if (userId != null) {
			requestBuyItem(userId, item);
		} else
			createUser(item);
	}
	
	private void checkPaidStatus() {
		QueryWorker.add(new Runnable() {
			@Override
			public void run() {
				isPaid = DBCenter.checkPaidStatus( list, act.getApplicationContext());
				act.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						notifyDataSetChanged();
					}
				});
			}
		});
	}

	private void requestBuyItem(String userId, final ArticleItem item) {
		IRequest request = new ReqBuyItem(userId, RadioConfiguration.ITEM_PRICE);
		IDataListener listener = new IDataListener() {
			@Override
			public void onError(Exception e) {
				Logger.logError(e);
				showRetryDialog(item);
			}

			@Override
			public void onCompleted(Object data) {
				if (data == null) {
					onError(new Exception("Null data returned"));
				} else {
					int code = (Integer) data;
					switch (code) {
					case ReqBuyItem.SUCCESS:
						QueryWorker.add(new Runnable() {
							@Override
							public void run() {
								DBCenter.addItem(act, item);
								act.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										checkPaidStatus();
									}
								});
								
							}
						});
						break;
					case ReqBuyItem.USER_NOT_EXISTs:
						createUser(item);
						break;
					case ReqBuyItem.NOT_ENOUGH_MONEY:
						showWarningSMSDialog(item);
						break;
					case ReqBuyItem.UNKNOWN_ERROR:
						showRetryDialog(item);
						break;
					default:
						Helpers.assertCondition(false);
					}
				}
			}
		};
		RequestWorker.addRequest(request, listener, handler);
	}

	protected void showWarningSMSDialog(ArticleItem item) {

		IDialogSMSListener dlistener = new IDialogSMSListener() {
			@Override
			public void onClosed() {
				// No-op
			}

			@Override
			public void onAccepted() {
				sendSMS();
			}

			private void sendSMS() {
				Uri smsUri = Uri.parse("tel:" + RadioConfiguration.SMS_NUMBER);
				Intent i = new Intent(Intent.ACTION_VIEW, smsUri);
				CharSequence userId = PreferenceHelpers.getUserId(act);
				Helpers.assertCondition(userId != null);
				if (userId != null) {
					String content = RadioConfiguration.SMS_CONTENT.replace(
							RadioConfiguration.USER_CODE, userId);
					i.putExtra("sms_body", content);
					i.setType("vnd.android-dir/mms-sms");
					act.startActivity(i); // TODO: start activity for result !
				}
			}
		};
		DialogWarningSMS dialog = DialogWarningSMS.getInstance(dlistener);
		dialog.show(act.getSupportFragmentManager(), DIALOG_WARNING_SMS);
	}

	protected void showRetryDialog(final ArticleItem item) {
		IDialogRetryListener dlistener = new IDialogRetryListener() {

			@Override
			public void onRetryClick() {
				buyItem(item);
			}

			@Override
			public void onClosed() {
				// no-op
			}
		};
		DialogRetry dialog = DialogRetry.getInstance(dlistener);
		dialog.show(act.getSupportFragmentManager(), DIALOG_RETRY);
	}

	protected void createUser(final ArticleItem item) {
		IRequest request = new ReqCreateUser(Helpers.getMacAddress(act));
		IDataListener listener = new IDataListener() {

			@Override
			public void onError(Exception e) {
				Logger.logError(e);
				showRetryDialog(item);
			}

			@Override
			public void onCompleted(Object data) {
				if (data != null) {
					String userId = (String) data;
					PreferenceHelpers.setUserId(act, userId);
					buyItem(item);
				} else {
					onError(new Exception("Null data returned"));
				}
			}
		};
		RequestWorker.addRequest(request, listener, handler);
	}

}
