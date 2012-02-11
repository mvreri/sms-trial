package dtd.phs.sil.entities;

public interface IMessagesList {

	int size();
	int removeMessageWithId(long id);
	MessageItem get(int position);

}
