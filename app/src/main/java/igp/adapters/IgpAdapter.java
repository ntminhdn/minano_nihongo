package igp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.john.nguyen.minnanonihongovocabulary.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import igp.objects.App;

/**
 * Created by Administrator on 18/01/2017.
 */

public class IgpAdapter extends ArrayAdapter<App> {

    private Context mContext;
    private int mResource;
    private ArrayList<App> mList;

    public IgpAdapter(Context context, int resource, ArrayList<App> list) {
        super(context, resource, list);

        mContext = context;
        mResource = resource;
        mList = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView)convertView.findViewById(R.id.icon);
            viewHolder.tvName = (TextView)convertView.findViewById(R.id.tvName);
            viewHolder.tvDescription = (TextView)convertView.findViewById(R.id.tvDescription);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        App app = mList.get(position);

        Picasso.with(mContext)
                .load(app.getIcon())
                .error(R.drawable.error)
                .placeholder(R.drawable.loading)
                .into(viewHolder.icon);
        viewHolder.tvName.setText(app.getName());
        viewHolder.tvDescription.setText(app.getDecription());

        return convertView;
    }

    private class ViewHolder{
        public ImageView icon;
        public TextView tvName, tvDescription;
    }
}
