package com.luleo.baselibrary.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.luleo.baselibrary.items.BaseViewHolder;
import com.luleo.baselibrary.items.ItemView;
import com.luleo.baselibrary.model.BaseModelJson;
import com.luleo.baselibrary.rest.MyErrorHandler;
import com.luleo.baselibrary.tools.AndroidTool;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.res.StringRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2016/5/3.
 */
@EBean
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private List<T> items = new ArrayList<>();

    private OnItemClickListener<T> onItemClickListener;

    private OnItemLongClickListener<T> onItemLongClickListener;

    public VerticalAndHorizontal verticalAndHorizontal;

    private DynamicHeight dynamicHeight;

    @StringRes
    String no_net;

    @Bean
    MyErrorHandler myErrorHandler;


    @RootContext
    Context context;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = onCreateItemView(parent, viewType);
        //修正 item不充满
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        return new BaseViewHolder(view);
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
            if (result.Data.size() > 0) {
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
        ItemView<T> itemView = (ItemView) viewHolder.itemView;
        itemView.init(items.get(position), this, viewHolder);
        setNormalClick(viewHolder);
        if (dynamicHeight != null) {
//            int cellWidth = viewHolder.itemView.getWidth();// this will give you cell width dynamically
//            int cellHeight = viewHolder.itemView.height;// this will give you cell height dynamically
            dynamicHeight.HeightChange(position, 55); //call your iterface hear
        }
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
        items.addAll(position, list);
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
//        notifyItemRangeRemoved();
        notifyDataSetChanged();
    }


    public T getItemData(int position) {
        return items.size() < position + 1 ? null : items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    /**
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T> {

        void onItemClick(RecyclerView.ViewHolder viewHolder, T obj, int position);

    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
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

    public DynamicHeight getDynamicHeight() {
        return dynamicHeight;
    }

    public void setDynamicHeight(DynamicHeight dynamicHeight) {
        this.dynamicHeight = dynamicHeight;
    }
}
