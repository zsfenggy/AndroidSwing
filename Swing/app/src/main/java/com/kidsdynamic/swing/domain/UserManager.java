package com.kidsdynamic.swing.domain;

import android.support.annotation.NonNull;

import com.kidsdynamic.data.dao.DB_User;
import com.kidsdynamic.data.net.ApiGen;
import com.kidsdynamic.data.net.user.model.UpdateProfileSuccess;
import com.kidsdynamic.data.net.user.model.UserInfo;
import com.kidsdynamic.data.persistent.DbUtil;
import com.kidsdynamic.data.repository.disk.UserDataStore;
import com.kidsdynamic.swing.SwingApplication;


/**
 * UserManager </br></br>
 * Created by Administrator on 2017/11/4.
 */

public class UserManager {
    public static String getProfileRealUri(String userProfile){
        return ApiGen.BASE_PHOTO_URL + userProfile;
    }

    public static void updateProfile2DB(@NonNull UpdateProfileSuccess updateProfileSuccess){
        DbUtil dbUtil = DbUtil.getInstance(SwingApplication.getAppContext());
        UserDataStore userDataStore = new UserDataStore(dbUtil);

        UserInfo userInfoNew = updateProfileSuccess.getUser();
        DB_User dbUser = userDataStore.getById(userInfoNew.getId());

        if(dbUser == null){
            return;
        }

        //开始更新
        userDataStore.update(BeanConvertor.updateDBUser(dbUser, updateProfileSuccess.getUser()));

    }

    public static void updateProfile2DB(@NonNull UserInfo userInfo){
        DbUtil dbUtil = DbUtil.getInstance(SwingApplication.getAppContext());
        UserDataStore userDataStore = new UserDataStore(dbUtil);

        DB_User dbUser = userDataStore.getById(userInfo.getId());

        if(dbUser == null){
            return;
        }

        //开始更新
        userDataStore.update(BeanConvertor.updateDBUser(dbUser, userInfo));

    }
}
