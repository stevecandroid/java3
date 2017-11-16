package com.xt.java3.ui.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xt.java3.Constant;
import com.xt.java3.R;
import com.xt.java3.User;
import com.xt.java3.ui.chat.ChatActivity;
import com.xt.java3.ui.main.frag.contacts.ContactsFrag;


import com.xt.java3.util.dialog.DialogHelper;
import com.xt.java3.util.pic.bitmap.BitmapUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by steve on 17-10-24.
 */

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.MyViewHolder> {

    private List<User> users;
    private Context context;
    private ContactsFrag frag;


    public FriendListAdapter(ContactsFrag frag , List<User> users) {
        this.users = users;
        this.frag = frag;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_item,parent,false);
        final MyViewHolder holder = new MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //动画所需的图片通过evenbus 传给下一个活动
                EventBus.getDefault().postSticky(users.get(holder.getAdapterPosition()));
                Pair a = Pair.create(holder.name,"text");
                Pair b = Pair.create(holder.avater,"avatar");
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) context,a,b).toBundle();
                context.startActivity(new Intent(context, ChatActivity.class)
                        , bundle );
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                DialogHelper.showEnsureDialog(context, "确认删除", new Runnable() {
                    @Override
                    public void run() {
                        User user = users.get(holder.getAdapterPosition());
                        frag.getPresenter().deleteFriend(user.getId(),holder.getAdapterPosition());
                    }
                });


                return true;
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText( users.get(holder.getAdapterPosition()).getNickname());
        Log.e("FriendListAdapter",Constant.IP+"avatar?id="+users.get(holder.getAdapterPosition()).getId());
        Glide.with(context)
                .load(Constant.IP+"avatar?id="+users.get(holder.getAdapterPosition()).getId())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.avater);
        switch (users.get(holder.getAdapterPosition()).getStatus()){
            case 0 : holder.status.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.offline));break;
            case 1 : holder.status.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.online));break;
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView avater ;
        TextView name;
        ImageView status;

        public MyViewHolder(View itemView) {
            super(itemView);
            avater = itemView.findViewById(R.id.user_avater);
            name = itemView.findViewById(R.id.user_name);
            status = itemView.findViewById(R.id.status);
        }
    }
}
