package com.wz.wtxyd.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wz.wtxyd.R;
import com.wz.wtxyd.bean.FragmentInfo;
import com.wz.wtxyd.common.util.DensityUtil;
import com.wz.wtxyd.common.util.ToastUtil;
import com.wz.wtxyd.di.component.AppComponent;
import com.wz.wtxyd.ui.fragment.BookSelfFragment;
import com.wz.wtxyd.ui.fragment.FragmentFactory;

import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_NETWORK_STATE;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.wz.wtxyd.R.id.tv_edit;
import static com.wz.wtxyd.ui.fragment.FragmentFactory.BOOK_SELF_FRAGMENT;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private static final int REQUEST_SEARCH_BOOK = 1;

    @BindView(R.id.fl_main)
    FrameLayout mFlMain;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    public DrawerLayout mDrawerLayout;
    @BindView(R.id.rg_main)
    RadioGroup mRgMain;
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    private PopupWindow mPopupWindow;
    private boolean isOpen;
    private int mPosition;
    private List<Fragment> mFragments;
    private Fragment preFragment;

    @Override
    protected void setupAppComponent(AppComponent appComponent) {

    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {


        ActionBarDrawerToggle barDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, mToolbar, R.string.open, R.string.close);
        barDrawerToggle.syncState();
        mToolbar.inflateMenu(R.menu.toolbar_menu);
        mDrawerLayout.addDrawerListener(barDrawerToggle);



        RxPermissions rxPermissions = new RxPermissions(this);

        rxPermissions
                .request(Manifest.permission.INTERNET,
                        Manifest.permission.READ_EXTERNAL_STORAGE,RECORD_AUDIO,ACCESS_NETWORK_STATE
                ,ACCESS_WIFI_STATE,CHANGE_NETWORK_STATE,READ_PHONE_STATE,READ_CONTACTS,WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean){
                            initListener();
                            //默认选择书架
                            mRgMain.check(R.id.rb_book_self);

                            //去掉drawerlayiout滑出后的阴影效果
                            mDrawerLayout.setScrimColor(Color.TRANSPARENT);
                        } else {
                            ToastUtil.show("对不起，需要权限！");
                            finish();
                        }
                    }
                });


    }


    private void initListener() {
       // mIbMainMenu.setOnClickListener(this);
        mRgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_book_self:
                        mPosition = 0;
                        break;
                    case R.id.rb_book_library:
                        mPosition = 1;
                        break;

                }
//                Fragment fragment = FragmentFactory.createFragment(mPosition);
                FragmentInfo fragment = FragmentFactory.createFragment(mPosition);

                changeFragment(fragment);
            }
        });
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = mDrawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                //改变DrawLayout侧栏透明度，若不需要效果可以不设置
                ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                ViewHelper.setTranslationX(mContent,
                        mMenu.getMeasuredWidth() * (1 - scale));
                ViewHelper.setPivotX(mContent, 0);
                ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
                mContent.invalidate();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isOpen = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isOpen = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });



        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.iv_more:
                        showPopupWindow(mToolbar);
                        break;
                    case R.id.iv_search:

                        break;
                }
                return false;
            }
        });

    }

    private void showPopupWindow(View view) {
        View contentView = View.inflate(this, R.layout.pop_window, null);

        TextView tvSearch = (TextView) contentView.findViewById(R.id.tv_search_book);
        TextView tvEdit = (TextView) contentView.findViewById(tv_edit);

        tvSearch.setOnClickListener(this);
        tvEdit.setOnClickListener(this);

        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new PaintDrawable(R.color.white));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPopupWindow.showAsDropDown(view, 0, 0, Gravity.RIGHT);
        } else {
            mPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int xPos =
                    DensityUtil.getScreenH() / 2 - DensityUtil.getNavigationBarrH()
                            - mPopupWindow.getContentView().getMeasuredHeight();
            mPopupWindow.showAtLocation(view, Gravity.RIGHT, 0, -xPos);
        }

    }


    private void changeFragment(FragmentInfo fragmentInfo) {
        Fragment fragment = fragmentInfo.getFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment!=null){
            if (fragment != preFragment) {
                if (fragment.isAdded()) {
                    if (preFragment != null) {
                        ft.hide(preFragment);
                    }
                    ft.show(fragment);
                } else {
                    if (preFragment != null) {
                        ft.hide(preFragment);
                    }
                    ft.add(R.id.fl_main, fragment,fragmentInfo.getFragmentTag());
                }
            }
        }
        preFragment = fragment;
        ft.commit();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //搜索本地书籍
            case R.id.tv_search_book:
                Intent intent = new Intent(this, SearchLocalBookActivity.class);

                startActivity(intent);
                android.support.v4.app.FragmentManager supportFragmentManager = getSupportFragmentManager();
                BookSelfFragment bookSelfFragment = (BookSelfFragment) supportFragmentManager.findFragmentByTag(BOOK_SELF_FRAGMENT);
                bookSelfFragment.hideEditMenu();
                mPopupWindow.dismiss();
                break;

            //编辑本地书架
            case tv_edit:

                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK){
            android.support.v4.app.FragmentManager supportFragmentManager = getSupportFragmentManager();
            BookSelfFragment bookSelfFragment = (BookSelfFragment) supportFragmentManager.findFragmentByTag(BOOK_SELF_FRAGMENT);
            boolean editState = bookSelfFragment.isEditState();
            //判断书加查否是编辑模式
            if (editState){
                bookSelfFragment.hideEditMenu();
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }


        return super.onKeyDown(keyCode, event);
    }
}
