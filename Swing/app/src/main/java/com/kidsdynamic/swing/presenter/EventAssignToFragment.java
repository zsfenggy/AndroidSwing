package com.kidsdynamic.swing.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kidsdynamic.swing.R;
import com.kidsdynamic.swing.domain.DeviceManager;
import com.kidsdynamic.swing.domain.UserManager;
import com.kidsdynamic.swing.model.KidsEntityBean;
import com.kidsdynamic.swing.model.WatchEvent;
import com.kidsdynamic.swing.utils.GlideHelper;
import com.yy.base.utils.ToastCommon;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.carbs.android.avatarimageview.library.AvatarImageView;

/**
 * EventAssignToFragment
 */

public class EventAssignToFragment extends CalendarBaseFragment {
    @BindView(R.id.listview_kids)
    protected ListView listView_kids;

    private WatchEvent mEvent;
    private EventAssignToListAdapter eventAssignToListAdapter;

    private long assignToKidsId = -1;
    private long userId = -1;
    List<KidsEntityBean> allKidsByUserId = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        if (arg != null) {
            assignToKidsId = getArguments().getLong(DeviceManager.BUNDLE_KEY_KID_ID, -1);
            userId = getArguments().getLong(DeviceManager.BUNDLE_KEY_USER_ID, -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_calendar_assign_to, container, false);

        ButterKnife.bind(this,mView);
        initTitleBar();

        initValue();
        initView();

        return mView;
    }

    private void initValue() {
//        mainFrameActivity.mEventStack.pop();

        allKidsByUserId = DeviceManager.getAllKidsByUserId(getContext(), userId);

        //test todo
        KidsEntityBean kidsEntityBean = new KidsEntityBean();
        kidsEntityBean.setKidsId(123);
        kidsEntityBean.setName("Alex Smith");
        kidsEntityBean.setParentId(123);
        allKidsByUserId.add(kidsEntityBean);
    }

    private void initTitleBar() {
        tv_title.setTextColor(getResources().getColor(R.color.colorAccent));
        tv_title.setText(R.string.calendar_event_assign);
        view_left_action.setImageResource(R.drawable.icon_left);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mainFrameActivity.mEventStack.isEmpty()) {
            ToastCommon.showToast(getContext(),"event null");
            getFragmentManager().popBackStack();
        }else {
            mEvent = mainFrameActivity.mEventStack.pop();
        }
    }

    @OnClick(R.id.main_toolbar_title)
    public void onToolbarAction1() {
        mainFrameActivity.mEventStack.push(mEvent);
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void initView() {
        eventAssignToListAdapter = new EventAssignToListAdapter(allKidsByUserId);
        listView_kids.setAdapter(eventAssignToListAdapter);
        listView_kids.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                handleOnItemClick(i);
            }
        });
    }

    private void handleOnItemClick(int i) {
        /*Bundle bundle = new Bundle();
        bundle.putString(CalendarManager.KEY_DATA_TYPE,CalendarManager.VALUE_DATA_TYPE_REPEAT);
        bundle.putString(CalendarManager.KEY_DATA_TYPE_REPEAT_VALUE,
                eventRepeatListAdapter.getItem(i));

        String optionShowStr = getContext().getString(
                eventOptionMap.get(eventRepeatListAdapter.getItem(i)));
        bundle.putString(CalendarManager.KEY_DATA_TYPE_REPEAT_STR,
                optionShowStr);

        mainFrameActivity.mCalendarBundleStack.push(bundle);*/

        mainFrameActivity.mEventStack.push(mEvent);
        getActivity().getSupportFragmentManager().popBackStack();
    }


    private class EventAssignToListAdapter extends BaseAdapter{
        private final List<KidsEntityBean> kidsList;
        private LayoutInflater inflater;

        public EventAssignToListAdapter(List<KidsEntityBean> kidsList){
            inflater = LayoutInflater.from(getContext());
            this.kidsList = kidsList;
        }

        @Override
        public int getCount() {
            return kidsList.size();
        }

        @Override
        public KidsEntityBean getItem(int i) {
            return kidsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = inflater.inflate(R.layout.item_kids_assign,parent,false);
                viewHolder = ViewHolder.initView(convertView);

                convertView.setTag(viewHolder);

            }else {
                viewHolder = ((ViewHolder) convertView.getTag());
            }

            //赋值
            KidsEntityBean dbKids = getItem(position);

            //获取真实的头像地址，并显示
            String profileRealUri = UserManager.getProfileRealUri(dbKids.getProfile());
            GlideHelper.showCircleImageView(getContext().getApplicationContext(),profileRealUri,
                    viewHolder.kidsAvatarView);

            viewHolder.tv_kids_name.setText(dbKids.getName());

            if(assignToKidsId == dbKids.getKidsId()){
                viewHolder.view_selected.setVisibility(View.VISIBLE);
            }else {
                viewHolder.view_selected.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }
    }

    private static class ViewHolder{
        AvatarImageView kidsAvatarView;
        TextView tv_kids_name;
        ImageView view_selected;

        static ViewHolder initView(View containView){
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.kidsAvatarView = containView.findViewById(R.id.user_avatar_iv);
            viewHolder.tv_kids_name = containView.findViewById(R.id.tv_kids_name);
            viewHolder.view_selected = containView.findViewById(R.id.view_selected);

            return viewHolder;
        }
    }
}
