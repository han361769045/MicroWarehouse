package com.zczczy.leo.microwarehouse.activities.lotteryactivities;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.fragments.lotteryfragments.AnnouncedFragment;
import com.zczczy.leo.microwarehouse.fragments.lotteryfragments.AnnouncedFragment_;
import com.zczczy.leo.microwarehouse.fragments.lotteryfragments.LotteryHomeFragment;
import com.zczczy.leo.microwarehouse.fragments.lotteryfragments.LotteryHomeFragment_;
import com.zczczy.leo.microwarehouse.fragments.lotteryfragments.LotteryMineFragment;
import com.zczczy.leo.microwarehouse.fragments.lotteryfragments.LotteryMineFragment_;
import com.zczczy.leo.microwarehouse.fragments.lotteryfragments.LotteryShareFragment;
import com.zczczy.leo.microwarehouse.fragments.lotteryfragments.LotteryShareFragment_;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;



/**
 * Created by zczczy on 2016/11/10.
 * 一元夺宝
 */
@EActivity(R.layout.activity_lottery_main)
public class LotteryMainActivity extends BaseLotteryActivity {

    @ViewById
    LinearLayout ll_home, ll_announced, ll_share, ll_mine;

    @ViewById
    ImageView img_home, img_announced, img_share, img_mine;

    @ViewById
    TextView txt_home, txt_announced, txt_share, txt_mine;

    //实例化
    FragmentManager fragmentManager;

    LotteryHomeFragment lotteryHomeFragment;

    LotteryMineFragment lotteryMineFragment;

    AnnouncedFragment announcedFragment;

    LotteryShareFragment lotteryShareFragment;

    //控制  选中状态时再被点中
    int flag = 0;
    @Extra
    int index=0;

    @AfterViews
    void afterView() {
        fragmentManager = getSupportFragmentManager();
        setTabSelection(index);
    }
    //首页
    @Click
    void ll_home() {
        if (flag == 0) {
            return;
        }
        setTabSelection(0);
    }

    //即将揭晓
    @Click
    void ll_announced() {

        if (flag == 1) {
            return;
        }
        setTabSelection(1);
    }

   //晒单
    @Click
    void ll_share() {
        if (flag == 2) {
            return;
        }
        setTabSelection(2);
    }

  //我的夺宝
    @Click
    void ll_mine() {
        if (flag == 3) {
            return;
        }
        setTabSelection(3);
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     */
    public void setTabSelection(int index) {
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                img_home.setSelected(true);
                txt_home.setSelected(true);
                if (lotteryHomeFragment == null) {
                    // 如果homeFragment为空，则创建一个并添加到界面上
                    lotteryHomeFragment = LotteryHomeFragment_.builder().build();
                    transaction.add(R.id.fl_content, lotteryHomeFragment);

                } else {
                    // 如果homeFragment不为空，则直接将它显示出来
                    transaction.show(lotteryHomeFragment);
                }
                flag = 0;
                break;
            case 1:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                img_announced.setSelected(true);
                txt_announced.setSelected(true);
                if (announcedFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    announcedFragment = AnnouncedFragment_.builder().build();
                    transaction.add(R.id.fl_content, announcedFragment,"messageFragment");
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(announcedFragment);
                }
                flag = 1;
                break;
            case 2:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                img_share.setSelected(true);
                txt_share.setSelected(true);
                if (lotteryShareFragment == null) {
                    // 如果contactsFragment为空，则创建一个并添加到界面上
                    lotteryShareFragment = LotteryShareFragment_.builder().build();
                    transaction.add(R.id.fl_content, lotteryShareFragment,"contactsFragment");
                } else {
                    // 如果contactsFragment不为空，则直接将它显示出来
                    transaction.show(lotteryShareFragment);
                }
                flag = 2;
                break;
            case 3:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                img_mine.setSelected(true);
                txt_mine.setSelected(true);
                if (lotteryMineFragment == null) {
                    // 如果meFragment为空，则创建一个并添加到界面上
                    lotteryMineFragment = LotteryMineFragment_.builder().build();
                    transaction.add(R.id.fl_content, lotteryMineFragment,"seatFragment");
                } else {
                    // 如果meFragment不为空，则直接将它显示出来
                    transaction.show(lotteryMineFragment);
                }
                flag = 3;
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    void clearSelection() {
        img_home.setSelected(false);
        img_announced.setSelected(false);
        img_share.setSelected(false);
        img_mine.setSelected(false);

        txt_home.setSelected(false);
        txt_announced.setSelected(false);
        txt_share.setSelected(false);
        txt_mine.setSelected(false);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    void hideFragments(FragmentTransaction transaction) {
        if (lotteryHomeFragment != null) {
            transaction.hide(lotteryHomeFragment);
        }
        if (announcedFragment != null) {
            transaction.hide(announcedFragment);
        }
        if (lotteryShareFragment != null) {
            transaction.hide(lotteryShareFragment);
        }
        if (lotteryMineFragment != null) {
            transaction.hide(lotteryMineFragment);
        }
    }
}
