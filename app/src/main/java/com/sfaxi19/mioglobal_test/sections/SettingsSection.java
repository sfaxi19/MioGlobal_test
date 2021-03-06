package com.sfaxi19.mioglobal_test.sections;

import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sfaxi19.mioglobal_test.MainActivity;
import com.sfaxi19.mioglobal_test.R;

import java.io.File;

/**
 * Created by sfaxi19 on 30.06.16.
 */
public class SettingsSection {

    private MainActivity activity;
    private LinearLayout mainLayout;

    private EditText periodEditText;
    private EditText portEditText;
    private EditText ipEditText;
    private EditText nameEditText;
    private static RadioGroup radioGroup;
    private static RadioButton radioButtonPack1;
    private static RadioButton radioButtonPack2;
    private final static String LOG_TAG ="myLog";

    public SettingsSection(MainActivity activity, View rootView) {
        this.activity = activity;
        mainLayout = (LinearLayout)rootView.findViewById(R.id.settingsLayout);
    }

    public void view() {

        ScrollView scrollView = new ScrollView(activity);
        mainLayout.addView(scrollView);
        LinearLayout settingsLayout = new LinearLayout(activity);
        settingsLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(settingsLayout);

        TextView titleTextView = new TextView(activity);
        titleTextView.setText("Настройки");
        titleTextView.setTextSize(35);
        titleTextView.setPadding(0, 20, 0, 30);
        titleTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        settingsLayout.addView(titleTextView);
        //ip_address
        TextView ipTextView = new TextView(activity);
        ipTextView.setText("IP-адрес получателя:");
        ipTextView.setTextSize(20);
        ipEditText = new EditText(activity);
        ipEditText.setHint("192.168.1.14");
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       android.text.Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart)
                            + source.subSequence(start, end)
                            + destTxt.substring(dend);
                    if (!resultingTxt
                            .matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i = 0; i < splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }

        };
        ipEditText.setFilters(filters);
        ipEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        ipEditText.setMinWidth(200);
        ipEditText.setText(activity.settings.get("ip"));
        GridLayout grid = new GridLayout(activity);
        grid.setColumnCount(2);
        grid.addView(ipTextView);
        grid.addView(ipEditText);
        grid.setPadding(5, 10, 5, 10);
        grid.setBackgroundColor(Color.rgb(240, 240, 240));

        //port
        TextView portTextView = new TextView(activity);
        portTextView.setText("Порт:");
        portTextView.setTextSize(20);
        portEditText = new EditText(activity);
        portEditText.setHint("5554");
        portEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        portEditText.setMinWidth(100);
        portEditText.setText(activity.settings.get("port"));
        grid.addView(portTextView);
        grid.addView(portEditText);

        //period send
        TextView periodTextView = new TextView(activity);
        periodTextView.setText("Период отправки:");
        periodTextView.setTextSize(20);
        periodTextView.setPadding(0, 10, 0, 0);
        periodEditText = new EditText(activity);
        periodEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        periodEditText.setMinWidth(100);
        periodEditText.setHint("5");
        periodEditText.setText(activity.settings.get("period"));
        grid.addView(periodTextView);
        grid.addView(periodEditText);

        //packet type
        TextView packetTextView = new TextView(activity);
        packetTextView.setText("Тип пакета:");
        packetTextView.setTextSize(20);
        packetTextView.setPadding(0, 10, 0, 0);
        grid.addView(packetTextView);
        radioGroup = new RadioGroup(activity);
        radioButtonPack1 = new RadioButton(activity);
        radioButtonPack2 = new RadioButton(activity);
        radioButtonPack1.setText("Puls RSSI MAC X Y Z DATE");
        radioButtonPack2.setText("Type Puls XYZ MAC");
        radioButtonPack1.setTextSize(9);
        radioButtonPack2.setTextSize(9);
        radioGroup.removeAllViews();
        radioButtonPack1.setId(0);
        radioButtonPack2.setId(1);
        radioGroup.addView(radioButtonPack1,0);
        radioGroup.addView(radioButtonPack2,1);
        if(activity.settings.containsKey("packet")){
            radioGroup.check(Integer.decode(activity.settings.get("packet")));
        }else{
            radioGroup.check(1);
        }

        grid.addView(radioGroup);
        settingsLayout.addView(grid);

        //name
        TextView nameTextView = new TextView(activity);
        nameTextView.setText("Имя устройства:");
        nameTextView.setTextSize(30);
        nameTextView.setPadding(0, 30, 0, 20);
        nameTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        nameEditText = new EditText(activity);
        nameEditText.setTextSize(35);
        nameEditText.setTextColor(Color.rgb(255, 0, 0));
        nameEditText.setGravity(Gravity.CENTER_HORIZONTAL);
        nameEditText.setHint("4");
        nameEditText.setText(activity.settings.get("name"));
        settingsLayout.addView(nameTextView);
        settingsLayout.addView(nameEditText);

        Button acceptBtn = new Button(activity);
        acceptBtn.setText("Применить");
        acceptBtn.setPadding(0,10,0,10);
        settingsLayout.addView(acceptBtn);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putSettings();
                activity.hUpdateNetworkSettings.sendEmptyMessage(0);
                activity.nameTextView.setText(activity.settings.get("name"));
            }
        });

        Button saveBtn = new Button(activity);
        saveBtn.setText("Запомнить");
        saveBtn.setPadding(0,10,0,10);
        settingsLayout.addView(saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putSettings();
                activity.saveNetworkSettings();
            }
        });

        Button deleteBtn = new Button(activity);
        deleteBtn.setText("Забыть");
        deleteBtn.setPadding(0,10,0,10);
        settingsLayout.addView(deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(activity.getFilesDir(), "default_settings");
                file.delete();
            }
        });
    }

    private void putSettings(){
        if (!periodEditText.getText().toString().isEmpty()) {
            activity.settings.put("period", periodEditText.getText().toString());
        }else{
            activity.settings.put("period", periodEditText.getHint().toString());
        }
        if (!ipEditText.getText().toString().isEmpty()) {
            activity.settings.put("ip", ipEditText.getText().toString());
        }else{
            activity.settings.put("ip", ipEditText.getHint().toString());
        }
        if (!portEditText.getText().toString().isEmpty()) {
            activity.settings.put("port", portEditText.getText().toString());
        }else{
            activity.settings.put("port", portEditText.getHint().toString());
        }
        if (!nameEditText.getText().toString().isEmpty()) {
            activity.nameTextView.setText(nameEditText.getText().toString());
            activity.settings.put("name", nameEditText.getText().toString());
        }else{
            activity.settings.put("name", nameEditText.getHint().toString());
        }
        activity.settings.put("packet", String.valueOf(radioGroup.getCheckedRadioButtonId()));
    }

}
