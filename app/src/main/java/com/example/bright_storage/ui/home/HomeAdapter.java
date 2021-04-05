package com.example.bright_storage.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bright_storage.activity.MainActivity;
import com.example.bright_storage.R;
import com.example.bright_storage.model.entity.StorageUnit;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private Context context;
    private List<StorageUnit> list;
    private layoutInterface layoutInterface;


    public HomeAdapter(Context context, List<StorageUnit> list) {
        this.context = context;
        this.list = list;
    }

    /**
     * 按钮点击事件需要的方法
     */
    public void layoutSetOnclick(layoutInterface layoutInterface) {
        this.layoutInterface = layoutInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface layoutInterface {
        public void onclick(View view, StorageUnit TstorageUnit);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.storage_unit, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv.setText(list.get(position).getName());
//        holder.th.setImageResource();
//        holder.th.setImageBitmap();
//        holder.id_clicklayout.setText(list.get(position));
//
        ViewGroup.LayoutParams lp;
        lp= holder.id_clicklayout.getLayoutParams();
        lp.height= MainActivity.width/2;
        holder.id_clicklayout.setLayoutParams(lp);
//        holder.id_clicklayout(com.example.roomtest3.MainActivity.width/2);
//        holder.id_clicklayout.setWidth(com.example.roomtest3.MainActivity.width/3);
        Picasso.get()
                .load(list.get(position).getImage())//"https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3155097781,4164517483&fm=26&gp=0.jpg"
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                .into(holder.th);
//        x.image().bind(holder.th,"https://c-ssl.duitang.com/uploads/item/202002/24/20200224003004_movto.thumb.1000_0.jpg");

//        try {
//            URL url = new URL("https://c-ssl.duitang.com/uploads/item/202002/24/20200224003004_movto.thumb.1000_0.jpg");
//            holder.th.setImageBitmap(BitmapFactory.decodeStream(url.openStream()));
//        } catch (Exception e) {
//
//        }

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
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView th;
        View id_clicklayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            th = (ImageView) itemView.findViewById(R.id.id_picture);
            tv = (TextView) itemView.findViewById(R.id.id_name);
            id_clicklayout = (View) itemView.findViewById(R.id.id_clicklayout);

        }


    }
}

