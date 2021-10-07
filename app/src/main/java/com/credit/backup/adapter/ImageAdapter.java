package com.credit.backup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.credit.backup.R;
import com.credit.backup.model.ImageHelperClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<ImageHelperClass> {
    private Context mContext;
    int mRresouurce;
    private ArrayList<ImageHelperClass> rentalProperties;

    public ImageAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ImageHelperClass> objects) {
        super(context, resource, objects);
        mContext=context;
        mRresouurce=resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String id,name,image,comment,publishedAt;
        name=getItem(position).getName();
        id=getItem(position).getId();
        image=getItem(position).getImage();
        comment=getItem(position).getComment();
        publishedAt=getItem(position).getPublishedAt();


        ImageHelperClass helperClass=new ImageHelperClass(id,name,image,comment,publishedAt);
        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView=inflater.inflate(mRresouurce,parent,false);
        TextView name_tv = (TextView) convertView.findViewById(R.id.user_name);
        TextView id_tv=(TextView) convertView.findViewById(R.id.user_id);
        ImageView app_install_icon=(ImageView) convertView.findViewById(R.id.image);
        Picasso.get().load(image).into(app_install_icon);

        name_tv.setText(name);
        id_tv.setText(publishedAt);

        return convertView;
    }
}
