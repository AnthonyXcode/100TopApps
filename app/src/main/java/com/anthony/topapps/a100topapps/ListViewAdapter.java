package com.anthony.topapps.a100topapps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by anthony on 8/12/2016.
 */

public class ListViewAdapter extends BaseAdapter {
    private String TAG = "ListViewAdapter";
    private ArrayList<Item> items;
    private LayoutInflater layoutInflater;

    public ListViewAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        items = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.layout_item, null);
            holder = new Holder();
            initLayout(holder, convertView);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        setupLayout(holder, items.get(position), position);
        return convertView;
    }

    public void addAllItems(ArrayList<Item> allItems){
        items.addAll(allItems);
        notifyDataSetChanged();
    }

    private void setupLayout(Holder holder, Item item, int position){
        holder.typeTxt.setText(item.getType());
        holder.nameTxt.setText(item.getName());
        holder.numberTxt.setText(String.valueOf(item.getNumber()));
        DownloadUserImageTask downloadUserImageTask = new DownloadUserImageTask(holder.iconImg, 150, 150);
        downloadUserImageTask.execute(item.getIconLink());
    }

    private void initLayout(Holder holder, View view){
        holder.typeTxt = (TextView)view.findViewById(R.id.typeTxt);
        holder.nameTxt = (TextView)view.findViewById(R.id.nameTxt);
        holder.numberTxt = (TextView)view.findViewById(R.id.numberTxt);
        holder.iconImg = (ImageView) view.findViewById(R.id.iconImg);
    }

    private class Holder{
        TextView typeTxt;
        TextView nameTxt;
        TextView numberTxt;
        ImageView iconImg;
    }

    public class DownloadUserImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;
        int w;
        int h;
        public DownloadUserImageTask(ImageView imageView, int w, int h) {
            this.imageView = imageView;
            this.w = w;
            this.h = h;
        }

        @Override
        protected void onPreExecute() {
            imageView.setImageBitmap(null);
            super.onPreExecute();
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap shopImg = null;
            try {
                InputStream inputStream = new URL(url).openStream();
                Log.i(TAG, "doInBackground: " + params[0]);
                shopImg = ImageCompressorByBitmap(BitmapFactory.decodeStream(inputStream), w, h);
            } catch (IOException e) {
                Log.i(TAG, "doInBackground: "+ e.getMessage());
                e.printStackTrace();
            }
            return shopImg;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
        }

        public Bitmap ImageCompressorByBitmap(Bitmap imageBitmap, int maxW, int maxH){
            Bitmap bitmap;
            if (imageBitmap == null) return null;
            int inW = imageBitmap.getWidth();
            int inH = imageBitmap.getHeight();
            int xRatio = inW/maxW;
            int yRatio = inH/maxH;

            if (xRatio > 1 || yRatio > 1){
                if (xRatio > yRatio){
                    bitmap = Bitmap.createScaledBitmap(imageBitmap, inW/xRatio, inH/xRatio, false);
                }else {
                    bitmap = Bitmap.createScaledBitmap(imageBitmap, inW/yRatio, inH/yRatio, false);
                }
            }else {
                bitmap = imageBitmap;
            }

            return bitmap;
        }
    }
}
