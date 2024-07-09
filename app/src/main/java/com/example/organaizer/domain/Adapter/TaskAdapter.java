package com.example.organaizer.domain.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.organaizer.App;
import com.example.organaizer.R;
import com.example.organaizer.data.db.classes.Task;
import com.example.organaizer.domain.ViewModels.TaskViewModel;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    private LayoutInflater inflater;
    private int layout;
    private List<Task> tasks;
    private TaskViewModel viewModel;

    public interface OnCheckBoxClickListener {
        void onCheckBoxClick(Task task, boolean isChecked);
    }
    private OnCheckBoxClickListener onCheckBoxClickListener;

    public TaskAdapter(Context context, int resource, List<Task> states, TaskViewModel viewModel) {
        super(context, resource, states);
        this.tasks = states;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.viewModel = viewModel;
    }

    public void setOnCheckBoxClickListener(OnCheckBoxClickListener listener) {
        this.onCheckBoxClickListener = listener;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskAdapter.ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new TaskAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (TaskAdapter.ViewHolder) convertView.getTag();
        }
        Task task = tasks.get(position);

        viewHolder.textView.setText(task.getText());
        viewHolder.dateView.setText(task.getDate());
        viewHolder.checkBox.setChecked(task.getCompleted());

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.setCompleted(viewHolder.checkBox.isChecked());
                viewModel.updateTask(task);
                if (onCheckBoxClickListener != null) {
                    onCheckBoxClickListener.onCheckBoxClick(task, viewHolder.checkBox.isChecked());
                }
            }
        });
        return convertView;
    }
    public void updateView(List<Task> newList){
        tasks.clear();
        tasks.addAll(newList);
        this.notifyDataSetChanged();
    }
    private class ViewHolder {
        final TextView textView, dateView;
        final CheckBox checkBox;
        ViewHolder(View view){
            textView = view.findViewById(R.id.task_name);
            dateView = view.findViewById(R.id.task_date);
            checkBox = view.findViewById(R.id.checkbox);
        }
    }
}
