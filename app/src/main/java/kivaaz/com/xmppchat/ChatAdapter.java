package kivaaz.com.xmppchat;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Muguntan on 9/27/2017.
 */

public class ChatAdapter extends BaseAdapter{

    private static LayoutInflater inflater = null;
    ArrayList<ChatMessage> chatMessageList;

    public ChatAdapter(Activity activity, ArrayList<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ChatMessage message = (ChatMessage) chatMessageList.get(i);
        View vi = view;

        if (view == null) {
            vi = inflater.inflate(R.layout.chat_bubble, null);
        }
        TextView sender = vi.findViewById(R.id.senderName);
        TextView msg = vi.findViewById(R.id.msg);
        msg.setText(message.body);
        sender.setText(message.sender);
        LinearLayout bubble = (LinearLayout) vi.findViewById(R.id.msgBubble);
        LinearLayout layout = (LinearLayout) vi.findViewById(R.id.msgLayout);

        if(message.isMine){
            bubble.setBackgroundResource(R.drawable.bubble_right);
            layout.setGravity(Gravity.RIGHT);
        }
        else{
            bubble.setBackgroundResource(R.drawable.bubble_left);
            layout.setGravity(Gravity.LEFT);
        }

        return vi;
    }

    public void add(ChatMessage msg){
        chatMessageList.add(msg);
    }
}
