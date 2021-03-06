package com.example.julisarrelli.mcdonaldstest.JavaClases.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.julisarrelli.mcdonaldstest.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by julisarrelli on 1/12/17.
 */
public class LocalsListViewAdapter extends BaseAdapter {
    // Declare Variables
    Context context;
    //int[] imagenes;
    ArrayList<String> adresses;
    ArrayList<String> cities;
    LayoutInflater inflater;

    public LocalsListViewAdapter(Context context, ArrayList<String> adresses, ArrayList<String>cities ) {
        this.context = context;
      //  this.imagenes = imagenes;
      this.adresses=adresses;
      this.cities=cities;
    }

    @Override
    public int getCount() {
        return adresses.size();
    }

    @Override
    public HashMap<String,String> getItem(int position) {

        String adress=adresses.get(position);
        String city=cities.get(position);

        HashMap<String,String>answer=new HashMap<>();
        answer.put("adress",adress);
        answer.put("city",city);


        return answer;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        ImageView imgImg;
        TextView txtTitle;
        TextView txtContenido;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.locals_listrow, parent, false);

        // Locate the TextViews in listview_item.xml
       //imgImg = (ImageView) itemView.findViewById(R.id.imageView);
        txtTitle = (TextView) itemView.findViewById(R.id.list_adress);
        txtContenido = (TextView) itemView.findViewById(R.id.list_city);

        // Capture position and set to the TextViews

        txtTitle.setText(adresses.get(position));
        txtContenido.setText(cities.get(position));

        return itemView;
    }
}