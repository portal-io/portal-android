package com.whaley.biz.user.ui.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.umeng.socialize.UMShareAPI;
import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.user.R;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.event.SignOutEvent;
import com.whaley.biz.user.event.UpdataAvatarEvent;
import com.whaley.biz.user.event.UpdateNameEvent;
import com.whaley.biz.user.interactor.MergeThirdBind;
import com.whaley.biz.user.interactor.ThirdUnbind;
import com.whaley.biz.user.interactor.UpdateAvatar;
import com.whaley.biz.user.model.ImageModel;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.ui.repository.UserInfoRepository;
import com.whaley.biz.user.ui.view.BigAvatarFragment;
import com.whaley.biz.user.ui.view.ClipAvatarFragment;
import com.whaley.biz.user.ui.view.EditNickNameFragment;
import com.whaley.biz.user.ui.view.EditPasswordFragment;
import com.whaley.biz.user.ui.view.EditPhoneFragment;
import com.whaley.biz.user.ui.view.UserInfoView;
import com.whaley.biz.user.utils.AuthManager;
import com.whaley.biz.user.utils.StringUtils;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.AppFileStorage;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.PermissionUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/8/3
 * Introduction:
 */

public class UserInfoPresenter extends BasePagePresenter<UserInfoView> {
    public final static int REQUEST_CAMERA = 400;
    public final static int CAMERA_RESULT = 100;
    public final static int RESULT_LOAD_IMAGE = 200;
    public final static int RESULT_CLIP_IMAGE = 300;
    public final static String KEY_RESULT_PATH = "result_path";
    public final static String IMAGE_NAME = "/temp_image.jpg";
    @Repository
    UserInfoRepository userInfoRepository;


    @UseCase
    MergeThirdBind mergeThirdBind;

    @UseCase
    UpdateAvatar updateAvatar;
    @UseCase
    ThirdUnbind thirdUnbind;
    Disposable disposable;

    public UserInfoPresenter(UserInfoView view) {
        super(view);
    }

    public UserInfoRepository getUserInfoRepository() {
        return userInfoRepository;
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        regist();
        if (UserManager.getInstance().isLogin()) {
            fetchUserInfo();
            updataName();
            updataAvatar();
            updataPhone();
            setAuth();
        } else {
            finish();
        }
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        dispose();
        unRegist();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateNameEvent event) {
        userInfoRepository.setNickname(UserManager.getInstance().getUser().getNickname());
        updataName();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SignOutEvent event) {
        finish();
    }

    private void setAuth() {
        updataQQ();
        updataWB();
        updataWX();
    }

    public void fetchUserInfo() {
        UserModel userModel = UserManager.getInstance().getUser();
        if (userModel != null) {
            userInfoRepository.setPhone(StringUtils.getPhone(userModel.getMobile()));
            userInfoRepository.setNickname(userModel.getNickname());
            userInfoRepository.setAvatarUrl(userModel.getAvatar());
            userInfoRepository.setBindQQ(!TextUtils.isEmpty(userModel.getQq()));
            userInfoRepository.setBindWB(!TextUtils.isEmpty(userModel.getWb()));
            userInfoRepository.setBindWX(!TextUtils.isEmpty(userModel.getWx()));
        }
    }

    private void updataName() {
        getUIView().showNickname(userInfoRepository.getNickname());
    }

    private void updataPhone() {
        getUIView().showPhone(userInfoRepository.getPhone());
    }

    private void updataAvatar() {
        getUIView().showAvatar(userInfoRepository.getAvatarUrl());
    }

    private void updataQQ() {
        getUIView().setBingQQ(userInfoRepository.isBindQQ());
    }

    private void updataWB() {
        getUIView().setBingWeibo(userInfoRepository.isBindWB());
    }

    private void updataWX() {
        getUIView().setBingWeixin(userInfoRepository.isBindWX());
    }

    public void onNameClick() {
        EditNickNameFragment.goPage(getStater());
    }

    public void onPhoneClick() {
        EditPhoneFragment.goPage(getStater());
    }

    public void onPasswordClick() {
        EditPasswordFragment.goPage(getStater());
    }

    public void onClickAvatarImage() {
        BigAvatarFragment.goPage(getStater(), userInfoRepository.getAvatarUrl());
    }

    public void onQQClick() {
        if (userInfoRepository.isBindQQ()) {
            showUnBindDialog(UserConstants.TYPE_AUTH_QQ);
        } else {
            thirdBind(UserConstants.TYPE_AUTH_QQ);
        }

    }

    public void onWXClick() {
        if (userInfoRepository.isBindWX()) {
            showUnBindDialog(UserConstants.TYPE_AUTH_WX);
        } else {
            thirdBind(UserConstants.TYPE_AUTH_WX);
        }

    }

    public void onWBClick() {
        if (userInfoRepository.isBindWB()) {
            showUnBindDialog(UserConstants.TYPE_AUTH_WB);
        } else {
            thirdBind(UserConstants.TYPE_AUTH_WB);
        }

    }

    private void thirdBind(final String type) {
        MergeThirdBind.MergeThirdBindParam param = new MergeThirdBind.MergeThirdBindParam(getUIView().getAttachActivity(), type);
        disposable = mergeThirdBind.buildUseCaseObservable(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<String>(getUIView(), true) {
                    @Override
                    public void onNext(@NonNull String s) {
                        super.onNext(s);
                        switch (type) {
                            case UserConstants.TYPE_AUTH_QQ:
                                userInfoRepository.setBindQQ(true);
                                break;
                            case UserConstants.TYPE_AUTH_WX:
                                userInfoRepository.setBindWX(true);
                                break;
                            default:
                                userInfoRepository.setBindWB(true);
                        }
                        getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_bind_sucess));
                        setAuth();
                    }

                    @Override
                    public void onStatusError(int status, String message) {
                        if (status == UserConstants.THIRD_CANCEL) {
                            message = "取消绑定";
                        }
                        if (status == UserConstants.THIRD_ERROR) {
                            message = "绑定失败";
                        }
                        super.onStatusError(status, message);

                    }
                });
//        mergeThirdBind.execute(new UpdateUIObserver<String>(getUIView(), true) {
//            @Override
//            public void onNext(@NonNull String u) {
//                super.onNext(u);
//                switch (type) {
//                    case UserConstants.TYPE_AUTH_QQ:
//                        userInfoRepository.setBindQQ(true);
//                        break;
//                    case UserConstants.TYPE_AUTH_WX:
//                        userInfoRepository.setBindWX(true);
//                        break;
//                    default:
//                        userInfoRepository.setBindWB(true);
//                }
//                getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_bind_sucess));
//                setAuth();
//            }
//
//            @Override
//            public void onStatusError(int status, String message) {
//                super.onStatusError(status, message);
//
//            }
//        }, getUIView().getAttachActivity(), type);
    }

    private void showUnBindDialog(final String type) {
        DialogUtil.showDialog(getUIView().getAttachActivity(), AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_unbind), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (PermissionUtil.requestPermission(getUIView().getAttachActivity(), Manifest.permission.READ_PHONE_STATE, 1)) {
//                    return;
//                }
                unBind(type);
            }
        });
    }

    private void unBind(final String type) {
        thirdUnbind.execute(new UpdateUIObserver<String>(getUIView(), true) {
            @Override
            public void onNext(@NonNull String s) {
                super.onNext(s);
                switch (type) {
                    case UserConstants.TYPE_AUTH_WX:
                        userInfoRepository.setBindWX(false);
                        break;
                    case UserConstants.TYPE_AUTH_WB:
                        userInfoRepository.setBindWB(false);
                        break;
                    case UserConstants.TYPE_AUTH_QQ:
                        userInfoRepository.setBindQQ(false);
                        break;
                }
                getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_unbind_sucess));
                setAuth();
            }
        }, type);
    }

    public void onAvatarClick() {
        DialogUtil.showFetchAvatarDialog(getUIView().getAttachActivity(), new DialogUtil.AvatarDialogListener() {
            @Override
            public void onClickCamera() {
                if (!setCameraParams() || ContextCompat.checkSelfPermission(getUIView().getAttachActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getUIView().getAttachActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getUIView().getAttachActivity(), new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA);
                    return;
                }
                toCamera();
            }

            @Override
            public void onClickGallery() {
                if (ContextCompat.checkSelfPermission(getUIView().getAttachActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getUIView().getAttachActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CAMERA);
                    return;
                }
                toAlbum();
            }

            @Override
            public void onClickCancel() {
            }
        });
    }

    private boolean setCameraParams() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            if (mCamera != null) {
                Camera.Parameters mParameters = mCamera.getParameters();
                mParameters.set("orientation", "portrait");
                mCamera.setParameters(mParameters);
            }
        } catch (Exception e) {
            Log.e(e, "");
            isCanUse = false;
        }
        try {
            if (mCamera != null) {
                mCamera.release();
            }
        } catch (Exception emCamera) {
            emCamera.printStackTrace();
        }
        return isCanUse;
    }


    private void toAlbum() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        getStater().startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void toCamera() {
        destoryImage();
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            userInfoRepository.setmPhotoFile(new File(AppFileStorage.getSnapPath(), IMAGE_NAME));
            userInfoRepository.getmPhotoFile().delete();
            if (!userInfoRepository.getmPhotoFile().exists()) {
                try {
                    userInfoRepository.getmPhotoFile().createNewFile();
                } catch (IOException e) {
                    Log.e(e, "");
                    Toast.makeText(AppContextProvider.getInstance().getContext(), "照片创建失败!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                Uri imageUri = Uri.fromFile(userInfoRepository.getmPhotoFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                Uri photoURI = FileProvider.getUriForFile(getStater().getAttatchContext(),
                        CommonConstants.APPLICATION_ID + ".provider",
                        userInfoRepository.getmPhotoFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }
            getStater().startActivityForResult(intent, CAMERA_RESULT);
        } else {
            Toast.makeText(AppContextProvider.getInstance().getContext(), "sdcard无效或没有插入!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void destoryImage() {
        if (userInfoRepository.getPhoto() != null) {
            userInfoRepository.getPhoto().recycle();
            userInfoRepository.setPhoto(null);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            toCamera();
            return;
        }
        if (requestCode == CAMERA_RESULT && resultCode == Activity.RESULT_OK) {
            if (userInfoRepository.getmPhotoFile() != null && userInfoRepository.getmPhotoFile().exists()) {
                ClipAvatarFragment.toClipAvatar(getStater(), userInfoRepository.getmPhotoFile(), null, false, RESULT_CLIP_IMAGE);
            } else {
                // error
            }
            return;
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            ClipAvatarFragment.toClipAvatar(getStater(), null, selectedImage, true, RESULT_CLIP_IMAGE);
            return;
        }
        if (requestCode == RESULT_CLIP_IMAGE && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            final String savedPath = bundle.getString(KEY_RESULT_PATH);
            if (TextUtils.isEmpty(savedPath)) {
                getUIView().showToast("图片裁剪失败");
                return;
            }
            changeNewAvatar(savedPath);
        } else {
        }
        AuthManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UserConstants.THIRD_WEIBO && data == null) {
            if (getUIView() != null) {
                dispose();
                getUIView().removeLoading();
            }
        }
    }


    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void changeNewAvatar(String savedPath) {
        updateAvatar.execute(new UpdateUIObserver<ImageModel>(getUIView(), true) {
            @Override
            public void onNext(@NonNull ImageModel imageModel) {
                super.onNext(imageModel);
                EventController.postEvent(new UpdataAvatarEvent(UserConstants.EVENT_UPATE_AVATAR));
                getUIView().showAvatar(UserManager.getInstance().getUser().getAvatar());
            }
        }, savedPath);
    }

    public void onLogoutClick() {
        DialogUtil.showDialog(getUIView().getAttachActivity(), AppContextProvider.getInstance().getContext().getResources().getString(R.string.text_logout_confirm), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManager.signOut();
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (permissions.length >= 1) {
                if (Manifest.permission.CAMERA.equals(permissions[0])) {
                    toCamera();
                } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permissions[0])) {
                    toAlbum();
                }
            }
        }
    }
}
