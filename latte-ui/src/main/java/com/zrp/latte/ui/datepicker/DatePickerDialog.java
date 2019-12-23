package com.zrp.latte.ui.datepicker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.example.latte.ui.R;
import com.zrp.latte.ui.datepicker.LoopView.LoopView;
import com.zrp.latte.ui.datepicker.LoopView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 精仿iOSAlertViewController控件
 * 点击取消按钮返回 －1，其他按钮从0开始算
 */
public class DatePickerDialog implements OnItemSelectedListener {
    private OnConfirmeListener confirmeListener;
    private Animation bgAnim;
    private Animation bgAnimOut;
    private LoopView yearView;
    private LoopView monthView;
    private LoopView dayView;
    private ArrayList<String> dayList;

    /**
     * 三个时间的监听
     *
     * @param view
     */
    @Override
    public void onItemSelected(LoopView view) {
        String year = yearView.getItems().get(yearView.getSelectedItem());
        year = year.substring(0, year.length() - 1);
        String month = monthView.getItems().get(monthView.getSelectedItem());
        month = month.substring(0, month.length() - 1);
        getDay(Integer.parseInt(year), Integer.parseInt(month));
        dayView.setInitPosition(0);
        dayView.setItems(dayList);

    }

    /**
     * @param year
     * @param month
     * @return
     */

    private int getDay(int year, int month) {
        int size = dayList.size();
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                for (int i = size; i < 31; i++) {
                    dayList.add(i + 1 + "日");
                }
                break;
            case 2:
                day = flag ? 29 : 28;
                for (int i = day; i < size; i++) {
                    dayList.remove(dayList.size() - 1);
                }
                if (size == 28 && flag) {
                    dayList.add(29 + "日");
                }

                break;
            default:
                day = 30;
                for (int i = size; i < 30; i++) {
                    dayList.add(i + 1 + "日");
                }
                if (size == 31) {
                    dayList.remove(size - 1);
                }
                break;
        }
        return day;
    }

    public static enum Style {
        ActionSheet,
        Alert,
        Date
    }

    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
    );
    public static final int HORIZONTAL_BUTTONS_MAXCOUNT = 2;
    public static final String OTHERS = "others";
    public static final String DESTRUCTIVE = "destructive";
    public static final String CANCEL = "cancel";
    public static final String TITLE = "title";
    public static final String MSG = "msg";
    public static final int CANCELPOSITION = -1;//点击取消按钮返回 －1，其他按钮从0开始算

    private String title;
    private String msg;
    private List<String> mDestructive;
    private List<String> mOthers;
    private String cancel;
    private ArrayList<String> mDatas = new ArrayList<String>();

    private Context context;
    private ViewGroup contentContainer;
    private ViewGroup decorView;//activity的根View
    private ViewGroup rootView;//DatePickerDialog 的 根View
    private ViewGroup loAlertHeader;//窗口headerView

    private Style style = Style.Alert;

    private OnDismissListener onDismissListener;
    private OnItemClickListener onItemClickListener;
    private boolean isDismissing;

    private Animation outAnim;
    private Animation inAnim;
    private int gravity = Gravity.CENTER;

    public DatePickerDialog(String title, String msg, String cancel, String[] destructive, String[] others, Context context, Style style, OnItemClickListener onItemClickListener) {
        this.context = context;
        if (style != null) this.style = style;
        this.onItemClickListener = onItemClickListener;
        initData(title, msg, cancel, destructive, others);
        initViews();
        init();
        initEvents();
    }

    public DatePickerDialog(String title, Context context, int startyear, int endyear, OnConfirmeListener confirmeListener) {
        this.title = title;
        this.context = context;
        this.confirmeListener = confirmeListener;
        this.style = Style.Date;
        initData(startyear, endyear);
        init();
        initEvents();
    }
	/**
	 * 自定义的三级选择,联动需要自己设置下一个loopView的内容
	 *
	 * @param title
	 * @param context
	 * @param year
	 * @param month
	 * @param day
	 * @param confirmeListener
	 */
	public DatePickerDialog(String title, Context context, List<String> year, List<String> month, List<String> day, OnConfirmeListener confirmeListener) {
		this.context = context;
		this.title = title;
		this.style = Style.Date;
		this.confirmeListener = confirmeListener;
		initViews(year, month, day);
		init();
		initEvents();
	}

    /**
     * 初始化时间
     *
     * @param startyear
     * @param endyear
     */
    private void initData(int startyear, int endyear) {
        List<String> yearList = new ArrayList<>();
        List<String> monthList = new ArrayList<>();
        dayList = new ArrayList<>();
        for (int i = startyear; i < endyear + 1; i++) {
            yearList.add(i + "年");
        }
        for (int i = 1; i < 13; i++) {
            monthList.add(i + "月");
        }
        for (int i = 1; i < 32; i++) {
            dayList.add(i + "日");
        }
        initViews(yearList, monthList, dayList);
        initLoopViewListener();

    }

    private void initLoopViewListener() {
        yearView.setListener(this);
        monthView.setListener(this);
//        dayView.setListener(this);
    }



    private void initViews(List<String> year, List<String> month, List<String> day) {
        initViews();
        params.gravity = Gravity.BOTTOM;
        contentContainer.setLayoutParams(params);
        gravity = Gravity.BOTTOM;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        initDateViews(layoutInflater, year, month, day, title);
    }

    private void initDateViews(LayoutInflater layoutInflater, List<String> year, final List<String> month, final List<String> day, String titlr) {
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.alertview_data, contentContainer);
        yearView = (LoopView) viewGroup.findViewById(R.id.year);
        monthView = (LoopView) viewGroup.findViewById(R.id.month);
        TextView tvTitle = (TextView) viewGroup.findViewById(R.id.tvTitle);
        viewGroup.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                String s = yearView.getItems().get(yearView.getSelectedItem());
                if (month != null) {
                    s = s + monthView.getItems().get(monthView.getSelectedItem());
                }
                if (day != null) {
                    s = s + dayView.getItems().get(dayView.getSelectedItem());
                }
                confirmeListener.result(s);
            }
        });
        viewGroup.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tvTitle.setText(title);
        dayView = (LoopView) viewGroup.findViewById(R.id.day);
        yearView.setItems(year);
        if (month == null) {
            monthView.setVisibility(View.GONE);
        } else {
            monthView.setItems(month);
        }
        if (day == null) {
            dayView.setVisibility(View.GONE);
        } else {
            dayView.setItems(day);
        }


    }

    /**
     * 获取数据
     */
    protected void initData(String title, String msg, String cancel, String[] destructive, String[] others) {

        this.title = title;
        this.msg = msg;
        if (destructive != null) {
            this.mDestructive = Arrays.asList(destructive);
            this.mDatas.addAll(mDestructive);
        }
        if (others != null) {
            this.mOthers = Arrays.asList(others);
            this.mDatas.addAll(mOthers);
        }
        if (cancel != null) {
            this.cancel = cancel;
            if (style == Style.Alert && mDatas.size() < HORIZONTAL_BUTTONS_MAXCOUNT) {
                this.mDatas.add(0, cancel);
            }
        }

    }

    protected void initViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_alertview, decorView, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
//        rootView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if () {
//                    dismiss();
//                }
//                return false;
//            }
//        });
        contentContainer = (ViewGroup) rootView.findViewById(R.id.content_container);
        contentContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        int margin_alert_left_right = 0;
        switch (style) {
            case ActionSheet:
                params.gravity = Gravity.BOTTOM;
                margin_alert_left_right = context.getResources().getDimensionPixelSize(R.dimen.margin_actionsheet_left_right);
                params.setMargins(margin_alert_left_right, 0, margin_alert_left_right, margin_alert_left_right);
                contentContainer.setLayoutParams(params);
                gravity = Gravity.BOTTOM;
                initActionSheetViews(layoutInflater);
                break;
            case Alert:
                params.gravity = Gravity.CENTER;
                margin_alert_left_right = context.getResources().getDimensionPixelSize(R.dimen.margin_alert_left_right);
                params.setMargins(margin_alert_left_right, 0, margin_alert_left_right, 0);
                contentContainer.setLayoutParams(params);
                gravity = Gravity.CENTER;
                initAlertViews(layoutInflater);
                break;
        }
    }

    protected void initHeaderView(ViewGroup viewGroup) {
        loAlertHeader = (ViewGroup) viewGroup.findViewById(R.id.loAlertHeader);
        //标题和消息
        TextView tvAlertTitle = (TextView) viewGroup.findViewById(R.id.tvAlertTitle);
        TextView tvAlertMsg = (TextView) viewGroup.findViewById(R.id.tvAlertMsg);
        if (title != null) {
            tvAlertTitle.setText(title);
        } else {
            tvAlertTitle.setVisibility(View.GONE);
        }
        if (msg != null) {
            tvAlertMsg.setText(msg);
        } else {
            tvAlertMsg.setVisibility(View.GONE);
        }
    }

    protected void initListView() {
        ListView alertButtonListView = (ListView) contentContainer.findViewById(R.id.alertButtonListView);
        //把cancel作为footerView
        if (cancel != null && style == Style.Alert) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_alertbutton, null);
            TextView tvAlert = (TextView) itemView.findViewById(R.id.tvAlert);
            tvAlert.setText(cancel);
            tvAlert.setClickable(true);
            tvAlert.setTypeface(Typeface.DEFAULT_BOLD);
            tvAlert.setTextColor(context.getResources().getColor(R.color.textColor_alert_button_cancel));
            tvAlert.setBackgroundResource(R.drawable.bg_alertbutton_bottom);
            tvAlert.setOnClickListener(new OnTextClickListener(CANCELPOSITION));
            alertButtonListView.addFooterView(itemView);
        }
        AlertViewAdapter adapter = new AlertViewAdapter(mDatas, mDestructive);
        alertButtonListView.setAdapter(adapter);
        alertButtonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(DatePickerDialog.this, position);
                dismiss();
            }
        });
    }

    protected void initActionSheetViews(LayoutInflater layoutInflater) {
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.layout_alertview_actionsheet, contentContainer);
        initHeaderView(viewGroup);

        initListView();
        TextView tvAlertCancel = (TextView) contentContainer.findViewById(R.id.tvAlertCancel);
        if (cancel != null) {
            tvAlertCancel.setVisibility(View.VISIBLE);
            tvAlertCancel.setText(cancel);
        }
        tvAlertCancel.setOnClickListener(new OnTextClickListener(CANCELPOSITION));
    }

    protected void initAlertViews(LayoutInflater layoutInflater) {

        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.layout_alertview_alert, contentContainer);
        initHeaderView(viewGroup);

        int position = 0;
        //如果总数据小于等于HORIZONTAL_BUTTONS_MAXCOUNT，则是横向button
        if (mDatas.size() <= HORIZONTAL_BUTTONS_MAXCOUNT) {
            ViewStub viewStub = (ViewStub) contentContainer.findViewById(R.id.viewStubHorizontal);
            viewStub.inflate();
            LinearLayout loAlertButtons = (LinearLayout) contentContainer.findViewById(R.id.loAlertButtons);
            for (int i = 0; i < mDatas.size(); i++) {
                //如果不是第一个按钮
                if (i != 0) {
                    //添加上按钮之间的分割线
                    View divier = new View(context);
                    divier.setBackgroundColor(context.getResources().getColor(R.color.bgColor_divier));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.size_divier), LinearLayout.LayoutParams.MATCH_PARENT);
                    loAlertButtons.addView(divier, params);
                }
                View itemView = LayoutInflater.from(context).inflate(R.layout.item_alertbutton, null);
                TextView tvAlert = (TextView) itemView.findViewById(R.id.tvAlert);
                tvAlert.setClickable(true);

                //设置点击效果
                if (mDatas.size() == 1) {
                    tvAlert.setBackgroundResource(R.drawable.bg_alertbutton_bottom);
                } else if (i == 0) {//设置最左边的按钮效果
                    tvAlert.setBackgroundResource(R.drawable.bg_alertbutton_left);
                } else if (i == mDatas.size() - 1) {//设置最右边的按钮效果
                    tvAlert.setBackgroundResource(R.drawable.bg_alertbutton_right);
                }
                String data = mDatas.get(i);
                tvAlert.setText(data);

                //取消按钮的样式
                if (data == cancel) {
                    tvAlert.setTypeface(Typeface.DEFAULT_BOLD);
                    tvAlert.setTextColor(context.getResources().getColor(R.color.textColor_alert_button_cancel));
                    tvAlert.setOnClickListener(new OnTextClickListener(CANCELPOSITION));
                    position = position - 1;
                }
                //高亮按钮的样式
                else if (mDestructive != null && mDestructive.contains(data)) {
                    tvAlert.setTextColor(context.getResources().getColor(R.color.textColor_alert_button_destructive));
                }

                tvAlert.setOnClickListener(new OnTextClickListener(position));
                position++;
                loAlertButtons.addView(itemView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            }
        } else {
            ViewStub viewStub = (ViewStub) contentContainer.findViewById(R.id.viewStubVertical);
            viewStub.inflate();
            initListView();
        }
    }

    protected void init() {
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
        bgAnim = AnimationUtils.loadAnimation(context, R.anim.alertview_bgin);
        bgAnimOut = AnimationUtils.loadAnimation(context, R.anim.alertview_bgout);
    }

    protected void initEvents() {
    }

    public DatePickerDialog addExtView(View extView) {
        loAlertHeader.addView(extView);
        return this;
    }

    /**
     * show的时候调用
     *
     * @param view 这个View
     */
    private void onAttached(View view) {
        decorView.addView(view);
        rootView.startAnimation(bgAnim);
        contentContainer.startAnimation(inAnim);
    }

    /**
     * 添加这个View到Activity的根视图
     */
    public void show() {
        if (isShowing()) {
            return;
        }
        onAttached(rootView);
    }

    /**
     * 检测该View是不是已经添加到根视图
     *
     * @return 如果视图已经存在该View返回true
     */
    public boolean isShowing() {
        View view = decorView.findViewById(R.id.outmost_container);
        return view != null;
    }

    public void dismiss() {
        if (isDismissing) {
            return;
        }

        //消失动画
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                decorView.post(new Runnable() {
                    @Override
                    public void run() {
                        //从activity根视图移除
                        decorView.removeView(rootView);
                        isDismissing = false;
                        if (onDismissListener != null) {
                            onDismissListener.onDismiss(DatePickerDialog.this);
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        contentContainer.startAnimation(outAnim);
        rootView.startAnimation(bgAnimOut);
        isDismissing = true;
    }

    public Animation getInAnimation() {
        int res = AlertAnimateUtil.getAnimationResource(this.gravity, true);
        return AnimationUtils.loadAnimation(context, res);
    }

    public Animation getOutAnimation() {
        int res = AlertAnimateUtil.getAnimationResource(this.gravity, false);
        return AnimationUtils.loadAnimation(context, res);
    }

    public DatePickerDialog setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    class OnTextClickListener implements View.OnClickListener {

        private int position;

        public OnTextClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(DatePickerDialog.this, position);
            dismiss();
        }
    }

    /**
     * 主要用于拓展View的时候有输入框，键盘弹出则设置MarginBottom往上顶，避免输入法挡住界面
     */
    public void setMarginBottom(int marginBottom) {
        int margin_alert_left_right = context.getResources().getDimensionPixelSize(R.dimen.margin_alert_left_right);
        params.setMargins(margin_alert_left_right, 0, margin_alert_left_right, marginBottom);
        contentContainer.setLayoutParams(params);
    }

    /**
     * 设置点击返回键是否关闭对话框
     *
     * @param isCancelable
     * @return
     */
    public DatePickerDialog setCancelable(boolean isCancelable) {
        View view = rootView.findViewById(R.id.outmost_container);

        if (isCancelable) {
            view.setOnTouchListener(onCancelableTouchListener);
        } else {
            view.setOnTouchListener(null);
        }
        return this;
    }

    /**
     * Called when the user touch on black overlay in order to dismiss the dialog
     */
    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dismiss();
            }
            return false;
        }
    };

    /**
     * 向外爆喽三个联动View
     *
     * @return
     */
    public LoopView getYearView() {
        return yearView;
    }

    public LoopView getMonthView() {
        return monthView;
    }

    public LoopView getDayView() {
        return dayView;
    }
}
