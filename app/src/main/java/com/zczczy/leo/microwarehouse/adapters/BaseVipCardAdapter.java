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

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.animators.internal.ViewHelper;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeItemManagerImpl;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeItemManagerInterface;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeLayout;
import com.zczczy.leo.microwarehouse.MyApplication;
import com.zczczy.leo.microwarehouse.items.BaseUltimateViewHolder;
import com.zczczy.leo.microwarehouse.items.ItemView;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
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
 * Created by zczczy on 2016/9/23.
 */
@EBean
public abstract class BaseVipCardAdapter<T> extends UltimateViewAdapter<BaseUltimateViewHolder> implements SwipeItemManagerInterface {
    protected SwipeItemManagerImpl mItemManger = new SwipeItemManagerImpl(this);

    private List<T> items = new ArrayList<>();

    private int total = 0;

    private boolean isFirstOnly = true;

    private Interpolator mInterpolator = new LinearInterpolator();

    private int mDuration = 300;

    private int mLastPosition = 5;

    private BaseVipCardAdapter.OnItemClickListener<T> onItemClickListener;

    private BaseVipCardAdapter.OnItemLongClickListener<T> onItemLongClickListener;

    private BaseVipCardAdapter.BindHeaderViewHolder bindHeaderViewHolder;

    public BaseVipCardAdapter.VerticalAndHorizontal verticalAndHorizontal;

    private BaseVipCardAdapter.DynamicHeight dynamicHeight;

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
     * @param
     * @param objects
     */
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
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseUltimateViewHolder viewHolder, int position) {

        if (getItemViewType(position) == BaseVipCardAdapter.VIEW_TYPES.NORMAL) {
            ItemView<T> itemView = (ItemView) viewHolder.itemView;
            itemView.init(items.get(customHeaderView != null ? position - 1 : position), this, viewHolder);
            if (viewHolder.swipeLayout != null) {
                mItemManger.updateConvertView(viewHolder, position);
            }
            setNormalClick(viewHolder);
        } else if (getItemViewType(position) == BaseVipCardAdapter.VIEW_TYPES.HEADER) {
            setHeaderClick(viewHolder);
            if (bindHeaderViewHolder != null) {
                bindHeaderViewHolder.onBindHeaderViewHolder(viewHolder);
            } else {
                onBindHeaderViewHolder(viewHolder);
            }

        } else if (getItemViewType(position) == BaseVipCardAdapter.VIEW_TYPES.FOOTER) {

        }
        if (!isFirstOnly || position > mLastPosition) {
            for (Animator anim : getAdapterAnimations(viewHolder.itemView, AdapterAnimationType.ScaleIn)) {
                anim.setDuration(mDuration).start();
                anim.setInterpolator(mInterpolator);
            }
            mLastPosition = position;
        } else {
            ViewHelper.clear(viewHolder.itemView);
        }
        if (dynamicHeight != null && position == 0) {
//            int cellWidth = viewHolder.itemView.getWidth();// this will give you cell width dynamically
//            int cellHeight = viewHolder.itemView.height;// this will give you cell height dynamically
            dynamicHeight.HeightChange(position, 55); //call your iterface hear
        }
    }

    abstract void onBindHeaderViewHolder(BaseUltimateViewHolder viewHolder);

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    /**
     * 绑定 HeaderView
     */
    public interface BindHeaderViewHolder {

        void onBindHeaderViewHolder(BaseUltimateViewHolder viewHolder);
    }

    public void setBindHeaderViewHolder(BaseVipCardAdapter.BindHeaderViewHolder bindHeaderViewHolder) {
        this.bindHeaderViewHolder = bindHeaderViewHolder;
    }

    @Override
    public BaseUltimateViewHolder getViewHolder(View view) {

        return new BaseUltimateViewHolder(view);
    }

    @Override
    public BaseUltimateViewHolder onCreateViewHolder(ViewGroup parent) {
        final View view = onCreateItemView(parent);
        //修正 item不充满
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        final BaseUltimateViewHolder baseViewHolder = getViewHolder(view);
        SwipeLayout swipeLayout = baseViewHolder.swipeLayout;
        if (swipeLayout != null) {
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
                @Override
                public void onDoubleClick(SwipeLayout layout, boolean surface) {

                }
            });
            swipeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SwipeLayout) v).close();
                }
            });
            swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOpenItems().get(0) != -1) {
                        closeItem(getOpenItems().get(0));
                        closeAllExcept(null);
                    } else {
                        view.performClick();
                    }
                }
            });
            swipeLayout.getSurfaceView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (getOpenItems().get(0) != -1) {
                        //closeItem(getOpenItems().get(0));
                        closeAllExcept(null);
                    } else {
                        view.performLongClick();
                    }
                    return false;
                }
            });
        }
        return baseViewHolder;
    }

    /**
     * 设置普通itemclick事件
     *
     * @param viewHolder
     */
    private void setNormalClick(final BaseUltimateViewHolder viewHolder) {
        if (onItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOpenItems().get(0) != -1 && viewHolder.swipeLayout != null) {
                        //closeItem(getOpenItems().get(0));
                        closeAllExcept(null);
                    } else {
                        onItemClickListener.onItemClick(viewHolder, items.get(customHeaderView != null ? viewHolder.getAdapterPosition() - 1 : viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
                    }
                }
            });
        }
        if (onItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (getOpenItems().get(0) != -1 && viewHolder.swipeLayout != null) {
                        //closeItem(getOpenItems().get(0));
                        closeAllExcept(null);
                    } else {
                        onItemLongClickListener.onItemLongClick(viewHolder, items.get(customHeaderView != null ? viewHolder.getAdapterPosition() - 1 : viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
                    }
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
    private void setHeaderClick(final BaseUltimateViewHolder viewHolder) {

        if (onItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOpenItems().get(0) != -1) {
                        //closeItem(getOpenItems().get(0));
                        closeAllExcept(null);
                    } else {
                        onItemClickListener.onHeaderClick(viewHolder, viewHolder.getAdapterPosition());
                    }
                }
            });
        }
        if (onItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (getOpenItems().get(0) != -1) {
                        //closeItem(getOpenItems().get(0));
                        closeAllExcept(null);
                    } else {
                        onItemLongClickListener.onHeaderLongClick(viewHolder, viewHolder.getAdapterPosition());
                    }
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
        if (items.size() > 0 && getItemViewType(position) == BaseVipCardAdapter.VIEW_TYPES.NORMAL) {
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

    public  boolean isFooter;

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
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public SwipeItemManagerImpl.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(SwipeItemManagerImpl.Mode mode) {
        mItemManger.setMode(mode);
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
    public void setOnItemClickListener(BaseVipCardAdapter.OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T> {

        void onItemClick(RecyclerView.ViewHolder viewHolder, T obj, int position);

        void onHeaderClick(RecyclerView.ViewHolder viewHolder, int position);

    }

    public void setOnItemLongClickListener(BaseVipCardAdapter.OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemLongClickListener<T> {

        void onItemLongClick(RecyclerView.ViewHolder viewHolder, T obj, int position);

        void onHeaderLongClick(RecyclerView.ViewHolder viewHolder, int position);

    }

    public class VIEW_TYPES {
        public static final int NORMAL = 0;
        public static final int HEADER = 1;
        public static final int FOOTER = 2;
        public static final int CHANGED_FOOTER = 3;
    }

    public enum VerticalAndHorizontal {
        Vertical,
        Horizontal
    }

    public interface DynamicHeight {
        void HeightChange(int position, int height);
    }

    public BaseVipCardAdapter.DynamicHeight getDynamicHeight() {
        return dynamicHeight;
    }

    public void setDynamicHeight(BaseVipCardAdapter.DynamicHeight dynamicHeight) {
        this.dynamicHeight = dynamicHeight;
    }
}
