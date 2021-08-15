package com.example.bright_storage.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bright_storage.R;
import com.example.bright_storage.activity.BSProActivity;
import com.example.bright_storage.activity.RecycleBinActivity;
import com.example.bright_storage.activity.RelationMemberActivity;
import com.example.bright_storage.activity.SettingActivity;

public class PersonalInfoFragment extends Fragment {

    private PersonalInfoViewModel PersonalInfoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PersonalInfoViewModel =
                new ViewModelProvider(this).get(PersonalInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_personal_info, container, false);
        /*final TextView textView = root.findViewById(R.id.text_personal_info);
        PersonalInfoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        final TextView textView = root.findViewById(R.id.user_name);
        Button buttonBSPro = (Button) root.findViewById(R.id.BSPro);
        Button buttonRelation = (Button) root.findViewById(R.id.Relation);
        Button buttonSettings = (Button) root.findViewById(R.id.Settings);
        Button buttonRecycleBin = (Button) root.findViewById(R.id.RecycleBin);
        buttonBSPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), BSProActivity.class);
                startActivity(intent);
            }
        });
        buttonRelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(root.getContext(), RelationActivity.class);
                Intent intent = new Intent(root.getContext(), RelationMemberActivity.class);
                startActivity(intent);
            }
        });
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });
        buttonRecycleBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), RecycleBinActivity.class);
                startActivity(intent);
            }
        });
        //RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_personal);
        return root;
    }
}