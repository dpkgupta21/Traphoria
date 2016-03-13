/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.traphoria.search.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.model.AccordianDTO;

import java.util.List;

public class TraditionExpandableAdapter extends BaseExpandableListAdapter {

    private List<AccordianDTO> data;
    private Activity mActivity;
    public LayoutInflater mInflater;

    public TraditionExpandableAdapter(Activity mActivity,
                                      List<AccordianDTO> data) {
        this.data = data;
        this.mActivity = mActivity;
        mInflater = mActivity.getLayoutInflater();
    }


    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getDescription();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.group_row_layout, null);
        }

        AccordianDTO group = (AccordianDTO) getGroup(groupPosition);
        TextView txt_group_lbl = (TextView) convertView.findViewById(R.id.txt_group_lbl);
        txt_group_lbl.setText(group.getTitle());
        //txt_group_lbl.setChecked(isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String children = (String) getChild(groupPosition, childPosition);
        TextView txt_child_lbl = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.child_row_layout, null);
        }
        txt_child_lbl = (TextView) convertView.findViewById(R.id.txt_child_lbl);
        txt_child_lbl.setText(children);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }
}