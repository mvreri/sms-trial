package dtd.phs.lib.ui.images_loader;

import hdcenter.vn.utils.Logger;

import java.util.Stack;

public class LoadingImages extends Stack<LoadingImageItem>{
	private static final long serialVersionUID = -7844315733138445126L;

	@Override
	public synchronized LoadingImageItem pop() {
		try {
			while ( this.empty() )
				this.wait();
			return super.pop();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public synchronized boolean add(LoadingImageItem object) {
		removeDuplicate(object);
		boolean res = super.add(object);
//		if ( res ) 
//			Logger.logInfo("LoadingImageItem is loaded SUCCESSFULLY !");
//		else Logger.logInfo("LoadingImageItem is loaded FAILED !");
		this.notifyAll();
		return res;

	}

	private synchronized void removeDuplicate(LoadingImageItem object) {
		for(int i = 0 ;;) {
			if ( i >= this.size() ) break;
			LoadingImageItem item = this.get(i);
			if ( item.iview == object.iview ) 
				this.remove(i);
			else i++;
		}
	}
}