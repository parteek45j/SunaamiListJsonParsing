package com.example.parteek.sunaamilist;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Parteek on 10/2/2017.
 */

public class CustomAdapter extends ArrayAdapter<Event> {
    Context context;
    int resource;
    ArrayList<Event> arrayList;
    Event event;
    GradientDrawable magnitudeCircle;
    int magnitudeResource;
    String distacne;
    String place;

    public CustomAdapter(@NonNull Context context, int resource, ArrayList<Event> arrayList) {
        super(context, resource,  arrayList);
        this.context=context;
        this.resource=resource;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=null;
        event=getItem(position);
        view= LayoutInflater.from(context).inflate(resource,parent,false);
        TextView textMag=(TextView)view.findViewById(R.id.textmag);
        magnitudeCircle=(GradientDrawable)textMag.getBackground();
        int color=getColor1(event.getMagnitude());
        magnitudeCircle.setColor(color);
        textMag.setText(String.valueOf(event.getMagnitude()));

        TextView textTime=(TextView)view.findViewById(R.id.texttime);
        TextView textTime1=(TextView)view.findViewById(R.id.texttime1);
        Date date=new Date(event.getTime());
        String date1=DateFormatter(date);
        String time=TimeFormatter(date);
        textTime1.setText(time);
        textTime.setText(date1);

        TextView textPlace=(TextView)view.findViewById(R.id.textplace);
        TextView textDistance=(TextView)view.findViewById(R.id.distance);
        splitIt(event.getTitle());
        textPlace.setText(place);
        textDistance.setText(distacne);
        return view;
    }
    void splitIt(String msg){
        if(msg.contains("of")){
            String[] distance1 = event.getTitle().split("of");
            distacne = distance1[0]+"Near";
            place = distance1[1];
        }else{
            distacne="Near";
            place=msg;
        }
    }
    String DateFormatter(Date date){
        SimpleDateFormat dateFormat=new SimpleDateFormat("LLL dd/yyyy");
        return dateFormat.format(date);
    }
    String TimeFormatter(Date date){
        SimpleDateFormat timeFormat=new SimpleDateFormat("h:mm a");
        return timeFormat.format(date);
    }
    public int getColor1(double magnitude){
        int magnitudeFloor=(int) Math.floor(magnitude);
        switch (magnitudeFloor){
            case 1:
                magnitudeResource=R.color.color1;
                break;
            case 2:
                magnitudeResource=R.color.color2;
                break;
            case 3:
                magnitudeResource=R.color.color3;
                break;
            case 4:
                magnitudeResource=R.color.color4;
                break;
            case 5:
                magnitudeResource=R.color.color5;
                break;
            case 6:
                magnitudeResource=R.color.color6;
                break;
            case 7:
                magnitudeResource=R.color.color7;
                break;
            case 8:
                magnitudeResource=R.color.color8;
                break;
            case 9:
                magnitudeResource=R.color.color9;
                break;
            case 10:
                magnitudeResource=R.color.color10;
                break;
            default:
                magnitudeResource=R.color.color1;
                break;
        }
        return ContextCompat.getColor(getContext(),magnitudeResource);
    }
}
