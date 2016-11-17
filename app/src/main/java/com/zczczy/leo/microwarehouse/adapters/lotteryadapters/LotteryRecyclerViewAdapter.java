package com.zczczy.leo.microwarehouse.adapters.lotteryadapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.MyApplication;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.items.BaseViewHolder;
import com.zczczy.leo.microwarehouse.items.ItemView;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zczczy on 2016/11/10.
 */
@EBean
public abstract class LotteryRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private List<T> items = new ArrayList<>();
    private BaseRecyclerViewAdapter.OnItemClickListener<T> onItemClickListener;
    private BaseRecyclerViewAdapter.OnItemLongClickListener<T> onItemLongClickListener;
    public BaseRecyclerViewAdapter.VerticalAndHorizontal verticalAndHorizontal;
    private BaseRecyclerViewAdapter.DynamicHeight dynamicHeight;
    public boolean isFooter;

    @RestService
    MyRestClient myRestClient;

    @App
    MyApplication app;

    @Pref
    MyPrefs_ pre;

    @StringRes
    String no_net;

    @Bean
    MyErrorHandler myErrorHandler;

    @AfterInject
    void afterBaseInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @RootContext
    Context context;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == BaseRecyclerViewAdapter.VIEW_TYPES.FOOTER) {
            view = createFooterView(parent, viewType);
        } else {
            view = onCreateItemView(parent, viewType);
        }
        if (isFooter) {
            //修正 item不充满
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
        }
        return new BaseViewHolder(view);
    }

    public int getAdapterItemCount() {
        return items.size();
    }

    @Background
    public abstract void getMoreData(Object... objects);

    @UiThread
    protected void afterGetMoreData(BaseModelJson<List<T>> result) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            result = new BaseModelJson<>();
//            AndroidTool.showToast(context, no_net);
        } else if (result.Successful) {
            clear();
            if (result.Data != null &&result.Data.size() > 0) {
                insertAll(result.Data, getItemCount());
            }
        }
    }


    /**
     * 设置 ItemView
     *
     * @param parent
     * @return
     */
    protected abstract View onCreateItemView(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, int position) {

        if (getItemViewType(position) == BaseRecyclerViewAdapter.VIEW_TYPES.NORMAL) {
            ItemView<T> itemView = (ItemView) viewHolder.itemView;
            itemView.init(items.get(position), this, viewHolder);
            setNormalClick(viewHolder);
            if (dynamicHeight != null) {
//            int cellWidth = viewHolder.itemView.getWidth();// this will give you cell width dynamically
//            int cellHeight = viewHolder.itemView.height;// this will give you cell height dynamically
                dynamicHeight.HeightChange(position, 55); //call your iterface hear
            }
        } else if (getItemViewType(position) == BaseRecyclerViewAdapter.VIEW_TYPES.FOOTER) {
            ItemView<T> itemView = (ItemView) viewHolder.itemView;
            itemView.init(null, this, viewHolder);
        }

    }

    public View createFooterView(ViewGroup parent, int viewType) {
        return null;
    }

    /**
     * 设置普通itemclick事件
     *
     * @param viewHolder
     */
    private void setNormalClick(final BaseViewHolder viewHolder) {
        if (onItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(viewHolder, items.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
                }
            });
        }
        if (onItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(viewHolder, items.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public void insertAll(List<T> list, int position) {
        items.addAll(isFooter ? position++ : position, list);
        notifyItemInserted(position);
    }

    public void itemNotify(Object... objects) {

    }

    /**
     * Clear the list of the adapter
     */
    public void clear() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void insertData(T t, int position) {
        items.add(position, t);
        notifyItemInserted(position);
    }

    public void deleteItem(T t, int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void deleteItemRange(List<T> t) {
        items.removeAll(t);
        notifyDataSetChanged();
    }


    public T getItemData(int position) {
        return items.size() < position + 1 ? null : items.get(position);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (isFooter)
            count++;
        return getAdapterItemCount() + count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && isFooter) {
            return BaseRecyclerViewAdapter.VIEW_TYPES.FOOTER;
        }
        return BaseRecyclerViewAdapter.VIEW_TYPES.NORMAL;
    }


    /**
     * @param onItemClickListener
     */
    public void setOnItemClickListener(BaseRecyclerViewAdapter.OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T> {

        void onItemClick(RecyclerView.ViewHolder viewHolder, T obj, int position);


    }

    public void setOnItemLongClickListener(BaseRecyclerViewAdapter.OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemLongClickListener<T> {

        void onItemLongClick(RecyclerView.ViewHolder viewHolder, T obj, int position);
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public enum VerticalAndHorizontal {
        Vertical,
        Horizontal
    }

    public interface DynamicHeight {
        void HeightChange(int position, int height);
    }

    public BaseRecyclerViewAdapter.DynamicHeight getDynamicHeight() {
        return dynamicHeight;
    }

    public void setDynamicHeight(BaseRecyclerViewAdapter.DynamicHeight dynamicHeight) {
        this.dynamicHeight = dynamicHeight;
    }

    public class VIEW_TYPES {
        public static final int NORMAL = 0;
        public static final int HEADER = 1;
        public static final int FOOTER = 2;
    }

}
