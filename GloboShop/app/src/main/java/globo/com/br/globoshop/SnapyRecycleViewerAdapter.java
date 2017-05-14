package globo.com.br.globoshop;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Carlos Daniel Drury on 5/14/2017.
 */

public class SnapyRecycleViewerAdapter extends RecyclerView.Adapter<SnapyRecycleViewerAdapter.CustomViewHolder> {

    private TextView textView;
    private ImageView imageView;
    private Context context; //context
    private ArrayList<Product> products; //data source of the list adapter

    public SnapyRecycleViewerAdapter(ArrayList<Product> products, Context context) {
        super();
        this.products = products;
        this.context = context;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_recycleview, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Product product = products.get(position);
    }


    @Override
    public int getItemCount() {
        return products.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.item_image);
            this.textView = (TextView) view.findViewById(R.id.item_textview);
        }
    }


}
