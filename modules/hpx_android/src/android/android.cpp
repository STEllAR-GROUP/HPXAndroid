
//  Copyright (c) 2012 Thomas Heller
//
//  Distributed under the Boost Software License, Version 1.0. (See accompanying
//  file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)

#include <hpx/config.hpp>
#include <hpx/hpx.hpp>
#include <hpx/hpx_start.hpp>
#include <hpx/include/lcos.hpp>
#include <hpx/include/actions.hpp>
#include <hpx/apply.hpp>
#include <hpx/android/android.hpp>
#include <hpx/util/static.hpp>
#include <hpx/util/interval_timer.hpp>
#include <hpx/lcos/local/spinlock.hpp>

#include <boost/shared_ptr.hpp>

#include <string>
#include <map>

#include <jni.h>
#include <cpu-features.h>

#include <android/log.h>

#define D(x...) __android_log_print(ANDROID_LOG_INFO, "hpx.android", "%s", x)

namespace hpx { namespace android {
    struct android_support
        : boost::noncopyable
    {
        typedef
            hpx::lcos::local::spinlock
            mutex_type;

        typedef HPX_STD_FUNCTION<void(std::string const &)> callback_type;

        typedef 
            std::map<std::string, callback_type>
            callbacks_type;
        
        void reset()
        {
            mutex_type::scoped_lock l1(callbacks_mutex);
            mutex_type::scoped_lock l2(actions_mutex);

            actions.clear();
            callbacks.clear();
        }

        bool register_callback(std::string const & name, callback_type const & f)
        {
            mutex_type::scoped_lock lk(callbacks_mutex);
            callbacks_type::iterator it = callbacks.find(name);

            std::string tmp = "register callback: ";
            tmp += name;
            D(name.c_str());

            if(it != callbacks.end())
                return false;

            callbacks.insert(it, std::make_pair(name, f));

            return true;
        };

        void new_action(std::string const & action, std::string const & arg)
        {
            std::string msg = "new_action: ";
            msg += action;
            msg += " ";
            msg += arg;
            D(msg.c_str());
            {
                mutex_type::scoped_lock lk(actions_mutex);
                actions.push_back(std::make_pair(action, arg));
            }
        }

        std::pair<callback_type, std::string> next_action()
        {
            std::pair<callback_type, std::string> res;

            std::pair<std::string, std::string> action;
            {
                hpx::lcos::local::spinlock::scoped_lock lk(actions_mutex);
                if(!actions.empty())
                {
                    action = actions.back();
                    actions.pop_back();
                }
            }
            
            if(!action.first.empty())
            {
                mutex_type::scoped_lock lk(callbacks_mutex);
                callbacks_type::iterator it = callbacks.find(action.first);

                std::string msg = "next_action: ";
                msg += action.first;
                msg += " ";
                if(it != callbacks.end())
                {
                    res.first = it->second;
                    res.second = action.second;
                    msg += "found!";
                }
                else
                {
                    msg += "not found! (entries in map: ";
                    mutex_type::scoped_lock lk(callbacks_mutex);
                    typedef std::pair<std::string, callback_type> pair_type;
                    BOOST_FOREACH(pair_type const & a, callbacks)
                    {
                        msg += a.first;
                        msg += " ";
                    }
                    msg += ")";
                }
                D(msg.c_str());
            }

            return res;
        }

        void apply(std::string const & method, std::string const & arg)
        {
            JNIEnv *env;
            vm->AttachCurrentThread(&env, 0);

            env->PushLocalFrame(256);
            {
                //D("inside android_callback...");
                jclass thiz_class = env->GetObjectClass(thiz);
                
                jstring jmethod = env->NewStringUTF(method.c_str());
                jstring jarg    = env->NewStringUTF(arg.c_str());
                
                jmethodID mid = env->GetMethodID(thiz_class, "callback", "(Ljava/lang/String;Ljava/lang/String;)Z");
                env->CallVoidMethod(thiz, mid, jmethod, jarg);
                 /*   
                env->DeleteLocalRef(thiz_class);
                env->DeleteLocalRef(jmethod);
                env->DeleteLocalRef(jarg);
                */
            }
            env->PopLocalFrame(0);
        }

        jobject thiz;
        JavaVM *vm;

        mutex_type actions_mutex;
        std::vector<std::pair<std::string, std::string> > actions;

        mutex_type callbacks_mutex;
        callbacks_type callbacks;
    };

    android_support & get_android_support()
    {
        hpx::util::static_<android_support> as;

        return as.get();
    }

    HPX_API_EXPORT bool register_callback(std::string const & name, HPX_STD_FUNCTION<void(std::string const &)> const & f)
    {
        return get_android_support().register_callback(name, f);
    }

    HPX_API_EXPORT void apply(std::string const & method, std::string const & arg)
    {
        hpx::util::io_service_pool *io_pool =
            hpx::get_runtime().get_thread_pool("io_pool");
        
        io_pool->get_io_service().post(
            HPX_STD_BIND(
                &android_support::apply
              , HPX_STD_REF(get_android_support())
              , method
              , arg
            )
        );
    }

    hpx::lcos::promise<void> & finish_promise()
    {
        hpx::util::static_<hpx::lcos::promise<void> > fp;
        return fp.get();
    }

    hpx::lcos::future<void> & finish()
    {
        hpx::util::static_<hpx::lcos::future<void> > f(finish_promise().get_future());
        return f.get();
    }

    bool & started()
    {
        hpx::util::static_<bool> started_flag(false);
        return started_flag.get();
    }

    hpx::lcos::local::spinlock & perf_counter_mtx()
    {
        hpx::util::static_<hpx::lcos::local::spinlock> mtx;
        return mtx.get();
    }

    std::map<std::string, boost::shared_ptr<hpx::util::interval_timer> > & perf_counter()
    {
        hpx::util::static_<std::map<std::string, boost::shared_ptr<hpx::util::interval_timer> > > pm;
        return pm.get();
    }

    bool get_perf_counter_val(hpx::naming::id_type id, std::string const & name)
    {
        using hpx::performance_counters::counter_value;
        using hpx::performance_counters::stubs::performance_counter;

        counter_value value = performance_counter::get_value(id);
        
        std::string result = boost::lexical_cast<std::string>(value.get_value<double>());
        
        
        hpx::android::apply(name, result);

        return true;
    }

    void enable_perf_counter(std::string const & name)
    {
        typedef std::map<std::string, boost::shared_ptr<hpx::util::interval_timer> > perf_counter_map;

        hpx::lcos::local::spinlock::scoped_lock lk(perf_counter_mtx());

        perf_counter_map::iterator it = perf_counter().find(name);

        if(it == perf_counter().end())
        {
            hpx::naming::id_type id = hpx::performance_counters::get_counter(name);

            std::string desc = "get_perf_counter_val: ";
            desc += name;
            boost::shared_ptr<hpx::util::interval_timer>
                timer(
                    new hpx::util::interval_timer(
                        boost::bind(
                            get_perf_counter_val
                          , id
                          , name
                        )
                      , 2500000
                      , desc
                    )
                );

            perf_counter().insert(it, std::make_pair(name, timer));
            timer->start();
        }
    }

    void disable_perf_counter(std::string const & name)
    {
        typedef std::map<std::string, boost::shared_ptr<hpx::util::interval_timer> > perf_counter_map;

        hpx::lcos::local::spinlock::scoped_lock lk(perf_counter_mtx());

        perf_counter_map::iterator it = perf_counter().find(name);

        if(it != perf_counter().end())
        {
            it->second->stop();
            perf_counter().erase(it);
        }
    }
    

    bool start(JNIEnv * env, jobject thiz, int argc = 0, char **argv = 0)
    {
        std::string threads = "Number of threads: ";
        threads += boost::lexical_cast<std::string>(android_getCpuCount());

        D(threads.c_str());
        
        hpx::android::android_support & as = hpx::android::get_android_support();

        env->GetJavaVM(&as.vm);
        as.thiz = env->NewGlobalRef(thiz);
        // We start HPX here ...
        // TODO: add command line parameters and runtime configuration options here
        if(!started())
        {
            hpx::android::register_callback(
                "enablePerfCounter"
              , HPX_STD_BIND(
                    enable_perf_counter
                  , HPX_STD_PLACEHOLDERS::_1
                )
            );
            hpx::android::register_callback(
                "disablePerfCounter"
              , HPX_STD_BIND(
                    disable_perf_counter
                  , HPX_STD_PLACEHOLDERS::_1
                )
            );
            hpx::start(argc, argv);
            started() = true;
        }
        D("started");

        return true;
    }

    hpx::threads::thread_state_enum thread_function(HPX_STD_FUNCTION<void()> const & f)
    {
        f();
        return hpx::threads::terminated;
    }

    bool run_callbacks()
    {
        typedef hpx::android::android_support::callback_type callback_type;
        std::pair<callback_type, std::string> f = hpx::android::get_android_support().next_action();
        if(f.first)
        {
            // FIXME: we should really use apply here, unfortunately, that makes the HPX threads hang.
            //hpx::apply(HPX_STD_BIND(f.first, f.second));
            hpx::threads::thread_init_data data(
                HPX_STD_BIND(thread_function, HPX_STD_PROTECT(HPX_STD_BIND(f.first, f.second)))
              , ""
            );
            hpx::get_runtime().get_thread_manager().
                register_thread(data, hpx::threads::pending);
        }

        return true;
    }

}}

int hpx_main(boost::program_options::variables_map&)
{
    {
        D("in hpx main ...");

        hpx::util::interval_timer
            callback_timer(
                boost::bind(
                    hpx::android::run_callbacks
                )
              , 10000
              , "hpx::android::run_callbacks"
            );

        callback_timer.start();

        hpx::wait(hpx::android::finish());
        
        callback_timer.stop();

        return hpx::finalize();
    }
}

#ifdef __cplusplus
extern "C" {
#endif

// Implementation of native method to start HPX
JNIEXPORT void JNICALL Java_hpx_android_Runtime_init(JNIEnv * env, jobject thiz)
{
    hpx::android::start(env, thiz);
}

JNIEXPORT void JNICALL Java_hpx_android_Runtime_initA(JNIEnv * env, jobject thiz, jobjectArray args)
{
    int argc = 0;
    char **argv = 0;
    if(!hpx::android::started())
    {
        // FIXME: free memory sometimes, maybe?
        argc = env->GetArrayLength(args) + 1;
        argv = (char**)malloc((argc + 1) * sizeof(char *));
        std::vector<jstring> jargv(argc-1);

        argv[0] = const_cast<char*>("hpx.android");

        for(int i = 0; i < argc-1; ++i)
        {
            jargv[i] = static_cast<jstring>(env->NewGlobalRef(env->GetObjectArrayElement(args, i)));
            argv[i+1] = const_cast<char *>(env->GetStringUTFChars(jargv[i], NULL));
            D(argv[i+1]);
        }
        argv[argc] = NULL;
    }

    hpx::android::start(env, thiz, argc, argv);
    /*
    for(std::size_t i = 1; i < argc; ++i)
    {
        env->ReleaseStringUTFChars(argv[i], jargv[i]);
        env->DeleteLocalRef(jargv[i]);
    }
    delete argv;
    */
}

JNIEXPORT void JNICALL Java_hpx_android_Runtime_stop(JNIEnv * env, jobject thiz)
{
    //TODO: properly shutdown hpx
    D("stopped");
    hpx::android::android_support & as = hpx::android::get_android_support();
    env->DeleteGlobalRef(as.thiz);
    /*
    as.reset();
    */
}

JNIEXPORT void JNICALL Java_hpx_android_Runtime_apply(JNIEnv * env, jobject thiz, jstring action, jstring arg)
{

    // FIXME: memory leak when exceptions occur

    const char * action_cstr = env->GetStringUTFChars(action, NULL);
    const char * arg_cstr = env->GetStringUTFChars(arg, NULL);
    
    D("apply got:");
    D(action_cstr);
    D(arg_cstr);

    std::string action_str(action_cstr);
    std::string arg_str(arg_cstr);

    env->ReleaseStringUTFChars(action, action_cstr);
    env->ReleaseStringUTFChars(arg, arg_cstr);

    hpx::android::get_android_support().new_action(action_str, arg_str);
}

#ifdef __cplusplus
}
#endif
