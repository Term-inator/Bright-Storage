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
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.bright_storage.R;
import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.repository.CategoryRepository;
import com.example.bright_storage.repository.StorageUnitRepository;
import com.example.bright_storage.service.StorageUnitService;
import com.example.bright_storage.service.impl.StorageUnitServiceImpl;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.view.View.INVISIBLE;

public class AddActivity extends AppCompatActivity
{
    @Inject
    StorageUnitService storageUnitService;
    private List<Category> allCategories;
    private Set<Category> categories;
    private Date overdueDate, productionDate;
    private int shelfLifeCount = 0;
    private String shelfLifeType = "";
    //private StorageUnitRepository storageUnitRepository;
    private StorageUnit storageUnit;
    private Uri imageUri;
    public static final int TAKE_PHOTO = 2, CHOOSE_PHOTO = 3, REQUEST_CODE = 5;
    private Button title_back, title_search;
    private Button choosePhoto, takePhoto, takeCode, cancel;
    private Switch isBox, isPrivate;
    private EditText objectName, objectCount, objectRemarks;
    private Button objectOverdue, objectType, objectDate, objectShelfLife, objectSubmit;
    private ImageButton photoButton;
    private TextView thetitle;
    private OptionsPickerView shelfLifeOptions, typeOptions;
    private TimePickerView dateOptions1, dateOptions2;
    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<String> optionsForType = new ArrayList<>();//类型列表
    boolean box = false, open = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageUnitService = new StorageUnitServiceImpl();
        ZXingLibrary.initDisplayOpinion(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add);
        initModule();
        initTypeData();
        initDateData();
        initOptionPicker();
        initTimePicker();
        setListener();
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
                        Toast.makeText(AddActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
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
                shelfLifeCount = Integer.parseInt(options2Items.get(options1).get(options2));
                shelfLifeType = options1Items.get(options1);
                String tx = shelfLifeCount + shelfLifeType;
                objectShelfLife.setText(tx);
                if(productionDate != null)
                {
                    Calendar temp = Calendar.getInstance();
                    temp.setTime(productionDate);
                    if(shelfLifeType.equals("年"))
                        temp.add(Calendar.YEAR, shelfLifeCount);
                    else if(shelfLifeType.equals("月"))
                        temp.add(Calendar.MONTH, shelfLifeCount);
                    else
                        temp.add(Calendar.DAY_OF_YEAR, shelfLifeCount);
                    overdueDate = temp.getTime();
                    objectOverdue.setText(getTime(overdueDate));
                }
                else if(overdueDate != null)
                {
                    Calendar temp = Calendar.getInstance();
                    temp.setTime(overdueDate);
                    if(shelfLifeType.equals("年"))
                        temp.add(Calendar.YEAR, -1*shelfLifeCount);
                    else if(shelfLifeType.equals("月"))
                        temp.add(Calendar.MONTH, -1*shelfLifeCount);
                    else
                        temp.add(Calendar.DAY_OF_YEAR, -1*shelfLifeCount);
                    productionDate = temp.getTime();
                    objectDate.setText(getTime(productionDate));
                }
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
                        shelfLifeCount = Integer.parseInt(options2Items.get(options1).get(options2));
                        shelfLifeType = options1Items.get(options1);
                        String tx = shelfLifeCount + shelfLifeType;
                        objectShelfLife.setText(tx);
                        if(productionDate != null)
                        {
                            Calendar temp = Calendar.getInstance();
                            temp.setTime(productionDate);
                            if(shelfLifeType.equals("年"))
                                temp.add(Calendar.YEAR, shelfLifeCount);
                            else if(shelfLifeType.equals("月"))
                                temp.add(Calendar.MONTH, shelfLifeCount);
                            else
                                temp.add(Calendar.DAY_OF_YEAR, shelfLifeCount);
                            overdueDate = temp.getTime();
                            objectOverdue.setText(getTime(overdueDate));
                        }
                        else if(overdueDate != null)
                        {
                            Calendar temp = Calendar.getInstance();
                            temp.setTime(overdueDate);
                            if(shelfLifeType.equals("年"))
                                temp.add(Calendar.YEAR, -1*shelfLifeCount);
                            else if(shelfLifeType.equals("月"))
                                temp.add(Calendar.MONTH, -1*shelfLifeCount);
                            else
                                temp.add(Calendar.DAY_OF_YEAR, -1*shelfLifeCount);
                            productionDate = temp.getTime();
                            objectDate.setText(getTime(productionDate));
                        }
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
        dateOptions1 = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                productionDate = date;
                objectDate.setText(getTime(productionDate));
                if(!shelfLifeType.equals(""))
                {
                    Calendar temp = Calendar.getInstance();
                    temp.setTime(productionDate);
                    if(shelfLifeType.equals("年"))
                        temp.add(Calendar.YEAR, shelfLifeCount);
                    else if(shelfLifeType.equals("月"))
                        temp.add(Calendar.MONTH, shelfLifeCount);
                    else
                        temp.add(Calendar.DAY_OF_YEAR, shelfLifeCount);
                    overdueDate = temp.getTime();
                    objectOverdue.setText(getTime(overdueDate));
                }
                //objectOverdue.setText(getTime(date));
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        productionDate = date;
                        objectDate.setText(getTime(productionDate));
                        if(!shelfLifeType.equals(""))
                        {
                            Calendar temp = Calendar.getInstance();
                            temp.setTime(productionDate);
                            if(shelfLifeType.equals("年"))
                                temp.add(Calendar.YEAR, shelfLifeCount);
                            else if(shelfLifeType.equals("月"))
                                temp.add(Calendar.MONTH, shelfLifeCount);
                            else
                                temp.add(Calendar.DAY_OF_YEAR, shelfLifeCount);
                            overdueDate = temp.getTime();
                            objectOverdue.setText(getTime(overdueDate));
                        }
                        //objectOverdue.setText(getTime(date));
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

        Dialog mDialog = dateOptions1.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            dateOptions1.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                //dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);
                dialogWindow.setDimAmount(0.3f);
            }
        }
        dateOptions2 = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //objectDate.setText(getTime(date));
                overdueDate = date;
                objectOverdue.setText(getTime(overdueDate));
                if(!shelfLifeType.equals(""))
                {
                    Calendar temp = Calendar.getInstance();
                    temp.setTime(overdueDate);
                    if(shelfLifeType.equals("年"))
                        temp.add(Calendar.YEAR, -1*shelfLifeCount);
                    else if(shelfLifeType.equals("月"))
                        temp.add(Calendar.MONTH, -1*shelfLifeCount);
                    else
                        temp.add(Calendar.DAY_OF_YEAR, -1*shelfLifeCount);
                    productionDate = temp.getTime();
                    objectDate.setText(getTime(productionDate));
                }
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        //objectDate.setText(getTime(date));
                        overdueDate = date;
                        objectOverdue.setText(getTime(overdueDate));
                        if(!shelfLifeType.equals(""))
                        {
                            Calendar temp = Calendar.getInstance();
                            temp.setTime(overdueDate);
                            if(shelfLifeType.equals("年"))
                                temp.add(Calendar.YEAR, -1*shelfLifeCount);
                            else if(shelfLifeType.equals("月"))
                                temp.add(Calendar.MONTH, -1*shelfLifeCount);
                            else
                                temp.add(Calendar.DAY_OF_YEAR, -1*shelfLifeCount);
                            productionDate = temp.getTime();
                            objectDate.setText(getTime(productionDate));
                        }
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true)
                .setItemVisibleCount(5)
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .setCancelText("取消")
                .setTitleText("过期日期")
                .setSubmitText("确定")
                .build();

        Dialog mDialog2 = dateOptions2.getDialog();
        if (mDialog2 != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            dateOptions1.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog2.getWindow();
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
        CategoryRepository categoryRepository = new CategoryRepository();
        allCategories = categoryRepository.findAll();
        for(Category it : allCategories)
        {
            optionsForType.add(it.getName());
        }
        //TODO 分为两类分类
    }

    private void startCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void initModule()
    {
        isBox = (Switch) findViewById(R.id.isBox);
        isPrivate = (Switch) findViewById(R.id.isPrivate);
        thetitle = (TextView) findViewById(R.id.title_text);
        title_back = (Button) findViewById(R.id.title_back);
        title_search = (Button) findViewById(R.id.title_search);
        photoButton = (ImageButton) findViewById(R.id.photoButton);
        objectName = (EditText) findViewById(R.id.object_name);
        objectCount = (EditText) findViewById(R.id.object_count);
        objectRemarks = (EditText) findViewById(R.id.object_remarks);
        objectOverdue = (Button) findViewById(R.id.object_overdue);
        objectType = (Button) findViewById(R.id.object_type);
        objectDate = (Button) findViewById(R.id.object_date);
        objectShelfLife = (Button) findViewById(R.id.object_shelflife);
        objectSubmit = (Button) findViewById(R.id.object_submit);
        thetitle.setText(R.string.title_add);
        title_search.setVisibility(INVISIBLE);
        objectCount.setInputType( InputType.TYPE_CLASS_NUMBER);
    }

    private void setListener()
    {
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
        objectOverdue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(AddActivity.this, PathSelectActivity.class);
                startActivityForResult(intent, SELECT_PATH);*/
                dateOptions2.show(v);
            }
        });
        objectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateOptions1.show(v);
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
        isBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == false) {
                    box = false;
                    objectCount.setText("");
                    objectCount.setVisibility(View.VISIBLE);
                    objectDate.setVisibility(View.VISIBLE);
                    objectShelfLife.setVisibility(View.VISIBLE);
                    objectOverdue.setVisibility(View.VISIBLE);
                    objectType.setVisibility(View.VISIBLE);
                }
                else{
                    box = true;
                    objectCount.setText("1");
                    objectCount.setVisibility(View.GONE);
                    objectDate.setVisibility(View.GONE);
                    objectShelfLife.setVisibility(View.GONE);
                    objectOverdue.setVisibility(View.GONE);
                    objectType.setVisibility(View.GONE);
                }
            }
        });
        isPrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == false)
                    open = true;
                else
                    open = false;

            }
        });
        objectSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hint = "请输入：";
                boolean legal = true;
                String name = objectName.getText().toString();
                int count = 0;
                String remarks = objectRemarks.getText().toString();
                // TODO 获取path
                Intent intent = getIntent();
                long path = 0;
                path = intent.getLongExtra("pid", 0);
                String overdue = objectOverdue.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date expireTime = null;
                try {
                    // 注意格式需要与上面一致，不然会出现异常
                    expireTime = sdf.parse(overdue);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String type = objectType.getText().toString();
                categories = new HashSet<Category>();
                for(Category it : allCategories) {
                    if(it.getName().equals(type)) {
                        categories.add(it);
                        break;
                    }
                }
                //String date = objectDate.getText().toString();
                //String shelfLife = objectShelfLife.getText().toString();
                if(name.length() == 0)
                {
                    legal = false;
                    hint += "名称 ";
                }
                if(objectCount.getText().toString().length() != 0) {
                    count = Integer.parseInt(objectCount.getText().toString());
                }
                else {
                    if(box == false) {
                        legal = false;
                        hint += "数量 ";
                    }
                }
                if(type.length() == 0 ) {
                    if(box == false) {
                        legal = false;
                        hint += "分类 ";
                    }
                }
                if(!legal) {
                    Toast.makeText(AddActivity.this, hint, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //TODO 提交数据
                    //暂时用来测试打开另一个页面
                    /*Intent intent = new Intent(AddActivity.this, ShowActivity.class);
                    startActivity(intent);*/
                    storageUnit = new StorageUnit();
                    storageUnit.setName(name);
                    storageUnit.setAccess(open);
                    storageUnit.setLocalParentId(path);
                    //System.out.println(categories.size());
                    storageUnit.setCategories(categories);
                    //System.out.println(storageUnit.getCategories().size());
                    storageUnit.setAmount(count);
                    if(imageUri != null)
                        storageUnit.setImage(imageUri.toString());
                    storageUnit.setExpireTime(expireTime);
                    storageUnit.setNote(remarks);
                    storageUnit.setDeleted(false);
                    int theBox = box ? 1 : 0;
                    storageUnit.setType(theBox);
                    storageUnitService.create(storageUnit);
//                    storageUnitRepository = new StorageUnitRepository();
//                    storageUnitRepository.save(storageUnit);
                    Intent intent1 = new Intent();
                    intent1.putExtra("Id",storageUnit.getId());
                    //HomeFragment.refresh();
                    finish();
                }
            }
        });
    }
}
