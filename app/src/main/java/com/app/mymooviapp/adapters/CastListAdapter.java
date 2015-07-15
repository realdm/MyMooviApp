package com.app.mymooviapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mymooviapp.R;
import com.app.mymooviapp.models.CastMember;
import com.app.mymooviapp.utils.CircleTransform;
import com.app.mymooviapp.utils.Constants;
import com.app.mymooviapp.utils.FontChanger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mac on 6/20/2015.
 */
public class CastListAdapter extends BaseAdapter {

    private List<CastMember> cast = new ArrayList<CastMember>();

    private Context mContext;

    public CastListAdapter(Context mContext){

        this.mContext = mContext;
    }

    public void setData(List<CastMember> newCast){

        this.cast = newCast;
    }
    @Override
    public int getCount() {

        return cast.isEmpty() ? 0 : cast.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        CastViewHolder holder;

        if(view==null)
        {
            view = inflater.inflate(R.layout.cast_row_item,viewGroup,false);

            holder = new CastViewHolder(view,mContext);

            view.setTag(holder);
        }
        else
        {
            holder =(CastViewHolder) view.getTag();
        }

        holder.actorName.setText(cast.get(i).getActorName());

        String characterAsLabel = mContext.getResources().getString(R.string.character_as_label);


        holder.characterName.setText(characterAsLabel+" "+cast.get(i).getCharacter());

        Picasso.with(mContext)
                .load(Constants.THUMB_URL_BASE_PATH+cast.get(i).getProfile_photo())
                .transform(new CircleTransform())
                .into(holder.castProfilePhoto);

        return view;
    }

    public class CastViewHolder{

        ImageView castProfilePhoto;

        TextView actorName;

        TextView characterName;

        public CastViewHolder(View view,Context context)
        {
            castProfilePhoto = (ImageView)view.findViewById(R.id.cast_profile);

            actorName = (TextView) view.findViewById(R.id.actor_name);

            characterName = (TextView)view.findViewById(R.id.actor_character);

            characterName.setTypeface(FontChanger.setRobotoLight(context));

        }


    }
}
