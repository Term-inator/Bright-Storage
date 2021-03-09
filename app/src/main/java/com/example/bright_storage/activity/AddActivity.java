package com.example.bright_storage.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.example.bright_storage.R;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.view.View.INVISIBLE;

public class AddActivity extends AppCompatActivity
{
    private Uri imageUri;
    public static final int SELECT_PATH = 1, TAKE_PHOTO = 2, CHOOSE_PHOTO = 3, REQUEST_CODE = 5;
    private Button title_back, title_search;
    private Button choosePhoto, takePhoto, takeCode, cancel;
    private EditText objectName, objectCount, objectRemarks;
    private Button objectPath, objectType, objectDate, objectShelfLife, objectSubmit;
    private ImageButton photoButton;
    private TextView thetitle;
    private OptionsPickerView shelfLifeOptions, typeOptions;
    private TimePickerView dateOptions;
    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<String> optionsForType = new ArrayList<>();//类型列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZXingLibrary.initDisplayOpinion(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add);
        thetitle = (TextView) findViewById(R.id.title_text);
        title_back = (Button) findViewById(R.id.title_back);
        title_search = (Button) findViewById(R.id.title_search);
        photoButton = (ImageButton) findViewById(R.id.photoButton);
        objectName = (EditText) findViewById(R.id.object_name);
        objectCount = (EditText) findViewById(R.id.object_count);
        objectRemarks = (EditText) findViewById(R.id.object_remarks);
        objectPath = (Button) findViewById(R.id.object_path);
        objectType = (Button) findViewById(R.id.object_type);
        objectDate = (Button) findViewById(R.id.object_date);
        objectShelfLife = (Button) findViewById(R.id.object_shelflife);
        objectSubmit = (Button) findViewById(R.id.object_submit);

        thetitle.setText(R.string.title_add);
        title_search.setVisibility(INVISIBLE);
        objectCount.setInputType( InputType.TYPE_CLASS_NUMBER);
        initTypeData();//把类型数据导入
        initDateData();
        initOptionPicker();
        initTimePicker();

        photoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showCameraDialog();
            }
        });
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        objectPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, PathSelectActivity.class);
                startActivityForResult(intent, SELECT_PATH);
            }
        });
        objectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateOptions.show(v);
            }
        });
        objectShelfLife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shelfLifeOptions.show();
            }
        });
        objectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOptions.show();
            }
        });
        objectSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hint = "请输入：";
                boolean legal = true;
                String name = objectName.getText().toString();
                int count;
                String remarks = objectRemarks.getText().toString();
                String path = objectPath.getText().toString();
                String type = objectType.getText().toString();
                String date = objectDate.getText().toString();
                String shelfLife = objectShelfLife.getText().toString();
                if(name.length() == 0)
                {
                    legal = false;
                    hint += "名称 ";
                }
                if(objectCount.getText().toString().length() != 0)
                {
                    count = Integer.parseInt(objectCount.getText().toString());
                }
                else
                {
                    legal = false;
                    hint += "数量 ";
                }
                if(path.length() == 0)
                {
                    legal = false;
                    hint += "路径 ";
                }
                if(type.length() == 0)
                {
                    legal = false;
                    hint += "分类 ";
                }
                if(!legal)
                {
                    Toast.makeText(AddActivity.this, hint, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //TODO 提交数据
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case SELECT_PATH:
                if(data != null)
                {
                    ArrayList<String> result = data.getExtras().getStringArrayList("pathName");//得到新Activity 关闭后返回的数据
                    objectPath.setText(result.get(0));
                }
                break;
            case TAKE_PHOTO:
                try {
                        // 将图片解析成Bitmap对象
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        // 将图片显示出来
                    photoButton.setImageBitmap(bitmap);
                        //picture.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        //4.4及以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case REQUEST_CODE:
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        Toast.makeText(AddActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();

        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document 类型的uri ，则通过document id处理

            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //如果uri的authority是media格式 再次解析id 将字符串分割取出后半部分才能得到真正的数字id
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，直接获取图片路径
            imagePath = uri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片
    }

    private String getImagePath(Uri externalContentUri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实图片路径
        Cursor cursor = getContentResolver().query(externalContentUri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            photoButton.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "获取图片失败，请重试", Toast.LENGTH_SHORT).show();
        }

    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "请获取权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void initOptionPicker() {//条件选择器初始化

        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */
        typeOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = optionsForType.get(options1);
                objectType.setText(tx);
            }
        })
                .setCancelText("取消")
                .setSubmitText("确定")
                .setTitleText("分类")
                .setContentTextSize(20)//设置滚轮文字大小
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setOutSideColor(0x00000000) //设置外部遮罩颜色
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String tx = optionsForType.get(options1);
                        objectType.setText(tx);
                    }
                })
                .build();

        shelfLifeOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options2Items.get(options1).get(options2) + options1Items.get(options1);
                objectShelfLife.setText(tx);
            }
        })
                .setCancelText("取消")
                .setSubmitText("确定")
                .setTitleText("保质期")
                .setContentTextSize(20)//设置滚轮文字大小
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideColor(0x00000000) //设置外部遮罩颜色
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String tx = options2Items.get(options1).get(options2) + options1Items.get(options1);
                        objectShelfLife.setText(tx);
                    }
                })
                .build();
        shelfLifeOptions.setPicker(options1Items, options2Items);
        typeOptions.setPicker(optionsForType);
//        pvOptions.setSelectOptions(1,1);
        //pathOptions.setPicker(options1Items);//一级选择器*/
        //pathOptions.setPicker(options1Items, options2Items);//二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/
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
        choosePhoto = (Button) root.findViewById(R.id.choosePhoto);
        takePhoto = (Button) root.findViewById(R.id.takePhoto);
        takeCode = (Button) root.findViewById(R.id.takeCode);
        cancel = (Button) root.findViewById(R.id.cancel);
        takePhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCameraDialog.dismiss();
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
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
                    imageUri = FileProvider.getUriForFile(AddActivity.this, "com.example.bright_storage.camera.file_provider", outputImage);
                } else {
                    // 将File对象转换为Uri对象，这个Uri标识着output_image.jpg这张图片的本地真实路径
                    imageUri = Uri.fromFile(outputImage);
                }

                // 动态申请权限
                if (ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) AddActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                } else {
                    // 启动相机程序
                    startCamera();
                }
            }
        });

        choosePhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCameraDialog.dismiss();
                if (ContextCompat.checkSelfPermission(AddActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });

        takeCode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO 扫码
                mCameraDialog.dismiss();
                Intent intent = new Intent(AddActivity.this, ZxingActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
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

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    private void initTimePicker() {
        dateOptions = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                objectDate.setText(getTime(date));
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        objectDate.setText(getTime(date));
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true)
                .setItemVisibleCount(5)
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .setCancelText("取消")
                .setTitleText("生产日期")
                .setSubmitText("确定")
                .build();

        Dialog mDialog = dateOptions.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            dateOptions.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                //dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }

    private String getTime(Date date) {
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private void initDateData()
    {
        ArrayList<String> optionsForDays = new ArrayList<>();
        ArrayList<String> optionsForMonths = new ArrayList<>();
        ArrayList<String> optionsForYears = new ArrayList<>();
        for(int i = 1; i <= 30; i ++)
        {
            optionsForDays.add(i+"");
        }
        for(int i = 1; i <= 12; i ++)
        {
            optionsForMonths.add(i+"");
        }
        for(int i = 1; i <= 10; i ++)
        {
            optionsForYears.add(i+"");
        }
        options1Items.add("日");
        options1Items.add("月");
        options1Items.add("年");
        options2Items.add(optionsForDays);
        options2Items.add(optionsForMonths);
        options2Items.add(optionsForYears);
    }

    private void initTypeData()
    {
        optionsForType.add("食物");
        optionsForType.add("服装");
        optionsForType.add("书籍");
        optionsForType.add("文件");
        //TODO 传入分类数据
    }

    private void startCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 指定图片的输出地址为imageUri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }
}
