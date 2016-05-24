package com.example.nsity.schooldiary.navigation.messages;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.database.tables.MessageDBInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by nsity on 27.04.16.
 */
public class ChatRoomThreadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_PROG = 0;


    public static final int Header = 3;
    public static final int Normal = 4;
    public static final int Footer = 5;

    private Context context;
    private ArrayList<Message> messageArrayList;

    public class TextViewHolder extends RecyclerView.ViewHolder {
        TextView message, timestamp;
        RelativeLayout messageLayout;

        public TextViewHolder(View view) {
            super(view);
            message = (TextView) itemView.findViewById(R.id.message);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            messageLayout = (RelativeLayout) itemView.findViewById(R.id.message_layout);

        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public MaterialProgressBar progressBar;
        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (MaterialProgressBar)v.findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    public ChatRoomThreadAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if(viewType == MessageDBInterface.PUPIL_TYPE) {
            // self message
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_self, parent, false);
            return new TextViewHolder(itemView);
        }

        if(viewType == MessageDBInterface.TEACHER_TYPE) {
            // others message
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_other, parent, false);
            return new TextViewHolder(itemView);
        }

        if(viewType == VIEW_PROG) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            return new ProgressViewHolder(itemView);
        }

      /*  // view type is to identify where to render the chat message
        // left or right
        if (viewType == MessageDBInterface.PUPIL_TYPE) {
            // self message
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_self, parent, false);
        } else {
            // others message
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_other, parent, false);
        }

        return new TextViewHolder(itemView);*/

        return null;
    }


    @Override
    public int getItemViewType(int position) {
        if (messageArrayList.get(position) != null) {
            Message message = messageArrayList.get(position);
            if (message.getUserId() == MessageDBInterface.PUPIL_TYPE) {
                return MessageDBInterface.PUPIL_TYPE;
            }
            return MessageDBInterface.TEACHER_TYPE;
        } else {
            return VIEW_PROG;
        }
    }

    private void setMargins(RecyclerView.ViewHolder holder, int left, int top, int right, int bottom) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(left, top, right, bottom);
        ((TextViewHolder) holder).messageLayout.setLayoutParams(params);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TextViewHolder) {

            if(messageArrayList.size() == 1) {
                setMargins(holder, 0, 24, 0, 24);
            } else  {
                if(position == 0) {
                    setMargins(holder, 0, 24, 0, 0);
                } else {
                    if(position == messageArrayList.size() - 1) {
                        setMargins(holder, 0, 0, 0, 24);
                    } else {
                        setMargins(holder, 0, 0, 0, 0);
                    }
                }
            }

            Message message = messageArrayList.get(position);
            ((TextViewHolder) holder).message.setText(message.getMessage());

            String timestamp = convertDate(message.getCreatedAt());
            ((TextViewHolder) holder).timestamp.setText(timestamp);
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }


    SimpleDateFormat date_format = new SimpleDateFormat("yyyyMMdd", new Locale("ru"));

    SimpleDateFormat hh_mm = new SimpleDateFormat("HH:mm", new Locale("ru"));

    private String convertDate(String oldDate) {
        try {
            Date date = new SimpleDateFormat(CommonFunctions.FORMAT_YYYY_MM_DD_HH_MM_SS, new Locale("ru")).parse(oldDate);

            if(date_format.format(date).equals(date_format.format(Calendar.getInstance().getTime()))) {
                return hh_mm.format(date);
            } else {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int month = cal.get(Calendar.MONTH);

                String strMonth = context.getResources().getStringArray(R.array.months_short)[month];
                String strDate = cal.get(Calendar.DAY_OF_MONTH) <= 9 ? "0" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) :
                        String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

                String strYear = "";
                if(Calendar.getInstance().get(Calendar.YEAR) != cal.get(Calendar.YEAR)) {
                    strYear = String.valueOf(cal.get(Calendar.YEAR));
                }

                String timestamp = strDate + " " + strMonth + " " +
                        (strYear.equals("") ? "" : strYear + " " + context.getResources().getString(R.string.year)); //+ ", " + hh_mm.format(date);

                return timestamp;
            }
        } catch (Exception e) {
            return oldDate;
        }
    }

}