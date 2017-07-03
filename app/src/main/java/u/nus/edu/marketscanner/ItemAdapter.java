package u.nus.edu.marketscanner;

/**
 * Created by ithsirslawragga on 3/7/17.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

    public class ItemAdapter extends ArrayAdapter{
        private List list= new ArrayList();

        public ItemAdapter(Context context, int resource) {
            super(context, resource);
            // TODO Auto-generated constructor stub
        }
        public void add(Item object) {
            // TODO Auto-generated method stub
            list.add(object);
            super.add(object);
        }
        static class ImgHolder
        {
            ImageView IMG;
            TextView NAME;
            TextView PRICE;
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return this.list.size();
        }
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return this.list.get(position);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View row;
            row = convertView;
            ImgHolder holder;
            if(convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.row_layout,parent,false);
                holder = new ImgHolder();

                holder.IMG = (ImageView) row.findViewById(R.id.item_img);
                holder.NAME = (TextView) row.findViewById(R.id.item_name);
                holder.PRICE = (TextView) row.findViewById(R.id.item_price);
                row.setTag(holder);
            }
            else
            {
                holder = (ImgHolder) row.getTag();

            }

            Item item = (Item) getItem(position);
            holder.IMG.setImageResource(item.getItem_Image_int());
            holder.NAME.setText(item.getItem_Name());
            holder.PRICE.setText(item.getItem_Price_String());
            return row;
        }

    }
