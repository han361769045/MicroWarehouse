package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.TaskOrderModel;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.springframework.util.StringUtils;

/**
 * @author Created by LuLeo on 2016/6/17.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/17.
 */
@EViewGroup(R.layout.activity_task_order_item)
public class TaskOrderItemView extends ItemView<TaskOrderModel> {

    @ViewById
    ImageView img_avatar;

    @ViewById
    TextView txt_title, txt_publish_time, txt_publisher, txt_content;

    public TaskOrderItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {
        if (!StringUtils.isEmpty(_data.PublishHeadImg)) {
            Glide.with(context)
                    .load(_data.PublishHeadImg)
                    .skipMemoryCache(true)
                    .crossFade()
                    .centerCrop()
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(img_avatar);
        }
        txt_title.setText(_data.TaskTitle);
        txt_publish_time.setText(_data.PublishTime);
        txt_publisher.setText(_data.PublishLogin);
        txt_content.setText(_data.TaskOrderContent);
    }
}
