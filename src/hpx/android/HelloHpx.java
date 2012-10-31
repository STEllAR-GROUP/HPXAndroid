
//  Copyright (c) 2012 Thomas Heller
//
//  Distributed under the Boost Software License, Version 1.0. (See accompanying
//  file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)

package hpx.android;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;

import android.widget.TextView;
import android.widget.Button;
import android.widget.CheckBox;

import android.net.wifi.WifiManager.WifiLock;
import android.net.wifi.WifiManager;

import android.util.Log;
import android.content.Context;

import java.util.HashSet;

public class HelloHpx extends Activity
{
    Runtime runtime = new Runtime();
    WifiLock wifiLock;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        wifiLock = ((WifiManager)getSystemService(Context.WIFI_SERVICE)).createWifiLock("hpx.android");
        wifiLock.acquire();
        super.onCreate(savedInstanceState);

        runtime.registerCallback(
            "setHelloWorldText"
          , new HpxCallback() {
                public final void apply(final String arg) {
                    HelloHpx.this.runOnUiThread(
                        new Runnable() {
                            public final void run() {
                                TextView tv = (TextView)findViewById(R.id.hello_message);
                                String tt = tv.getText().toString();
                                tt += "\n" + arg;
                                tv.setText(tt);
                            }
                        }
                    );
                }
            }
        );

        final String perf_counter_name = new String();

        setContentView(R.layout.activity_main);
    }

    private TextView selectPerfCounterText(String name)
    {
        if(name.endsWith("active"))
        {
            return (TextView)findViewById(R.id.perf_counter_text_threads_active);
        }
        if(name.endsWith("pending"))
        {
            return (TextView)findViewById(R.id.perf_counter_text_threads_pending);
        }
        if(name.endsWith("suspended"))
        {
            return (TextView)findViewById(R.id.perf_counter_text_threads_suspended);
        }
        if(name.endsWith("terminated"))
        {
            return (TextView)findViewById(R.id.perf_counter_text_threads_terminated);
        }

        return null;
    }

    public void togglePerfCounter(View view)
    {
        CheckBox button = (CheckBox)view;
        final String name = button.getText().toString();
        if(button.isChecked())
        {
            runtime.enablePerfCounterUpdate(
                name
              , new HpxCallback() {
                    public final void apply(final String arg) {
                        HelloHpx.this.runOnUiThread(
                            new Runnable() {
                                public final void run() {
                                    TextView tv = selectPerfCounterText(name);
                                
                                    if(tv != null)
                                    {
                                        tv.setText(" : " + arg);
                                    }
                                }
                            }
                        );
                    }
                }
            );
        }
        else
        {
            TextView tv = selectPerfCounterText(name);
            if(tv != null)
            {
                tv.setText(null);
            }
            runtime.disablePerfCounterUpdate(name);
        }
    }

    public void runHelloWorld(View view)
    {
        
        String[] args = {
            "--hpx:threads=2"
          , "--hpx:hpx=192.129.11.13"
          , "--hpx:connect"
          , "--hpx:agas=131.188.33.203"
          , "--hpx:run-hpx-main"
          //, "-Ihpx.logging.level=4"
                /*
          , "--hpx:debug-hpx-log"
          */
        };
        runtime.init(args);

        TextView tv = (TextView)findViewById(R.id.hello_message);
        tv.setText(null);
        runtime.apply("runHelloWorld", "");
    }

    static {
        Runtime.loadLibraries();
        System.loadLibrary("hello_hpx");
    }
}
