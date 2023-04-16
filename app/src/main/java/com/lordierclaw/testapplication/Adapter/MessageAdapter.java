package com.lordierclaw.testapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lordierclaw.testapplication.LocationModel;
import com.lordierclaw.testapplication.MessageActivity;
import com.lordierclaw.testapplication.Model.MessageModel;
import com.lordierclaw.testapplication.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<MessageModel> messageList;
    public int lastPosition = -1;
    String selfuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public MessageAdapter(Context context , List<MessageModel> post){
        this.context = context;
        messageList = post;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) { //1
            View view = LayoutInflater.from(context).inflate(R.layout.chatitembot, parent, false);
            return new SendMoneyHolder(view);
        } else if (viewType == 2) { //2
            View view = LayoutInflater.from(context).inflate(R.layout.chatitembot, parent, false);
            return new SplitMoneyHolder(view);
        } else if (viewType == 3) { //3
            View view = LayoutInflater.from(context).inflate(R.layout.chatitemleft , parent , false);
            return new MessageHolder(view);
        } else { //4 - user
            View view = LayoutInflater.from(context).inflate(R.layout.chatitemright , parent , false);
            return new MessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel msg = messageList.get(position);
        if (msg == null) return;
        int type = msg.getMessageType();
        Log.d("TAG", "onBindViewHolder: "+type);
        setAnimation(holder.itemView, position);
        switch (type) {
            case 1:
                SendMoneyHolder sendMoneyHolder = (SendMoneyHolder) holder;
                sendMoneyHolder.message.setText(msg.getMessage());
                sendMoneyHolder.money.setText(String.valueOf(msg.getMoney()) + " đ");
                sendMoneyHolder.sendMoneyBtn.setOnClickListener(v -> {

                    //ấn thì xóa chat đi và hiện lên giao diện
                    DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chat/" + msg.getUidmessage());
                    chatRef.child("receiver").setValue("");

                });
                break;
            case 2:
                SplitMoneyHolder splitMoneyHolder = (SplitMoneyHolder) holder;
                splitMoneyHolder.message.setText(msg.getMessage());
                splitMoneyHolder.money.setText(String.valueOf(msg.getMoney())+ " đ");
                splitMoneyHolder.splitPrepaidBtn.setOnClickListener(view -> {
                    DatabaseReference locReference = FirebaseDatabase.getInstance().getReference("location/");
                    ArrayList<String> usernearbyList = new ArrayList<>();
                    MessageActivity messageActivity = (MessageActivity) context;
                    double latitude = messageActivity.latitude;
                    double longitude = messageActivity.longitude;
                    String useruid = FirebaseAuth.getInstance().getUid();
                    locReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot child : dataSnapshot.getChildren()) {
                                if (!child.getKey().toString().equals(selfuid)) {
                                    LocationModel locationModel = child.getValue(LocationModel.class);
                                    Log.d("Loaded ", "Location: " + locationModel.toString());
                                        //check if near with current self uid
                                        //if near, add to list
                                        //if not, do nothing

                                    if (meterDistanceBetweenPoints((float) latitude, (float) longitude, (float) locationModel.getLatitude(), (float) locationModel.getLongitude()) < 100) {
                                        usernearbyList.add(child.getKey().toString());
                                    }
                                }
                            }
                            for (String targetuid : usernearbyList) {
                                DatabaseReference debtReference = FirebaseDatabase.getInstance().getReference("chat/");
                                MessageModel messageModel = new MessageModel(System.currentTimeMillis(), "Bạn đang nợ tiền " + FirebaseAuth.getInstance().getCurrentUser().getEmail(), MessageModel.SEND_MONEY_BOT, targetuid, msg.getTransactionAmount()/(usernearbyList.size()+1));
                                messageModel.setMessageType(1);
                                debtReference.push().setValue(messageModel);
                            }
                            DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chat/" + msg.getUidmessage());
                            chatRef.child("receiver").setValue("");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                });
                splitMoneyHolder.splitPostpaidBtn.setOnClickListener(view -> {

                });
                break;
            case 3:
                MessageHolder messageHolder = (MessageHolder) holder;
                messageHolder.messagetext.setText(msg.getMessage());


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/" + msg.getSender() + "/email");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageHolder.author.setText(snapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                break;
            case 4:
                MessageHolder messageHolder1 = (MessageHolder) holder;
                messageHolder1.messagetext.setText(msg.getMessage());


                DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users/" + msg.getSender() + "/email");
                dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("SOS", messageHolder1.author + " " + snapshot.getValue().toString());
                        messageHolder1.author.setText(snapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                messageHolder1.messagetext.setText(msg.getMessage());
                break;
        }


    }

    private double meterDistanceBetweenPoints(float lat_a, float lng_a, float lat_b, float lng_b) {
        float pk = (float) (180.f/Math.PI);

        float a1 = lat_a / pk;
        float a2 = lng_a / pk;
        float b1 = lat_b / pk;
        float b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }



    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public int getItemViewType(int position) {
        MessageModel msg = messageList.get(position);
        if (msg == null) return -1;
        return msg.getMessageType();

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull final RecyclerView.ViewHolder viewHolder)
    {
        super.onViewDetachedFromWindow(viewHolder);
        viewHolder.itemView.clearAnimation();
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.pop_enter);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageHolder extends RecyclerView.ViewHolder {
        TextView author, messagetext;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            messagetext = itemView.findViewById(R.id.messagetext);
            author = itemView.findViewById(R.id.messageauthor);
        }
    }

    public static class SendMoneyHolder extends RecyclerView.ViewHolder {
        TextView author, message, money;
        MaterialButton sendMoneyBtn, splitPrepaidBtn, splitPostpaidBtn;
        public SendMoneyHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.bot_msg_name);
            message = itemView.findViewById(R.id.bot_msg_text);
            money = itemView.findViewById(R.id.bot_msg_money);
            sendMoneyBtn = itemView.findViewById(R.id.bot_send_money_btn);
            splitPrepaidBtn = itemView.findViewById(R.id.bot_split_prepaid_btn);
            splitPostpaidBtn = itemView.findViewById(R.id.bot_split_postpaid_btn);
            sendMoneyBtn.setVisibility(View.VISIBLE);
            splitPrepaidBtn.setVisibility(View.GONE);
            splitPostpaidBtn.setVisibility(View.GONE);
            author.setText("JadeG Chatbot");
        }
    }

    public static class SplitMoneyHolder extends RecyclerView.ViewHolder {
        TextView author, message, money;
        MaterialButton sendMoneyBtn, splitPrepaidBtn, splitPostpaidBtn;
        public SplitMoneyHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.bot_msg_name);
            message = itemView.findViewById(R.id.bot_msg_text);
            money = itemView.findViewById(R.id.bot_msg_money);
            sendMoneyBtn = itemView.findViewById(R.id.bot_send_money_btn);
            splitPrepaidBtn = itemView.findViewById(R.id.bot_split_prepaid_btn);
            splitPostpaidBtn = itemView.findViewById(R.id.bot_split_postpaid_btn);
            sendMoneyBtn.setVisibility(View.GONE);
            splitPrepaidBtn.setVisibility(View.VISIBLE);
            splitPostpaidBtn.setVisibility(View.VISIBLE);
            author.setText("JadeG Chatbot");
        }
    }
}