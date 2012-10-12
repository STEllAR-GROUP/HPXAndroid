
//  Copyright (c) 2012 Thomas Heller
//
//  Distributed under the Boost Software License, Version 1.0. (See accompanying
//  file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)

package hpx.android;

import android.util.Log;

import java.util.HashMap;

public class Runtime
{
    private HashMap<String, HpxCallback> callbacks = new HashMap<String, HpxCallback>();

    private boolean isInitialized = false;

    public boolean registerCallback(String name, HpxCallback callback)
    {
        if(callbacks.containsKey(name))
        {
            return false;
        }
            
        callbacks.put(name, callback);
        return true;
    }

    public void enablePerfCounterUpdate(String name, HpxCallback callback)
    {
        /*
        if(!isInitialized)
        {
            throw new Exception("Runtime hasn't been initialized yet!");
        }
        */
        registerCallback(name, callback);
        apply("enablePerfCounter", name);
    }

    public void disablePerfCounterUpdate(String name)
    {
        /*
        if(!isInitialized)
        {
            throw new Exception("Runtime hasn't been initialized yet!");
        }
        */
        callbacks.remove(name);
        apply("disablePerfCounter", name);
    }

    private boolean callback(String name, String arg)
    {
        HpxCallback cb = callbacks.get(name);
        Log.i("hpx.android.Runtime", "callback(" + name + ", " + arg + ")");
        if(cb == null)
        {
            return false;
        }
 
        Log.i("hpx.android.Runtime", "callback(" + name + ", " + arg + "): executing ...");

        cb.apply(arg);

        return true;
    }

    public void init(String[] args)
    {
        initA(args);
        isInitialized = true;
    }
    public native void init();
    private native void initA(String[] args);
    public native void stop();
    public native void apply(String action, String name);

    public static void loadLibraries()
    {
        System.loadLibrary("gnustl_shared");
        System.loadLibrary("boost_system");
        System.loadLibrary("boost_thread");
        System.loadLibrary("boost_serialization");
        System.loadLibrary("boost_chrono");
        System.loadLibrary("boost_atomic");
        System.loadLibrary("boost_context");
        System.loadLibrary("boost_regex");
        System.loadLibrary("boost_date_time");
        System.loadLibrary("boost_program_options");
        System.loadLibrary("boost_filesystem");
        System.loadLibrary("hpx_serialization");
        System.loadLibrary("hpx");
        System.loadLibrary("hpx_android");
        //System.loadLibrary("hello_hpx");
    }

    /*
    static
    {
        loadLibraries();
    }
    */
}
