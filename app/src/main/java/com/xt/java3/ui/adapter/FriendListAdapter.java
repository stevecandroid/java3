package com.xt.java3.ui.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.Utils;
import com.xt.java3.R;
import com.xt.java3.User;
import com.xt.java3.ui.chat.ChatActivity;
import com.xt.java3.util.BitmapUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by steve on 17-10-24.
 */

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.MyViewHolder> {

    private List<User> users;

    private Context context;

    public FriendListAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_item,null,false);
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

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText( users.get(position).getNickname());
        holder.avater.setImageBitmap(BitmapUtils.base64ToBitmap(users.get(position).getAvatar()));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView avater ;
        TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            avater = itemView.findViewById(R.id.user_avater);
            name = itemView.findViewById(R.id.user_name);
        }
    }
}
