package com.zczczy.leo.microwarehouse.views;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.CategoryActivity_;
import com.zczczy.leo.microwarehouse.activities.ClassifyActivity_;
import com.zczczy.leo.microwarehouse.activities.CommonWebViewActivity_;
import com.zczczy.leo.microwarehouse.activities.GoodsDetailActivity_;
import com.zczczy.leo.microwarehouse.activities.SearchResultActivity_;
import com.zczczy.leo.microwarehouse.activities.TestActivity_;
import com.zczczy.leo.microwarehouse.model.AdvertModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.ViewById;
import org.springframework.util.StringUtils;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by zczczy on 2016/10/14.
 */

public class WelfareAdapter extends PagerAdapter {

    private Context mContext;
    private List<AdvertModel> dataList;

    public WelfareAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addDatas(List<AdvertModel> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        return dataList != null && dataList.size() > 0 ? dataList.size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (dataList == null) {
            return null;
        }
        final AdvertModel data = dataList.get(position);
        ViewHolder viewHolder = null;
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.item_banner_vp, null);
        if (viewHolder == null) {
            viewHolder = new ViewHolder(view);
        }
        if (!StringUtils.isEmpty(data.AdvertImg)){
            Glide.with(mContext)
                    .load(data.AdvertImg)
                    .skipMemoryCache(true)
                    .crossFade()
                    .placeholder(R.drawable.goods_default)
                    .error(R.drawable.goods_default)
                    .into(viewHolder.image);
        }
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转标识(1:商品类别页，2：商品明细 3:牙医预约 4：牙医web页)
                if (1==data.JumpType) {
                    // CategoryActivity_.intent(getActivity()).id(temp[1]).title(temp[2]).start();
                    ClassifyActivity_.intent(mContext).id(data.InfoId).start();
                }
                else  if(2==data.JumpType){
                    GoodsDetailActivity_.intent(mContext).goodsId(data.InfoId).start();
                }
                else if(3==data.JumpType){
                    SearchResultActivity_.intent(mContext).IsAppointmentPro("1").searchContent("").start();
                }
                else {
                    CommonWebViewActivity_.intent(mContext).linkUrl(data.InfoId).title(data.AdvertName).start();

                }

            }
        });

        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        return view;
    }




    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private class ViewHolder {
        private ImageView image;

        ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.image);
        }
    }
}
