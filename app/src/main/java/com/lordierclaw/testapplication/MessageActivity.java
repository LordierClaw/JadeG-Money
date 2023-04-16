package com.lordierclaw.testapplication;

import static android.view.View.OnClickListener;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.microsoft3dprovider.AXMicrosoft3DEmojiProvider;
import com.aghajari.emojiview.view.AXEmojiEditText;
import com.aghajari.emojiview.view.AXEmojiPopup;
import com.aghajari.emojiview.view.AXEmojiView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lordierclaw.testapplication.Adapter.MessageAdapter;
import com.lordierclaw.testapplication.Model.MessageModel;
import com.lordierclaw.testapplication.Utils.BotPayMoneyRequest;
import com.lordierclaw.testapplication.Utils.BotSplitMoneyRequest;
import com.lordierclaw.testapplication.Utils.sharedprefs.SharedPrefsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessageActivity extends AppCompatActivity implements LocationListener {

    RecyclerView messageRecyclerView;
    ImageView sendicon, emojiicon, menuBtn, backBtn;
    AXEmojiEditText chatMessage;
    String selfuid, targetuid;
    ArrayList<MessageModel> messageList;
    MessageAdapter adapter;

    boolean isGoingToEat = false;
    long mTransaction = 0;
    private LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    public double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Log.d("MessageActivity", "Init message activity");

        AXEmojiManager.install(this, new AXMicrosoft3DEmojiProvider(this));
        init();


        // Location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    protected void onStart()
    {
        super.onStart();
//        // Khởi động service
//        Intent serviceIntent = new Intent(this, MyForegroundService.class);
//        ContextCompat.startForegroundService(this, serviceIntent);

        // Kiểm tra quyền truy cập vị trí và kết nối với LocationService
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }


    private void checkBot() {
        // 2.
        isGoingToEat = SharedPrefsManager.getInstance().getPrediction();
        mTransaction = SharedPrefsManager.getInstance().getTransaction();
        Log.d("isGoingToEat", String.valueOf(isGoingToEat));
        Log.d("mTransaction", String.valueOf(mTransaction));
        if (isGoingToEat) {
            // TODO: call request to server, server send command back to all other clients to check location and send back
            // Gửi thông báo lên server, server gửi ngược lại cho những người khác, những người khác sẽ lấy vị trí và tìm nhóm người đi ăn
            // Gửi List nhóm người lại lên server như là 1 bản nháp


        }

        // 3.
        // TODO: trong app có chức năng chuyển tiền, khi isGoingToEat = true => chắc chắn là sẽ chia tiền
        // Lần tiếp theo mở app sẽ hiển thị msg bạn muốn chia tiền không? (message loại 2)
        if (isGoingToEat && mTransaction > 0) {
            DatabaseReference dataReference = FirebaseDatabase.getInstance().getReference("chat");
            MessageModel msg = new MessageModel(System.currentTimeMillis(), "Bạn có muốn chia tiền?", MessageModel.SPLIT_MONEY_BOT, selfuid, mTransaction);
            msg.setMessageType(2);
            msg.setTransactionAmount(mTransaction);
            dataReference.push().setValue(msg);
            SharedPrefsManager.getInstance().setPrediction(false);
            isGoingToEat = false;
        }
        mTransaction = 0;
        SharedPrefsManager.getInstance().setTransaction(0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initChatMessage();
        adapter.lastPosition = -1;
    }

    private void init() {
        messageRecyclerView = findViewById(R.id.message_recyclerview);
        chatMessage = findViewById(R.id.chatMessage);
        sendicon = findViewById(R.id.sendicon);
        emojiicon = findViewById(R.id.emojiicon);
        menuBtn = findViewById(R.id.menu_button);
        backBtn = findViewById(R.id.back_button);

        selfuid = FirebaseAuth.getInstance().getUid();
        AXEmojiView emojiView = new AXEmojiView(MessageActivity.this);
        emojiView.setEditText(chatMessage);
        AXEmojiPopup emojiPopup = new AXEmojiPopup(emojiView);


        //set email
        String useruid = FirebaseAuth.getInstance().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/" + useruid);
        databaseReference.child("email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        chatMessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiPopup.dismiss();
            }
        });

        sendicon.setOnClickListener(v -> {
            String message = chatMessage.getText().toString();
            if (!message.isEmpty()) {
                sendMessage(selfuid, targetuid, message);
                chatMessage.setText("");
            }
        });

        emojiicon.setOnClickListener(v -> emojiPopup.toggle());

        menuBtn.setOnClickListener(view -> {
            // TODO: transaction
            mTransaction = 100000;
            SharedPrefsManager.getInstance().setTransaction(mTransaction);
            checkBot();
            Toast.makeText(this, "Transaction " + String.valueOf(mTransaction), Toast.LENGTH_SHORT).show();
        });

        backBtn.setOnClickListener(view -> {
            SharedPrefsManager.getInstance().clear();
            Toast.makeText(this, "Clear SharedPrefs", Toast.LENGTH_SHORT).show();
        });

        initChatMessage();
    }




    @SuppressLint("StaticFieldLeak")
    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("chat/");
        MessageModel messageModel = new MessageModel(System.currentTimeMillis(), message, sender, receiver, 0L);
        databaseReference.push().setValue(messageModel);
        try {
            new Handler().post(() -> new BotSplitMoneyRequest() {

                //gọi lên server xem câu nhắn có phải chuẩn bị đi ăn không
                @Override
                protected void onPostExecute(String result) {
                    if (result.equals("true")) {
                        SharedPrefsManager.getInstance().setPrediction(true);
                        isGoingToEat = true;
                    } else {
                        isGoingToEat = false;
                        //suggest mấy từ khóa kiểu có lương rồi
                        new Handler().post(() -> new BotPayMoneyRequest() {
                            @Override
                            protected void onPostExecute(String result) {
                                if (result.equals("true")) {
                                    MessageModel msg = new MessageModel(System.currentTimeMillis(), message, MessageModel.SEND_MONEY_BOT, FirebaseAuth.getInstance().getUid(), 10000L);
                                    msg.setMessageType(1);
                                    databaseReference.push().setValue(msg);
                                }
                            }
                        }.execute(message));
                    }
                }
            }.execute(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initChatMessage() {

        messageList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MessageActivity.this, LinearLayoutManager.VERTICAL, false);
        adapter = new MessageAdapter(MessageActivity.this , messageList);
        messageRecyclerView.setLayoutManager(layoutManager);
        messageRecyclerView.setAdapter(adapter);

        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("chat" + "/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                checkBot();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    messageModel.setUidmessage(dataSnapshot.getKey().toString());
                    Log.d("Reading ", messageModel.getSender() + " " + messageModel.getReceiver() + " " + messageModel.getMessage());
                    if (Objects.equals(messageModel.getSender(), selfuid)) {
                        messageModel.setMessageType(4);

                    } else if (Objects.equals(messageModel.getSender(), MessageModel.SEND_MONEY_BOT)) {
                        messageModel.setMessageType(1);
                        if (!messageModel.getReceiver().equals(FirebaseAuth.getInstance().getUid())) {
                            continue;
                        }
                    } else if (Objects.equals(messageModel.getSender(), MessageModel.SPLIT_MONEY_BOT)) {
                        messageModel.setMessageType(2);
                        if (!messageModel.getReceiver().equals(FirebaseAuth.getInstance().getUid())) {
                            continue;
                        }


                    } else {
                        messageModel.setMessageType(3);
                    }
                    messageList.add(messageModel);
                }
                adapter.notifyDataSetChanged();
                messageRecyclerView.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("MessageActivity", "onCancelled: " + error.getMessage());
            }
        });

    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Cập nhật vị trí
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Log.d("Update ", "update location");

        // Sử dụng thông tin vị trí để hiển thị lên giao diện người dùng
        DatabaseReference locReference = FirebaseDatabase.getInstance().getReference("location/" + selfuid);
        locReference.setValue(new LocationModel(latitude, longitude));
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}