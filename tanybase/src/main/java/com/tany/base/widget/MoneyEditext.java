package com.tany.base.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/2/28.
 * 只能输入两位小数的控件
 */

@SuppressLint("AppCompatCustomView")
public class MoneyEditext extends EditText {
    public MoneyEditext(Context context) {
        super(context);
    }

    public MoneyEditext(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.addTextChangedListener(textWatcher);
        this.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
    }

    public MoneyEditext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String varle = s.toString();
            if (varle.toString().equals(".")) {
                varle = "0.";
                MoneyEditext.this.setText(varle);
                MoneyEditext.this.setSelection(varle.length());
            }
            if (!isDouble(varle.toString()) && varle.length() > 0) {
                String var = getVarle(varle.toString()) + "";
                MoneyEditext.this.setText(var);
                MoneyEditext.this.setSelection(var.length());
            }
        }
    };


    public String getVarle(String varle) {
        StringBuilder var = new StringBuilder(varle);
        if (varle.equals(".")) {
            varle = "0.";
        }

        if (TextUtils.isEmpty(var))
            return "0";

        if (isDouble(varle)) {
            return varle;
        } else {
            StringBuilder builder = var.deleteCharAt(var.length() - 1);
            return getVarle(builder.toString());
        }
    }

    public double getNumber() {
        double varle;
        if (TextUtils.isEmpty(this.getText().toString())) {
            varle = 0;
        } else {
            varle = Double.parseDouble(getVarle(this.getText().toString()));
        }
        return varle;
    }

    private boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^\\d+\\d*+.{0,1}+\\d{0,2}");
        return pattern.matcher(str).matches();
    }


}
