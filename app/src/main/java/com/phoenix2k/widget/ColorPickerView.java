package com.phoenix2k.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phoenix2k.priorityreminder.R;


/**
 * Created by Pushpan on 13/04/17.
 */
public class ColorPickerView extends RelativeLayout implements OnClickListener {

    // final AlertDialog dialog;
    private OnColorSecletionListener mOnColorSelectionListener;
    private View mViewHue;
    private ShadeSelectorView mShadeSelectoView;
    private ImageView mViewCursor;
    private View mViewOldColor;
    private View mViewNewColor;
    private ImageView mViewTarget;
    private ViewGroup mViewContainer;
    private TextView mTxtNoColor;
    private float[] mCurrentColorHsv = new float[3];
    private View mLytDisable;
    private View mWhiteColorView;
    private View mBlackColorView;

    private RadioGroup mRadioGroupColorSelectionType;
    private RadioButton mRadioTypeAuto;
    private RadioButton mRadioTypeCustom;

    ImageView mImgSave;
    ImageView mImgCancel;

    private int mCurrentColor;
    private boolean mDoubleButtonTheme;

    public ColorPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ColorPickerView(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        inflate(getContext(), R.layout.layout_color_picker, this);
        mViewHue = findViewById(R.id.viewHue);
        mShadeSelectoView = (ShadeSelectorView) findViewById(R.id.shadeSelectoView);
        mViewCursor = (ImageView) findViewById(R.id.cursor);
        mViewOldColor = findViewById(R.id.old_color_view);
        mViewNewColor = findViewById(R.id.new_color_view);
        mViewTarget = (ImageView) findViewById(R.id.target);
        mViewContainer = (ViewGroup) findViewById(R.id.viewContainer);
        mTxtNoColor = (TextView) findViewById(R.id.txtNoColor);
        mLytDisable = findViewById(R.id.lytDisable);
        mWhiteColorView = findViewById(R.id.white_color_view);
        mBlackColorView = findViewById(R.id.black_color_view);
        mShadeSelectoView.setHue(getHue());

        mViewHue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE
                        || event.getAction() == MotionEvent.ACTION_DOWN
                        || event.getAction() == MotionEvent.ACTION_UP) {

                    float x = event.getX();
                    if (x < 0.f)
                        x = 0.f;
                    if (x > mViewHue.getMeasuredWidth())
                        x = mViewHue.getMeasuredWidth() - 0.001f; // to avoid
                    // looping
                    // from end
                    // to start.
                    float hue = 360.f - 360.f / mViewHue.getMeasuredWidth() * x;
                    if (hue == 360.f)
                        hue = 0.f;
                    setHue(hue);

                    // update view
                    mShadeSelectoView.setHue(getHue());
                    moveCursor();
                    mViewNewColor.setBackgroundColor(getColor());

                    return true;
                }
                return false;
            }
        });
        mShadeSelectoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE
                        || event.getAction() == MotionEvent.ACTION_DOWN
                        || event.getAction() == MotionEvent.ACTION_UP) {

                    float x = event.getX(); // touch event are in dp units.
                    float y = event.getY();
                    if (x < 0.f)
                        x = 0.f;
                    if (x > mShadeSelectoView.getMeasuredWidth())
                        x = mShadeSelectoView.getMeasuredWidth();
                    if (y < 0.f)
                        y = 0.f;
                    if (y > mShadeSelectoView.getMeasuredHeight())
                        y = mShadeSelectoView.getMeasuredHeight();

                    setSat(1.f / mShadeSelectoView.getMeasuredWidth() * x);
                    setVal(1.f - (1.f / mShadeSelectoView.getMeasuredHeight() * y));

                    // update view
                    moveTarget();
                    mViewNewColor.setBackgroundColor(getColor());
                    Log.e("Color", "Sat=" + getSat() + " Val=" + getVal()
                            + " Hue=" + getHue());
                    return true;
                }
                return false;
            }
        });


        mRadioGroupColorSelectionType = (RadioGroup) findViewById(R.id.radio_group_color_selection_type);
        mRadioTypeAuto = (RadioButton) findViewById(R.id.radio_type_auto);
        mRadioTypeCustom = (RadioButton) findViewById(R.id.radio_type_custom);
        setDoubleButtonTheme(false);
        // move cursor & target on first draw
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                moveCursor();
                moveTarget();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        mTxtNoColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtNoColor.setSelected(!mTxtNoColor.isSelected());
                viewPickerEnabled(!mTxtNoColor.isSelected());
            }
        });
        mWhiteColorView.setOnClickListener(this);
        mBlackColorView.setOnClickListener(this);
        mViewOldColor.setOnClickListener(this);
        mViewNewColor.setOnClickListener(this);

        mImgSave = (ImageView) findViewById(R.id.imgSave);
        mImgSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               if(getOnColorSelectionListener()!=null){
                   getOnColorSelectionListener().onColorSelection(getColor());
               }
            }
        });
        mImgCancel = (ImageView) findViewById(R.id.imgCancel);
        mImgCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getOnColorSelectionListener()!=null){
                    getOnColorSelectionListener().onColorCancel();
                }
            }
        });
        applyTheme();
    }

    private void applyTheme() {
//        ThemePak pak = ConfigurationDataStore.getInstance().getThemePak();
//        Drawable bg = mRadioTypeAuto.getBackground();
//        ViewCompat.setBackground(mRadioTypeAuto, pak.getThemedDrawable(ThemePak.ColorGroup.contextActionButton, bg));
//        bg = mRadioTypeCustom.getBackground();
//        ViewCompat.setBackground(mRadioTypeCustom, pak.getThemedDrawable(ThemePak.ColorGroup.contextActionButton, bg));
//        mRadioTypeAuto.setTextColor(pak.getColorStateList(ThemePak.ColorGroup.contextActionButtonBackground));
//        mRadioTypeCustom.setTextColor(pak.getColorStateList(ThemePak.ColorGroup.contextActionButtonBackground));

    }


    public boolean isDoubleButtonTheme() {
        return mDoubleButtonTheme;
    }

    public void setDoubleButtonTheme(boolean mDoubleButtonTheme) {
        this.mDoubleButtonTheme = mDoubleButtonTheme;
        if (mDoubleButtonTheme) {
            mRadioGroupColorSelectionType.setVisibility(View.VISIBLE);
            mTxtNoColor.setVisibility(View.GONE);
            mRadioGroupColorSelectionType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.radio_type_auto) {
                        if (mRadioTypeAuto.isChecked()) {
                            viewPickerEnabled(false);
                        }

                    } else if (checkedId == R.id.radio_type_custom) {
                        if (mRadioTypeCustom.isChecked()) {
                            viewPickerEnabled(true);
                        }

                    }
                }
            });
        } else {
            mRadioGroupColorSelectionType.setVisibility(View.GONE);
            mTxtNoColor.setVisibility(View.VISIBLE);
            mRadioGroupColorSelectionType.setOnCheckedChangeListener(null);
        }
    }


    /**
     * create an AmbilWarnaDialog. call this only from OnCreateDialog() or from
     * a background thread.
     *
     * @param color current color
     */
    public void setToEdit(int color) {
        this.mCurrentColor = color;
        viewPickerEnabled(color != 0);
        mTxtNoColor.setSelected((color == 0));
        if(color==0){
            mRadioTypeAuto.performClick();
        }else{
            mRadioTypeCustom.performClick();
        }
        Color.colorToHSV(color, mCurrentColorHsv);
        mViewOldColor.setBackgroundColor(color);
        mViewNewColor.setBackgroundColor(color);
        mShadeSelectoView.setHue(getHue());
        postDelayed(new Runnable() {
            @Override
            public void run() {
                moveCursor();
                moveTarget();
            }
        }, 200);

    }

    protected void moveCursor() {
        float x = mViewHue.getMeasuredWidth()
                - (getHue() * mViewHue.getMeasuredWidth() / 360.f);
        if (x == mViewHue.getMeasuredWidth())
            x = 0.f;
        mViewCursor.setTranslationX(x - mViewCursor.getWidth() / 2
                + mShadeSelectoView.getLeft());
    }

    protected void moveTarget() {
        float x = getSat() * mShadeSelectoView.getMeasuredWidth();
        float y = (1.f - getVal()) * mShadeSelectoView.getMeasuredHeight();

        mViewTarget.setTranslationX(x - mViewTarget.getWidth() / 2
                + mShadeSelectoView.getLeft());
        mViewTarget.setTranslationY(y - mViewTarget.getHeight() / 2);
    }

    public int getColor() {
        return ((isDoubleButtonTheme() && mRadioTypeAuto.isChecked()) || (!isDoubleButtonTheme() && mTxtNoColor.isSelected())) ? 0 : Color
                .HSVToColor(mCurrentColorHsv);
    }

    private float getHue() {
        return mCurrentColorHsv[0];
    }

    private float getSat() {
        return mCurrentColorHsv[1];
    }

    private float getVal() {
        return mCurrentColorHsv[2];
    }

    private void setHue(float hue) {
        mCurrentColorHsv[0] = hue;
    }

    private void setSat(float sat) {
        mCurrentColorHsv[1] = sat;
    }

    private void setVal(float val) {
        mCurrentColorHsv[2] = val;
    }

    public void viewPickerEnabled(boolean enabled) {
        mLytDisable.setVisibility(enabled ? GONE : VISIBLE);
        mShadeSelectoView.setEnabled(enabled);
        mViewHue.setEnabled(enabled);
        mWhiteColorView.setEnabled(enabled);
        mBlackColorView.setEnabled(enabled);
        mViewOldColor.setEnabled(enabled);
        mViewNewColor.setEnabled(enabled);
    }

    @Override
    public void onClick(View v) {
        if (v == mWhiteColorView) {
            setSat(0);
            setHue(0);
            setVal(1);
        } else if (v == mBlackColorView) {
            setSat(1);
            setHue(0);
            setVal(0);
        } else if (v == mViewOldColor) {
            Color.colorToHSV(mCurrentColor, mCurrentColorHsv);
            mViewNewColor.setBackgroundColor(mCurrentColor);
        }
        mShadeSelectoView.setHue(getHue());
        mViewOldColor.setBackgroundColor(mCurrentColor);
        mViewNewColor.setBackgroundColor(getColor());
        moveCursor();
        moveTarget();
    }

    public OnColorSecletionListener getOnColorSelectionListener() {
        return mOnColorSelectionListener;
    }

    public void setOnColorSelectionListener(OnColorSecletionListener mOnColorSelectionListener) {
        this.mOnColorSelectionListener = mOnColorSelectionListener;
    }

    public interface OnColorSecletionListener {
        void onColorCancel();

        void onColorSelection(int color);
    }
}
