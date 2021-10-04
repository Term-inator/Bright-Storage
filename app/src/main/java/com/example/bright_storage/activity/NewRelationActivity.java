package com.example.bright_storage.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.example.bright_storage.R;
import com.example.bright_storage.model.dto.RelationDTO;
import com.example.bright_storage.service.RelationService;
import com.example.bright_storage.service.impl.RelationServiceImpl;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.INVISIBLE;

public class NewRelationActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO = 2, CHOOSE_PHOTO = 3, REQUEST_CODE = 5;
    private static final String TAG = "NewRelationActivity";
    private Button title_back;
    private Button title_search;
    private TextView theTitle;
    private Button submit;
    private EditText relationName;
    private ImageButton photoButton;
    private Button choosePhoto, takePhoto, cancel;
    private Uri imageUri;
    private RelationService relationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case TAKE_PHOTO:
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    photoButton.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
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
                        Toast.makeText(NewRelationActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        imageUri = data.getData();

        if (DocumentsContract.isDocumentUri(this, imageUri)) {
            String docId = DocumentsContract.getDocumentId(imageUri);
            if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            imagePath = getImagePath(imageUri, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            imagePath = imageUri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片
    }

    private String getImagePath(Uri externalContentUri, String selection) {
        String path = null;
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
        imageUri = data.getData();
        String imagePath = getImagePath(imageUri, null);
        displayImage(imagePath);
    }

    private void init() {
        relationService = new RelationServiceImpl();
        getSupportActionBar().hide();
        setContentView(R.layout.new_relation);
        theTitle = findViewById(R.id.title_text);
        theTitle.setText("新建关系");
        title_back = (Button) findViewById(R.id.title_back);
        title_search = (Button) findViewById(R.id.title_search);
        title_search.setBackgroundResource(R.drawable.saoma);
        relationName = (EditText) findViewById(R.id.relation_name);
        submit = (Button) findViewById(R.id.relation_submit);
        photoButton = (ImageButton) findViewById(R.id.photoButton);
    }

    private void setListeners() {
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCameraDialog();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRelation();
            }
        });
        title_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewRelationActivity.this, ZxingActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void showCameraDialog()
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
        cancel = (Button) root.findViewById(R.id.cancel);
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
                    imageUri = FileProvider.getUriForFile(NewRelationActivity.this, "com.example.bright_storage.camera.file_provider", outputImage);
                } else {
                    // 将File对象转换为Uri对象，这个Uri标识着output_image.jpg这张图片的本地真实路径
                    imageUri = Uri.fromFile(outputImage);
                }

                // 动态申请权限
                if (ContextCompat.checkSelfPermission(NewRelationActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) NewRelationActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
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
                if (ContextCompat.checkSelfPermission(NewRelationActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewRelationActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
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
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    private void createRelation() {
        RelationDTO relationDTO = new RelationDTO();
        relationDTO.setName(relationName.getText().toString());
        if(imageUri != null)
            relationDTO.setAvatar(imageUri.toString());
        //relationDTO.setOwner();
        relationService.createRelation(relationDTO);
        finish();
    }

}
