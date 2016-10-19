package com.zczczy.leo.microwarehouse.adapters;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.leo.lu.llrecyclerview.LLRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.MyApplication;
import com.zczczy.leo.microwarehouse.items.BaseViewHolder;
import com.zczczy.leo.microwarehouse.items.ItemView;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.PagerResult;
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
 * Created by leo on 2015/10/31.
 */
@EBean
public abstract class BaseUltimateRecyclerViewAdapter<T> extends LLRecyclerViewAdapter<BaseViewHolder> {
    private DynamicHeight dynamicHeight;
    public VerticalAndHorizontal verticalAndHorizontal;
    private List<T> items = new ArrayList<>();
    private int total = 0;
    private boolean isFirstOnly = true;
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mDuration = 300;
    private OnItemClickListener<T> onItemClickListener;
    private OnItemLongClickListener<T> onItemLongClickListener;
    private BindHeaderViewHolder bindHeaderViewHolder;

    @RestService
    MyRestClient myRestClient;

    @App
    MyApplication app;

    @Pref
    MyPrefs_ pre;

    @Bean
    OttoBus bus;

    @StringRes
    String no_net;

    @Bean
    MyErrorHandler myErrorHandler;

    boolean isRefresh;

    @RootContext
    Context context;

    @AfterInject
    void afterBaseInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }


    public int getAdapterItemCount() {

        return items.size();
    }

    /**
     * 获取更多的数据
     *
     * @param pageIndex
     * @param pageSize
     * @param objects
     */
    @Background
    public abstract void getMoreData(int pageIndex, int pageSize, boolean isRefresh, Object... objects);


    @UiThread
    protected void afterGetMoreData(BaseModelJson<PagerResult<T>> bmj) {
        AndroidTool.dismissLoadDialog();
        if (bmj == null) {
            bmj = new BaseModelJson<>();
        } else if (bmj.Successful) {
            if (isRefresh) {
                clear();
            }
            setTotal(bmj.Data.RowCount);
            if (bmj.Data.ListData.size() > 0) {
                insertAll(bmj.Data.ListData, getItems().size());
            }
        } else {
            AndroidTool.showToast(context, bmj.Error);
        }
        bus.post(bmj);
    }

    /**
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPES.NORMAL) {
            ItemView<T> itemView = (ItemView) viewHolder.itemView;
            itemView.init(items.get(customHeaderView != null ? position - 1 : position), this, viewHolder);
            setNormalClick(viewHolder);
        } else if (getItemViewType(position) == VIEW_TYPES.HEADER) {
            setHeaderClick(viewHolder);
            if (bindHeaderViewHolder != null) {
                bindHeaderViewHolder.onBindHeaderViewHolder(viewHolder);
            } else {
                onBindHeaderViewHolder(viewHolder);
            }

        } else if (getItemViewType(position) == VIEW_TYPES.FOOTER) {

        }
        if (!isFirstOnly) {
            for (Animator anim : getAdapterAnimations(viewHolder.itemView, AdapterAnimationType.ScaleIn)) {
                anim.setDuration(mDuration).start();
                anim.setInterpolator(mInterpolator);
            }
        } else {
//            ViewHelper.clear(viewHolder.itemView);
        }
        if (dynamicHeight != null && position == 0) {
            dynamicHeight.HeightChange(position, 55); //call your iterface hear
        }
    }

    abstract void onBindHeaderViewHolder(BaseViewHolder viewHolder);

    /**
     * 绑定 HeaderView
     */
    public interface BindHeaderViewHolder {

        void onBindHeaderViewHolder(BaseViewHolder viewHolder);
    }

    public void setBindHeaderViewHolder(BindHeaderViewHolder bindHeaderViewHolder) {
        this.bindHeaderViewHolder = bindHeaderViewHolder;
    }

    public BaseViewHolder getViewHolder(View view) {
        return new BaseViewHolder(view);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = onCreateItemView(parent);
        //修正 item不充满
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        return getViewHolder(view);

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
                    onItemClickListener.onItemClick(viewHolder, items.get(customHeaderView != null ? viewHolder.getAdapterPosition() - 1 : viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
                }
            });
        }
        if (onItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(viewHolder, items.get(customHeaderView != null ? viewHolder.getAdapterPosition() - 1 : viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    /**
     * 设置头部点击事件
     *
     * @param viewHolder
     */
    private void setHeaderClick(final BaseViewHolder viewHolder) {
        if (onItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onHeaderClick(viewHolder, viewHolder.getAdapterPosition());
                }
            });
        }
        if (onItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onHeaderLongClick(viewHolder, viewHolder.getAdapterPosition());
                    return false;
                }
            });
        }

    }


    /**
     * 设置 ItemView
     *
     * @param parent
     * @return
     */
    protected abstract View onCreateItemView(ViewGroup parent);


    public void insert(T object, int position) {
        items.add(position, object);
        if (customHeaderView != null) position++;
        notifyItemInserted(position);
    }

    public void insertAll(List<T> list, int position) {
        items.addAll(position, list);
        if (customHeaderView != null) position++;
        notifyItemInserted(position);
    }


    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * Remove a item of  the list of the adapter
     *
     * @param position position
     */
    public void remove(int position) {
        if (items.size() > 0 && getItemViewType(position) == VIEW_TYPES.NORMAL) {
            items.remove(customHeaderView != null ? position - 1 : position);
            notifyItemRemoved(position);
        } else {
            customHeaderView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Clear the list of the adapter
     */
    public void clear() {
        int size = items.size();
        items.clear();
        total = 0;
        notifyItemRangeRemoved(customHeaderView != null ? 1 : 0, size);
    }

    /**
     * allow resource layout id to be introduced
     *
     * @param mLayout res id
     */
    public void setCustomLoadMoreView(Context context, @LayoutRes int mLayout) {
        View h_layout = LayoutInflater.from(context).inflate(mLayout, null);
        setCustomLoadMoreView(h_layout);
    }

    /**
     * allow resource layout id to be introduced
     *
     * @param mLayout res id
     */
    public void setCustomLoadMoreView(@LayoutRes int mLayout) {
        View h_layout = LayoutInflater.from(context).inflate(mLayout, null);
        setCustomLoadMoreView(h_layout);
    }

    @Override
    public long generateHeaderId(int position) {
//        if (getItems().get(customHeaderView != null ? position - 1 : position).Error.length() > 0)
//            return getItems().get(customHeaderView != null ? position - 1 : position).Error.charAt(0);
//        else return -1;
        return -1;
    }


    /**
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T> {

        void onItemClick(RecyclerView.ViewHolder viewHolder, T obj, int position);

        void onHeaderClick(RecyclerView.ViewHolder viewHolder, int position);

    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemLongClickListener<T> {

        void onItemLongClick(RecyclerView.ViewHolder viewHolder, T obj, int position);

        void onHeaderLongClick(RecyclerView.ViewHolder viewHolder, int position);

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

    @Override
    public BaseViewHolder newFooterHolder(View view) {
        return new BaseViewHolder(view);
    }

    @Override
    public BaseViewHolder newHeaderHolder(View view) {
        return new BaseViewHolder(view);
    }
}
