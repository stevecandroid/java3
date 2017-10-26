package com.xt.java3.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xt.java3.R;
import com.xt.java3.modules.event.Message;
import com.xt.java3.util.Utils;

import java.util.List;

/**
 * Created by steve on 17-10-24.
 */

public class RecycleChatAdapter extends RecyclerView.Adapter<RecycleChatAdapter.MyChatViewHolder> {

    private Context context;
    private List<Message> messages ;

    public RecycleChatAdapter(List<Message> messages) {
        this.messages = messages;
    }


    @Override
    public MyChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        for(Message msg : messages){
            Log.e("ChatActivity",msg.getDirection()+" direction ");
        }


        View view ;
        context = parent.getContext();
        if(viewType == 0 ){
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_receive,null,false);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_send,null,false);
        }

        MyChatViewHolder holder = new MyChatViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(MyChatViewHolder holder, int position) {
        holder.message.setText(Utils.parseMessage(messages.get(holder.getAdapterPosition()).getMessage()));
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getDirection();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MyChatViewHolder extends RecyclerView.ViewHolder{

        TextView message;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            this.message = itemView.findViewById(R.id.message);
        }
    }
}
