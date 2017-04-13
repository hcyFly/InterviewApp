package com.accenture.cn.interview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.accenture.cn.interview.R;
import com.accenture.cn.interview.model.InterviewInfo;
import com.accenture.cn.interview.widget.SupportMultipleScreensUtil;

import java.util.HashMap;
import java.util.List;


/**
 * Created by chengyou.huang on 2016/12/29.
 */

public class WaitInterviewAdapter extends BaseExpandableListAdapter {
    /**
     * 一级菜单数据源
     */
    List<String> parents = null;

    /**
     * 二级菜单数据源
     */
    HashMap<String, List<InterviewInfo>> map = null;

    /**
     * 上下文对象
     */
    Context mContext = null;

    /**
     * 适配器构造方法
     *
     * @param parents
     * @param map
     * @param context
     */
    public WaitInterviewAdapter(Context context, List<String> parents, HashMap<String, List<InterviewInfo>> map) {
        super();
        this.mContext = context;
        this.parents = parents;
        this.map = map;

    }

    @Override
    public int getGroupCount() {
        return this.parents.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        if (groupPosition >= parents.size()) {
//            return 0;
//        }
//        String key = parents.get(groupPosition);
//        int size = map.get(key).size();
//        return size;
        return this.map.get(this.parents.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parents.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String key = parents.get(groupPosition);
        return (map.get(key).get(childPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.waitinterview_itme_parent, null);
            SupportMultipleScreensUtil.scale(convertView);
            groupHolder = new GroupHolder();
            groupHolder.tv_parent_time = (TextView) convertView.findViewById(R.id.tv_parent_time);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        String title = parents.get(groupPosition);
        groupHolder.tv_parent_time.setText(title == null ? " " : title);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.waitinterview_itme_child, null);
            SupportMultipleScreensUtil.scale(convertView);
            childHolder = new ChildHolder();
            childHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(childHolder);

        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        InterviewInfo interviewInfo = map.get(parents.get(groupPosition)).get(childPosition);
        String namename = interviewInfo.getChineseName();
        String skillskill = interviewInfo.getInterviewSkill();
        String nameORskill = namename + " / " + skillskill;

        childHolder.tv_title.
                setText(nameORskill.equals(" / ") ? "-----null----" : nameORskill);
        convertView.setBackgroundColor(mContext.getResources().getColor(R.color.color_item_ffffff));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 列表加载更多列表加载更多
     *
     * @param parents 更改后的一级数据源
     * @param map     更改后的二级数据源
     */
    public void update(List<String> parents, HashMap<String, List<InterviewInfo>> map) {
        this.parents = parents;
        this.map = map;
        notifyDataSetChanged();
    }

    public static class GroupHolder {
        TextView tv_parent_time;
    }

    public static class ChildHolder {
        TextView tv_title;
    }
}
