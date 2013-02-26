
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
        registerCallback(name, callback);
        apply("enablePerfCounter", name);
    }

    public void disablePerfCounterUpdate(String name)
    {
        callbacks.remove(name);
        apply("disablePerfCounter", name);
    }

    private boolean callback(String name, String arg)
    {
        HpxCallback cb = callbacks.get(name);
        if(cb == null)
        {
            return false;
        }
 
        Log.i("hpx.android.Runtime", "callback(\"" + name + "\", \"" + arg + "\")");

        cb.apply(arg);

        return true;
    }

    public native int getNumLocalities();

    public native int[] getNumThreads();

    public void apply(String action)
    {
        applyV(action);
    }

    public void apply(String action, String arg)
    {
        applyS(action, arg);
    }

    public void apply(String action, byte[] arg, int width, int height)
    {
        applyAB(action, arg, width, height);
    }

    public void init(String[] args)
    {
        initA(args);
    }
    public void init()
    {
        initE();
    }

    private native void initE();
    private native void initA(String[] args);
    public native void stop();
    private native void applyV(String action);
    private native void applyS(String action, String arg);
    private native void applyAB(String action, byte[] arg, int width, int height);

    public static void loadLibraries()
    {
        System.loadLibrary("gnustl_shared");
        System.loadLibrary("bz2");
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
        System.loadLibrary("boost_iostreams");
        System.loadLibrary("hpx_serialization");
        System.loadLibrary("hpx");
        System.loadLibrary("distributing_factory");
        System.loadLibrary("hpx_android");
    }

    static
    {
        loadLibraries();
    }
}
