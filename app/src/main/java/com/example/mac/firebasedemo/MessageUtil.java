package com.example.mac.firebasedemo;


import android.app.Activity;
import android.nfc.Tag;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessageUtil {
    private static final String LOG_TAG = MessageUtil.class.getSimpleName();
    public static final String MESSAGES_CHILD = "messages";
    private static DatabaseReference sFirebaseDatabaseReference =
            FirebaseDatabase.getInstance().getReference();
    private static MessageLoadListener sAdapterListener;
    private static FirebaseAuth sFirebaseAuth;
    public interface MessageLoadListener {  };

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView messengerTextView;
        public MessageViewHolder(View v) {
            super(v);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            messengerTextView = itemView.findViewById(R.id.messengerTextView);
        }
    }

    public static FirebaseRecyclerAdapter getFirebaseAdapter(final Activity activity,
                                                             MessageLoadListener listener,
                                                             final LinearLayoutManager linearManager,
                                                             final RecyclerView recyclerView) {
        sAdapterListener = listener;
        final FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<ChatMessage,
                MessageViewHolder>(
                ChatMessage.class,
                R.layout.message,
                MessageViewHolder.class,
                sFirebaseDatabaseReference.child(MESSAGES_CHILD)) {
            @Override
            protected void populateViewHolder(final MessageViewHolder viewHolder,
                                              ChatMessage chatMessage, int position) {
                viewHolder.messageTextView.setText(chatMessage.getText());
                viewHolder.messengerTextView.setText(chatMessage.getName());
            }
        };

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                //super.onItemRangeInserted(positionStart, itemCount);
                int messageCount = adapter.getItemCount();
                int lastVisiblePosition = linearManager.findLastCompletelyVisibleItemPosition();
                recyclerView.scrollToPosition(positionStart);
            }
        });
        return adapter;
    }
}