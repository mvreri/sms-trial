package dtd.phs.lib.ui.images_loader;

import java.util.LinkedList;

public class LoadingImages extends LinkedList<LoadingImageItem>{
	private static final long serialVersionUID = -7844315733138445126L;

	@Override
	public synchronized LoadingImageItem poll() {
		try {
			while ( this.isEmpty() )
				this.wait();
			LoadingImageItem item = this.peek();
			if ( item.allowedLoad()) {
				return super.poll();
			} else {
				long started = System.currentTimeMillis();
				while ( System.currentTimeMillis() - started < LoadingImageItem.DELAY_TIME ) {
					this.wait(LoadingImageItem.DELAY_TIME);
				}
				return this.poll();
			}
		} catch (InterruptedException e) {
			//Logger.logError(e);
			return null;
		}
	}

	@Override
	public synchronized boolean add(LoadingImageItem object) {
		removeDuplicate(object);
		boolean res = super.add(object);
		this.notifyAll();
		return res;

	}

	private synchronized void removeDuplicate(LoadingImageItem object) {
		for(int i = 0 ;;) {
			if ( i >= this.size() ) break;
			LoadingImageItem item = this.get(i);
			if ( item.weakIview == object.weakIview ) 
				this.remove(i);
			else i++;
		}
	}

	/*	
	 public synchronized boolean containsView(ImageView iview) {
		for(int i = 0 ; i < this.size() ; i++) {
			if ( this.get(i).weakIview.get() == iview ) return true;
		}
		return false;
	}
	 */
}