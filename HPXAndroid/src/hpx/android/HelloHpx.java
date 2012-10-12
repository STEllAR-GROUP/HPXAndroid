
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

import android.util.Log;

import java.util.HashSet;

public class HelloHpx extends Activity
{
    Runtime runtime = new Runtime();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        runtime.registerCallback(
            new String("setHelloWorldText")
          , new HpxCallback() {
                public final void apply(final String arg) {
                    Log.i("HelloHpx", "setHelloWorldText(apply)");
                    HelloHpx.this.runOnUiThread(
                        new Runnable() {
                            public final void run() {
                                Log.i("HelloHpx", "setHelloWorldText(apply) insided Runnable::run");
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
        

        String[] args = {
            "--hpx:threads=2"
        };
        runtime.init(args);

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
        if(button == null)
        {
            Log.i("HelloHpx", "not a button?!??");
        }
        final String name = button.getText().toString();
        Log.i("HelloHpx", "errr ...?");
        Log.i("HelloHpx", name);
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
        TextView tv = (TextView)findViewById(R.id.hello_message);
        tv.setText(null);
        runtime.apply(new String("runHelloWorld"), "");
    }

    static {
        Runtime.loadLibraries();
        System.loadLibrary("hello_hpx");
    }
}
