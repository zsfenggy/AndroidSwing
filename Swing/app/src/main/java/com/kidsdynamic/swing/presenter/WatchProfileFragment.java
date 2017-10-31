package com.kidsdynamic.swing.presenter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kidsdynamic.data.net.ApiGen;
import com.kidsdynamic.data.net.avatar.AvatarApi;
import com.kidsdynamic.data.net.avatar.PartUtils;
import com.kidsdynamic.data.net.kids.KidsApi;
import com.kidsdynamic.data.net.kids.model.KidsAddRequest;
import com.kidsdynamic.data.net.kids.model.KidsWithParent;
import com.kidsdynamic.data.net.user.model.UpdateKidAvatarRepEntity;
import com.kidsdynamic.data.utils.LogUtil2;
import com.kidsdynamic.swing.BaseFragment;
import com.kidsdynamic.swing.R;
import com.kidsdynamic.swing.view.BottomPopWindow;
import com.kidsdynamic.swing.view.CropImageView;
import com.kidsdynamic.swing.view.CropPopWindow;
import com.kidsdynamic.swing.view.ViewCircle;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * WatchProfileFragment
 * <p>
 * Created by Stefan on 2017/10/25.
 */

public class WatchProfileFragment extends BaseFragment {

    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_ALBUM = 2;


    private static final String MAC_ID = "mac_id";

    @BindView(R.id.watch_profile_photo)
    ViewCircle vc_photo;
    @BindView(R.id.watch_profile_first)
    EditText et_first;
    @BindView(R.id.watch_profile_last)
    EditText et_last;
    @BindView(R.id.watch_profile_zip)
    EditText et_zip;

    private File profile;

    public static WatchProfileFragment newInstance(String macId) {
        Bundle args = new Bundle();
        args.putString(MAC_ID, macId);
        WatchProfileFragment fragment = new WatchProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_watch_profile, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                doCameraResult(data);
                break;
            case REQUEST_CODE_ALBUM:
                doAlbumResult(data);
                break;
        }
    }

    @OnClick(R.id.ib_back)
    public void back() {
        SignupActivity signupActivity = (SignupActivity) getActivity();
        signupActivity.setFragment(SignupLoginFragment.newInstance());
    }

    @OnClick(R.id.watch_profile_photo)
    public void addPhoto() {
        CharSequence array[] = new CharSequence[]{"Take a new picture", "Choose from Library"};
        BottomPopWindow.Builder builder = new BottomPopWindow.Builder(getContext());
        builder.setItems(array, new BottomPopWindow.Builder.OnWhichClickListener() {
            @Override
            public void onWhichClick(View v, int position) {
                doWhichClick(position);
            }
        });
        BottomPopWindow bottomPopWindow = builder.create();
        bottomPopWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @OnClick(R.id.watch_profile_submit)
    public void doSubmit() {
        if (null == profile || !profile.exists()) {
            return;
        }
        String firstName = et_first.getText().toString().trim();
        if (TextUtils.isEmpty(firstName)) {
            return;
        }
        String lastName = et_last.getText().toString().trim();
        if (TextUtils.isEmpty(lastName)) {
            return;
        }
//        uploadAvatar(profile, firstName, lastName);
        Bundle args = getArguments();
        String macId = args.getString(MAC_ID);
        addKids(macId, profile, String.format("%1$s %2$s", firstName, lastName));
    }

    /**
     * add kids
     *
     * @param watchMacId String
     * @param kidsName   String
     */
    public void addKids(String watchMacId, final File profile, String kidsName) {
        KidsApi kidsApi = ApiGen
                .getInstance(getContext().getApplicationContext())
                .generateApi(KidsApi.class, true);

        KidsAddRequest kidsAddRequest = new KidsAddRequest();
        kidsAddRequest.setMacId(watchMacId);
        kidsAddRequest.setName(kidsName);

        kidsApi.addKid(kidsAddRequest).enqueue(new Callback<KidsWithParent>() {
            @Override
            public void onResponse(Call<KidsWithParent> call, Response<KidsWithParent> response) {
                LogUtil2.getUtils().d("addKid onResponse: " + response.code());
                if (response.code() == 200) {
                    //add successfully
                    KidsWithParent kidsWithParent = response.body();
                    uploadAvatar(profile, String.valueOf(kidsWithParent.getId()));
                    LogUtil2.getUtils().d("addKid rep kid ID: " + response.body().getId());
                } else {
                    // TODO: 2017/10/31 add error msg
                }
            }

            @Override
            public void onFailure(Call<KidsWithParent> call, Throwable t) {
                // TODO: 2017/10/31 add error msg
                LogUtil2.getUtils().d("addKid onFailure");
                t.printStackTrace();
            }
        });
    }

    /**
     * upload kid's avatar
     *
     * @param profile File
     * @param kidsId  String
     */
    public void uploadAvatar(File profile, String kidsId) {
        final AvatarApi avatarApi = ApiGen.getInstance(getContext().getApplicationContext()).
                generateApi4Avatar(AvatarApi.class);
        MultipartBody.Part filePart =
                PartUtils.prepareFilePart("upload", profile.getName(), profile);
        avatarApi
                .uploadKidAvatar(getUploadKidAvatarPram(kidsId), filePart)
                .enqueue(new Callback<UpdateKidAvatarRepEntity>() {
                    @Override
                    public void onResponse(Call<UpdateKidAvatarRepEntity> call,
                                           Response<UpdateKidAvatarRepEntity> response) {
                        LogUtil2.getUtils().d("uploadKidAvatar onResponse");
                        LogUtil2.getUtils().d("uploadKidAvatar  code: " + response.code());
                        //code == 200 upload ok
                        int code = response.code();
                        if (200 == code) {
                            SignupActivity signupActivity = (SignupActivity) getActivity();
                            signupActivity.setFragment(WatchHaveFragment.newInstance());
                        } else {
                            // TODO: 2017/10/31  show Error Msg
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateKidAvatarRepEntity> call, Throwable t) {
                        LogUtil2.getUtils().d("uploadKidAvatar onFailure");
                        t.printStackTrace();
                    }
                });
    }

    private Map<String, RequestBody> getUploadKidAvatarPram(String kidsId) {
        HashMap<String, RequestBody> paramMap = new HashMap<>();
        PartUtils.putRequestBodyMap(paramMap, AvatarApi.param_kidId, kidsId);
        return paramMap;
    }

    private void doWhichClick(int position) {
        switch (position) {
            case 0:
                // TODO: 2017/10/25 add Camera permission
                startCameraActivity();
                break;
            case 1:
                // TODO: 2017/10/25 add reading storage permission
                startAlbumActivity();
                break;
        }
    }

    /**
     * Start virtual camera
     */
    private void startCameraActivity() {
        Intent intent = new Intent();
        intent.setClass(getContext(), VirtualCameraActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * Do result after taking picture
     *
     * @param data Intent
     */
    private void doCameraResult(Intent data) {
        if (null == data) return;
        File file = (File) data.getSerializableExtra(VirtualCameraActivity.FILE);
        if (file != null && file.exists()) {
            showCropPopWindow(file);
        }
    }

    /**
     * Start album
     */
    private void startAlbumActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_ALBUM);
    }

    /**
     * Do result after picking picture from album
     *
     * @param data Intent
     */
    private void doAlbumResult(Intent data) {
        if (data == null) return;
        Uri uri = data.getData();
        if (uri != null) {
            File file = getRealPathFromUri(uri);
            showCropPopWindow(file);
        }
    }

    private File getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        String[] project = {MediaStore.Images.Media.DATA};

        try {
            ContentResolver contentResolver = getContext().getContentResolver();
            if (null == contentResolver) {
                return null;
            }
            cursor = contentResolver.query(contentUri, project, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(project[0]);
                String path = cursor.getString(columnIndex);
                if (!TextUtils.isEmpty(path)) {
                    return new File(path);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private void showCropPopWindow(final File file) {
        final CropPopWindow cropPopWindow = new CropPopWindow(getContext());
        cropPopWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
        cropPopWindow.setCropImageFile(file);
        cropPopWindow.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                cropPopWindow.dismiss();
                if (null == result) {
                    return;
                }
                vc_photo.setStrokeWidth(4.0f);
                vc_photo.setCrossWidth(0.0f);
                vc_photo.setBitmap(result.bitmap);
                profile = file;
            }
        });
    }

}
