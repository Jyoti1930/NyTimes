package sample.nytimes.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PopularArticleModel> mPopularArticleModelArrayList;

    public ListAdapter(Context context, ArrayList<PopularArticleModel> popularArticleModelArrayList) {

        this.context = context;
        this.mPopularArticleModelArrayList = popularArticleModelArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return mPopularArticleModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPopularArticleModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.popular_articles_listitems, null, true);

            holder.imgarticle = (ImageView) convertView.findViewById(R.id.articleimg);
            holder.tvtitle = (TextView) convertView.findViewById(R.id.title);
            holder.tvbyline = (TextView) convertView.findViewById(R.id.byline);
            holder.tvpublisheddate = (TextView) convertView.findViewById(R.id.publisheddate);

            convertView.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load(mPopularArticleModelArrayList.get(position).getImgURL()).into(holder.imgarticle);
        holder.tvtitle.setText(mPopularArticleModelArrayList.get(position).getTitle());
        holder.tvbyline.setText(mPopularArticleModelArrayList.get(position).getByline());
        holder.tvpublisheddate.setText(mPopularArticleModelArrayList.get(position).getPublisheddate());

        if ((mPopularArticleModelArrayList.get(position).getByline().toString().length() > 50)) {
            holder.tvpublisheddate.setPadding(0, 115, 0, 0);
        } else {
            holder.tvpublisheddate.setPadding(0, 60, 0, 0);
        }

        return convertView;
    }

    private class ViewHolder {

        protected TextView tvtitle, tvbyline, tvpublisheddate;
        protected ImageView imgarticle;
    }

}
