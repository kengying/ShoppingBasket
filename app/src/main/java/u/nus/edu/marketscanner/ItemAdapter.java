package u.nus.edu.marketscanner;

/**
 * Created by ithsirslawragga on 3/7/17.
 */

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ItemAdapter extends ArrayAdapter<Item> {
    private List<Item> list = new ArrayList<Item>();
    Context context;

    public ItemAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);
        this.context = context;
        this.list = items;
        Log.d("NEXT", list.toString());
    }

    public void add(Item object) {
        // TODO Auto-generated method stub
        list.add(object);
        super.add(object);
    }

    public void remove(List<Item> items, int position){
        this.list = items;
        list.remove(position);
    }

    static class ImgHolder {
        ImageView IMG;
        TextView NAME;
        TextView PRICE;
    }

    //        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return this.list.size();
//        }
//        @Override
//        public Object getItem(int position) {
//            // TODO Auto-generated method stub
//            return this.list.get(position);
//        }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        Log.d("NEXT HERE MAYBE", position + "");
        View row;
        row = convertView;
        ImgHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_layout, null);
            //LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //row = inflater.inflate(R.layout.row_layout,parent,false);
//                holder = new ImgHolder();
//
//                holder.IMG = (ImageView) convertView.findViewById(R.id.item_img);
//                holder.NAME = (TextView) convertView.findViewById(R.id.item_name);
//                holder.PRICE = (TextView) convertView.findViewById(R.id.item_price);
//                convertView.setTag(holder);
        }
//            else
//            {
//                holder = (ImgHolder) convertView.getTag();
//
//            }
//
//            Item item = getItem(position);
//            holder.IMG.setImageResource(item.getItem_Image_int());
//            holder.NAME.setText(item.getItem_Name());
//            holder.PRICE.setText(item.getItem_Price_String());

        Item item = getItem(position);
        Log.d("NEXT HERE MAYBE", list.get(0).toString());
        ImageView image = (ImageView) convertView.findViewById(R.id.item_img);
        TextView name = (TextView) convertView.findViewById(R.id.item_name);
        TextView price = (TextView) convertView.findViewById(R.id.item_price);

        name.setText(item.getItem_Name());
        price.setText("$"+item.getItem_Price());
        Picasso.with(context)
                .load(item.getItem_Image())
                .into(image);

        return convertView;
    }

}
