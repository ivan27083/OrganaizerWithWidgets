package com.example.organaizer.domain.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.organaizer.R;
import com.example.organaizer.data.db.classes.Aim;

import java.util.List;

public class AimAdapter extends ArrayAdapter<Aim> {
    private LayoutInflater inflater;
    private int layout;
    private List<Aim> aims;

    public AimAdapter(Context context, int resource, List<Aim> states) {
        super(context, resource, states);
        this.aims = states;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Aim aim = aims.get(position);

        viewHolder.textView.setText(aim.getText());
        viewHolder.progressBar.setProgress(aim.getProgress());

        return convertView;
    }
    private class ViewHolder {
        final TextView textView;
        final ProgressBar progressBar;
        ViewHolder(View view){
            textView = view.findViewById(R.id.aim_name);
            progressBar = view.findViewById(R.id.progressBar);
        }
    }
}
