package com.example.bright_storage.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.bright_storage.R;
import com.example.bright_storage.holder.MyBaseExpandableListAdapter;
import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.repository.StorageUnitCategoryRepository;
import com.example.bright_storage.service.CategoryService;
import com.example.bright_storage.service.StorageUnitService;
import com.example.bright_storage.service.impl.CategoryServiceImpl;
import com.example.bright_storage.service.impl.StorageUnitServiceImpl;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.kongzue.dialog.v3.Notification;
import com.kongzue.dialog.v3.TipDialog;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import static android.view.View.INVISIBLE;

public class SettingActivity extends AppCompatActivity
{
    @Inject
    StorageUnitService storageUnitService;

    @Inject
    CategoryService categoryService;

    @Inject
    StorageUnitCategoryRepository storageUnitCategoryRepository;
    public static final int TAKE_PHOTO = 2;
    private Uri imageUri;
    private String oldPassword = "123456";
    private ArrayList<String> types;
    private ArrayList<String> gData = null;
    private ArrayList<ArrayList<String>> iData = null;
    private ArrayList<String> lData = null;
    private Context mContext;
    private ExpandableListView settingsItem;
    private MyBaseExpandableListAdapter myAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        storageUnitService = new StorageUnitServiceImpl();
        categoryService = new CategoryServiceImpl();
        storageUnitCategoryRepository = new StorageUnitCategoryRepository();
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_setting);
        TextView thetitle = findViewById(R.id.title_text);
        thetitle.setText(R.string.title_Settings);
        Button title_back = (Button) findViewById(R.id.title_back);
        Button title_search = (Button) findViewById(R.id.title_search);
        title_search.setVisibility(INVISIBLE);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mContext = SettingActivity.this;
        settingsItem = (ExpandableListView) findViewById(R.id.settings_list);
        initList();
    }

    private void initList()
    {
        gData = new ArrayList<>();
        iData = new ArrayList<>();

        gData.add("个人信息");
        gData.add("分类信息");
        gData.add("备份");
        gData.add("安全");
        gData.add("关于");

        lData = new ArrayList<>();
        lData.add("修改昵称");
        lData.add("修改密码");
        lData.add("修改头像");
        lData.add("修改手机号");
        iData.add(lData);

        lData = new ArrayList<>();
        lData.add("管理分类");
        lData.add("新建物品分类");
        lData.add("新建容器分类");
        iData.add(lData);

        lData = new ArrayList<>();
        lData.add("管理备份");
        lData.add("立即备份");
        iData.add(lData);

        lData = new ArrayList<>();
        lData.add("TBC");
        iData.add(lData);

        lData = new ArrayList<>();
        lData.add("开发者信息");
        lData.add("版本信息");
        iData.add(lData);

        myAdapter = new MyBaseExpandableListAdapter(gData,iData,mContext);
        settingsItem.setAdapter(myAdapter);
        settingsItem.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            //Toast.makeText(mContext, "你点击了：" + iData.get(groupPosition).get(childPosition), Toast.LENGTH_SHORT).show();
            if(groupPosition == 0)
            {
                switch(childPosition)
                {
                    case 0:
                        InputDialog.build(SettingActivity.this)
                                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                                .setTitle("修改昵称")
                                .setMessage("请输入新昵称")
                                .setInputInfo(new InputInfo()
                                        .setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL)
                                        .setMultipleLines(true))
                                .setOkButton("确定", new OnInputDialogButtonClickListener() {
                                    @Override
                                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                        Toast.makeText(SettingActivity.this, inputStr, Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                })
                                .show();
                        break;
                    case 1:
                        InputDialog.build(SettingActivity.this)
                                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                                .setTitle("修改密码")
                                .setMessage("请输入原密码")
                                .setInputInfo(new InputInfo()
                                        .setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                                        .setMultipleLines(true))
                                .setOkButton("确定", new OnInputDialogButtonClickListener() {
                                    @Override
                                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                        if (inputStr.equals(oldPassword)) {
                                            InputDialog.build(SettingActivity.this)
                                                    .setStyle(DialogSettings.STYLE.STYLE_IOS)
                                                    .setTitle("修改密码")
                                                    .setMessage("请输入新密码")
                                                    .setInputInfo(new InputInfo()
                                                            .setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                                                            .setMultipleLines(true))
                                                    .setOkButton("确定", new OnInputDialogButtonClickListener() {
                                                        @Override
                                                        public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                                            oldPassword = inputStr;
                                                            Toast.makeText(SettingActivity.this, inputStr, Toast.LENGTH_SHORT).show();
                                                            return false;
                                                        }
                                                    })
                                                    .show();
                                            return false;
                                        } else {
                                            TipDialog.show(SettingActivity.this, "密码错误", TipDialog.TYPE.ERROR);
                                            return true;
                                        }
                                    }
                                })
                                .show();
                        break;
                    case 2:
                        showCameraDialog();
                        break;
                    case 3:
                        InputDialog.build(SettingActivity.this)
                                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                                .setTitle("修改手机号")
                                .setMessage("请输入新手机号")
                                .setInputInfo(new InputInfo()
                                        .setInputType(InputType.TYPE_CLASS_PHONE)
                                        .setMultipleLines(true))
                                .setOkButton("确定", new OnInputDialogButtonClickListener() {
                                    @Override
                                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                        if(inputStr.length() != 11 || inputStr.charAt(0) != '1')
                                            TipDialog.show(SettingActivity.this, "手机号格式错误", TipDialog.TYPE.WARNING);
                                        else
                                        {
                                            Toast.makeText(SettingActivity.this, inputStr, Toast.LENGTH_SHORT).show();
                                            return false;
                                        }
                                        return true;
                                    }
                                })
                                .show();
                        break;
                }
            }
            else if(groupPosition == 1)
            {
                if(childPosition == 0)
                {
                    Intent intent = new Intent(SettingActivity.this, TypeActivity.class);
                    intent.putExtra("types", types);
                    startActivity(intent);
                }
                else if(childPosition == 1)
                {
                    InputDialog.build(SettingActivity.this)
                            .setStyle(DialogSettings.STYLE.STYLE_IOS)
                            .setTitle("新建物品分类")
                            .setMessage("请输入您想添加的新分类")
                            .setInputInfo(new InputInfo()
                                    .setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL)
                                    .setMultipleLines(true))
                            .setOkButton("确定", new OnInputDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                    /*types = getMyTypes();
                                    for(String it : types)
                                    {
                                        if(inputStr.equals(it))
                                        {
                                            TipDialog.show(SettingActivity.this, "此分类已经存在", TipDialog.TYPE.WARNING);
                                            return true;
                                        }
                                    }
                                    Toast.makeText(SettingActivity.this, inputStr, Toast.LENGTH_SHORT).show();*/
                                    Category c1 = new Category(null, null, inputStr);
                                    categoryService.create(c1);
                                    return false;
                                }
                            })
                            .show();
                } else {
                    InputDialog.build(SettingActivity.this)
                            .setStyle(DialogSettings.STYLE.STYLE_IOS)
                            .setTitle("新建容器分类")
                            .setMessage("请输入您想添加的新分类")
                            .setInputInfo(new InputInfo()
                                    .setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL)
                                    .setMultipleLines(true))
                            .setOkButton("确定", new OnInputDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                    /*types = getMyTypes();
                                    for(String it : types)
                                    {
                                        if(inputStr.equals(it))
                                        {
                                            TipDialog.show(SettingActivity.this, "此分类已经存在", TipDialog.TYPE.WARNING);
                                            return true;
                                        }
                                    }*/
                                    Category c1 = new Category(null, null, inputStr);
                                    categoryService.create(c1);
                                    //Toast.makeText(SettingActivity.this, inputStr, Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            })
                            .show();
                }
            }
            else if(groupPosition == 2)
            {
                if(childPosition == 0)
                {
                    //开启备份
                }
                else
                {
                    //立即备份
                }
            }
            else if(groupPosition == 3)
            {

            }
            else if(groupPosition == 4)
            {
                if(childPosition == 0)
                {
                    //开启备份
                }
                else
                {
                    //立即备份
                }
            }
            return true;
        });

    }

    public void showCameraDialog()
    {
        Dialog mCameraDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.add_photo_dialog_box, null);
        //初始化视图

        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.Animation_Design_BottomSheetDialog); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        Button choosePhoto = (Button) root.findViewById(R.id.choosePhoto);
        Button takePhoto = (Button) root.findViewById(R.id.takePhoto);
        Button takeCode = (Button) root.findViewById(R.id.takeCode);
        takeCode.setVisibility(View.GONE);
        Button cancel = (Button) root.findViewById(R.id.cancel);
        takePhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCameraDialog.dismiss();
//                String path = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM+File.separator+"Camera"+File.separator;
//                System.out.println(path);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String name = format.format(date);
                File outputImage = new File(getExternalFilesDir(null), name);
                // 对照片的更换设置
                try {
                    // 如果上一次的照片存在，就删除
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    // 创建一个新的文件
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 如果Android版本大于等于7.0
                if (Build.VERSION.SDK_INT >= 24) {
                    // 将File对象转换成一个封装过的Uri对象
                    imageUri = FileProvider.getUriForFile(SettingActivity.this, "com.example.bright_storage.camera.file_provider", outputImage);
                } else {
                    // 将File对象转换为Uri对象，这个Uri标识着output_image.jpg这张图片的本地真实路径
                    imageUri = Uri.fromFile(outputImage);
                }

                // 动态申请权限
                if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) SettingActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                } else {
                    // 启动相机程序
                    startCamera();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCameraDialog.dismiss();
            }
        });
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    private void startCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 指定图片的输出地址为imageUri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case TAKE_PHOTO:
                try {
                    // 将图片解析成Bitmap对象
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    // 将图片显示出来
                    //photoButton.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private ArrayList<String> getMyTypes()
    {
        ArrayList<String> temp = new ArrayList<>();
        temp.add("食物");
        temp.add("服装");
        temp.add("书籍");
        temp.add("文件");
        return temp;
    }

}
