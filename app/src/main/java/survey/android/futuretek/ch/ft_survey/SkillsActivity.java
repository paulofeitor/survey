/**
 * Copyright (C) futuretek AG 2016
 * All Rights Reserved
 *
 * @author Artan Veliju
 */
package survey.android.futuretek.ch.ft_survey;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class SkillsActivity extends BaseActivity {
    private Button addBtn;
    private ListView listview;
    public List<String> _productlist = new ArrayList<String>();
    private ListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        listview = (ListView) findViewById(R.id.listView);
        View mainTextView = findViewById(R.id.textLayout);
        mainTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        addBtn = (Button) findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestNewSkill();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        addBtn.setTextColor(Color.GREEN);
        addBtn.setEnabled(true);
        ((ViewGroup)findViewById(R.id.textLayout)).removeAllViews();
        List<String> textArray = new ArrayList<>(1);
        textArray.add("Please add a developer skill");
        animateText(textArray);
        _productlist.clear();
        _productlist = getDatabase().getAllSkills();
        adapter = new ListAdapter(this);
        listview.setAdapter(adapter);
    }

    private class ListAdapter extends BaseAdapter {
        LayoutInflater inflater;
        ViewHolder viewHolder;

        public ListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return _productlist.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_row, null);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.textView1);
                viewHolder.delBtn = (Button) convertView.findViewById(R.id.deleteBtn);
                viewHolder.delBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ViewGroup row = ((ViewGroup)v.getParent());
                        String id = ((TextView)row.findViewById(R.id.textView1)).getText().toString();
                        getDatabase().deleteSkill(id);
                        _productlist.remove(id);
                        adapter.notifyDataSetChanged();
                    }
                });
                viewHolder.editBtn = (Button) convertView.findViewById(R.id.editBtn);
                viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ViewGroup row = ((ViewGroup)v.getParent());
                        String id = ((TextView)row.findViewById(R.id.textView1)).getText().toString();
                        requestUpdateSkill(id);
                    }
                });
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(_productlist.get(position));
            return convertView;
        }
    }

    private class ViewHolder {
        TextView textView;
        Button delBtn;
        Button editBtn;
    }

    private void requestNewSkill(){
        openInputDialog(new View.OnClickListener() {
            public void onClick(View v) {
                EditText userInput = ((EditText) v.findViewById(R.id.userInput));
                String skillName = userInput.getText().toString();
                if (skillName == null || skillName.isEmpty()) {
                    List<String> textArray = new ArrayList<String>(1);
                    textArray.add("New skill cannot be empty!");
                    animateText(textArray, new AnimationListDone() {
                        public void done() {
                            requestNewSkill();
                        }
                    });
                } else {
                    insertSkill(skillName);
                    List<String> textArray = new ArrayList<String>(2);
                    textArray.add("New Skill Added: " + skillName + "!");
                    animateText(textArray);
                }
            }
        });
    }

    private void requestUpdateSkill(final String id){
        openInputDialog(new View.OnClickListener() {
            public void onClick(View v) {
                EditText userInput = ((EditText) v.findViewById(R.id.userInput));
                String skillName = userInput.getText().toString();
                if (skillName == null || skillName.isEmpty()) {
                    List<String> textArray = new ArrayList<String>(1);
                    textArray.add("Updated skill cannot be empty!");
                    animateText(textArray, new AnimationListDone() {
                        public void done() {
                            requestNewSkill();
                        }
                    });
                } else {
                    getDatabase().updateSkill(id, skillName);
                    _productlist = getDatabase().getAllSkills();
                    adapter.notifyDataSetChanged();
                    List<String> textArray = new ArrayList<String>(2);
                    textArray.add("Updated from " + id + " to " + skillName + "!");
                    animateText(textArray);
                }
            }
        });
    }


    private void insertSkill(String skill){
        try {
            getDatabase().putSkill(skill);
            _productlist = getDatabase().getAllSkills();
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}