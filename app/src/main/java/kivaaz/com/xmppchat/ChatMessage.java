package kivaaz.com.xmppchat;

import java.util.Random;

/**
 * Created by Muguntan on 9/27/2017.
 */

public class ChatMessage {
    public String body,sender,receiver,senderName;
    public String Date, Time;
    public String msgid;
    public Boolean isMine;


    public ChatMessage(String sender, String receiver, String body, String msgid, Boolean isMine) {
        this.sender = sender;
        this.receiver = receiver;
        this.isMine = isMine;
        this.body = body;
        this.msgid = msgid;
        senderName = sender;
    }

    public void setMsgID(){
        msgid += "-"+ String.format("%02d",new Random().nextInt(100));

    }
}
