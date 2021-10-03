package com.example.bright_storage.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bright_storage.R;
import com.example.bright_storage.model.dto.RelationDTO;
import com.example.bright_storage.model.param.LoginParam;
import com.example.bright_storage.model.support.BaseResponse;
import com.example.bright_storage.model.vo.UserVO;
import com.example.bright_storage.service.impl.RelationServiceImpl;
import com.example.bright_storage.service.impl.UserServiceImpl;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RelationMemberActivity extends AppCompatActivity {
    private ArrayList<UserVO> members = new ArrayList<>();
    private RelationServiceImpl relationService = new RelationServiceImpl();
    Button title_back;
    TextView title_text;
    Button title_exit;

    // tmp
    public void loginPassword(){
        LoginParam loginParam = new LoginParam();
        loginParam.setPhone("15822222222");
        loginParam.setPassword("ab123456");
        UserServiceImpl userService = new UserServiceImpl();
        BaseResponse<?> response = userService.loginPassword(loginParam);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_relation_member);

        title_back = (Button) findViewById(R.id.title_back);
        title_exit = (Button) findViewById(R.id.title_search);
        title_exit.setBackgroundResource(R.mipmap.exit);
        title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("关系成员");

        title_back.setOnClickListener(v -> finish());

        title_exit.setOnClickListener(v -> {
            MessageDialog.show((AppCompatActivity) v.getContext(), "退出关系", "确定要退出" + "{关系名}" + "吗？", "确定", "取消")
                    .setOkButton("确定", new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            // TODO 退出关系
                            return true;
                        }
                    });
        });

        loginPassword();

        RelationDTO relation = relationService.getRelationById(1L);
        UserVO relation_owner = relation.getOwner();

        members.add(relation_owner);
        members.addAll(relationService.listMembersByRelationId(1L));

        for(UserVO u: members) {
            u.setAvatar("https://img.fulaishiji.com/images/goods/19883/big/03957c4d-6869-4cef-ad3e-824852f9da2b_800x800.png");
        }

        RelationMemberAdapter adapter = new RelationMemberAdapter(members);
        RecyclerView rv = (RecyclerView) this.findViewById(R.id.relation_member_rv);
        rv.setAdapter(adapter);
        rv.addItemDecoration(getItemDecoration());
    }

    private RecyclerView.ItemDecoration getItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            private Paint paint = new Paint();

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                if(parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1) {
                    outRect.top = 80;
                }
            }

            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int count = parent.getChildCount();
                paint.setColor(Color.GRAY);
                paint.setTextSize(50);

                View creator = parent.getChildAt(0);
                int top = creator.getTop();
                int bottom = creator.getBottom();
                int left = creator.getLeft();
                int right = creator.getRight();
                c.drawText("  创建者", left, top - 20, paint);

                if(count > 1) {
                    View member = parent.getChildAt(1);
                    top = member.getTop();
                    bottom = member.getBottom();
                    left = member.getLeft();
                    right = member.getRight();
                    c.drawText("  成员", left, top - 20, paint);
                }
            }
        };
    }
}

class RelationMemberAdapter extends RecyclerView.Adapter<RelationMemberAdapter.MyViewHolder> {
    private ArrayList<UserVO> members;

    public RelationMemberAdapter(ArrayList<UserVO> members) {
        this.members = members;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelationMemberAdapter.MyViewHolder holder = new RelationMemberAdapter.MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.relation_member, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(this.members.get(position).getNickname());
        Picasso.get()
                .load(members.get(position).getAvatar())
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(holder.avatar);
        if(position == 0) {
            holder.kick.setVisibility(View.GONE);
        }
        else {
            holder.kick.setOnClickListener(v -> {
                MessageDialog.show((AppCompatActivity) v.getContext(), "删除", "确定要移除成员" + holder.name.getText() + "吗？", "确定", "取消")
                        .setOkButton("确定", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                // TODO 移除成员
                                return true;
                            }
                        });
            });
        }
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView name;
        ImageView kick;

        public MyViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.member_avatar);
            name = (TextView) itemView.findViewById(R.id.member_name);
            kick = (ImageView) itemView.findViewById(R.id.kick_member);
        }
    }
}
