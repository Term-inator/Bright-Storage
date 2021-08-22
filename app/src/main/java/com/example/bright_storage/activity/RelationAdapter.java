package com.example.bright_storage.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bright_storage.R;
import com.example.bright_storage.model.dto.RelationDTO;
import com.example.bright_storage.model.entity.Relation;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.ui.home.HomeAdapter;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RelationAdapter extends RecyclerView.Adapter<RelationAdapter.MyViewHolder>{
    private Context context;
    private List<RelationDTO> list;
    private RelationAdapter.layoutInterface layoutInterface;
    public RelationAdapter(Context context, List<RelationDTO> list) {
        this.context = context;
        this.list = list;
    }
    /**
     * 按钮点击事件需要的方法
     */
    public void layoutSetOnclick(RelationAdapter.layoutInterface layoutInterface) {
        this.layoutInterface = layoutInterface;
    }

//    public void layoutSetOnLongClick(layoutInterface layoutInterface) {
//        this.layoutInterface = layoutInterface;
//    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface layoutInterface {
        public void onclick(View view, RelationDTO TstorageUnit);
//        public void onLongClick(View view, RelationDTO TstorageUnit);
    }

    @Override
    public RelationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelationAdapter.MyViewHolder holder = new RelationAdapter.MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.storage_unit, parent,
                false));
        //holder.deleteCheckBox.setVisibility(View.VISIBLE);
        return holder;
    }

    @Override
    public void onBindViewHolder(RelationAdapter.MyViewHolder holder, final int position) {
        holder.tv.setText(list.get(position).getName());
        ViewGroup.LayoutParams lp;
        lp= holder.id_clicklayout.getLayoutParams();
        lp.height= MainActivity.width/2;
        holder.id_clicklayout.setLayoutParams(lp);
//        if (list.get(position).getImage().length() == 0)
//        {
            Picasso.get()
                    .load(R.mipmap.ic_launcher)
                    .centerCrop()
                    .fit()
                    .into(holder.th);
//        }
//        else {
//            Picasso.get()
//                    .load(list.get(position).getImage())//"https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3155097781,4164517483&fm=26&gp=0.jpg"
//                    .centerCrop()
//                    .error(R.mipmap.ic_launcher)
//                    .fit()
//                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//                    .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
//                    .into(holder.th);
//        }

//        holder.id_clicklayout.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (layoutInterface != null) {
////                  接口实例化后的而对象，调用重写后的方法
//                    layoutInterface.onLongClick(v, list.get(position));
//                }
//                return true;
//            }
//        });

        holder.id_clicklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutInterface != null) {
//                  接口实例化后的而对象，调用重写后的方法
                    layoutInterface.onclick(v, list.get(position));
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView th;
        public View id_clicklayout;
        public CheckBox deleteCheckBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            th = (ImageView) itemView.findViewById(R.id.id_picture);
            tv = (TextView) itemView.findViewById(R.id.id_name);
            id_clicklayout = (View) itemView.findViewById(R.id.id_clicklayout);
            deleteCheckBox = (CheckBox) itemView.findViewById(R.id.delete_checkbox);

        }


    }
}
