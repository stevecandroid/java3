package com.xt.java3.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.xt.java3.App;
import com.xt.java3.R;
import com.xt.java3.modules.event.Message;
import com.xt.java3.modules.response.BaseResponse;
import com.xt.java3.util.Utils;
import com.xt.java3.util.dialog.DialogHelper;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by steve on 17-10-24.
 */

public class RecycleChatAdapter extends RecyclerView.Adapter<RecycleChatAdapter.MyChatViewHolder> {

    private final int id;
    private Context context;
    private List<Message> messages ;


    /**
     *
     * @param messages 聊天的信息
     * @param id 聊天对象的id
     */
    public RecycleChatAdapter(List<Message> messages, int id ) {
        this.messages = messages;
        this.id = id ;
    }


    @Override
    public MyChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        context = parent.getContext();
        if(viewType == 0 ){
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_receive,parent,false);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_send,parent,false);
        }

        final MyChatViewHolder holder = new MyChatViewHolder(view);

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogHelper.showEnsureDialog(context, "确认删除这条记录吗", new Runnable() {
                    @Override
                    public void run() {
                        App.client.deleteRecord(id,messages.get(holder.getAdapterPosition()).getTime()
                                ,messages.get(holder.getAdapterPosition()).getDirection())
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<BaseResponse>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(BaseResponse value) {
                                        if(value.getStatus() == 0) {
                                            ToastUtils.showShort("删除成功");
                                            messages.remove(holder.getAdapterPosition());
                                            notifyDataSetChanged();
                                        }

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        ToastUtils.showShort("删除失败");
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }

                });
                return false;
            }
        });



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
