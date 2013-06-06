
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

        typedef HPX_STD_FUNCTION<void(std::string const &)> string_callback_type;
        typedef HPX_STD_FUNCTION<void(std::vector<jbyte> const &, int, int)> vector_byte_callback_type;
        typedef HPX_STD_FUNCTION<void()> void_callback_type;
        typedef HPX_STD_FUNCTION<void(float, float, float, float)> float4_callback_type;

        typedef 
            std::map<std::string, string_callback_type>
            string_callbacks_type;

        typedef 
            std::map<std::string, vector_byte_callback_type>
            vector_byte_callbacks_type;

        typedef 
            std::map<std::string, void_callback_type>
            void_callbacks_type;

        typedef 
            std::map<std::string, float4_callback_type>
            float4_callbacks_type;

        android_support()
            : runtime_started(false)
        {
            pthread_mutex_init(&runtime_started_mtx, NULL);
            pthread_cond_init(&runtime_started_cond, NULL);
        }
        
        void reset()
        {
            mutex_type::scoped_lock l1(string_callbacks_mutex);
            mutex_type::scoped_lock l2(vector_byte_callbacks_mutex);
            mutex_type::scoped_lock l3(void_callbacks_mutex);
            mutex_type::scoped_lock l4(string_actions_mutex);
            mutex_type::scoped_lock l5(vector_byte_actions_mutex);
            mutex_type::scoped_lock l6(void_actions_mutex);
            mutex_type::scoped_lock l7(float4_actions_mutex);

            string_actions.clear();
            vector_byte_actions.clear();
            void_actions.clear();
            float4_actions.clear();
            string_callbacks.clear();
            vector_byte_callbacks.clear();
            void_callbacks.clear();
            float4_callbacks.clear();
        }

        bool register_callback(std::string const & name, string_callback_type const & f)
        {
            mutex_type::scoped_lock lk(string_callbacks_mutex);
            string_callbacks_type::iterator it = string_callbacks.find(name);

            std::string tmp = "register string callback: ";
            tmp += name;
            D(name.c_str());

            if(it != string_callbacks.end())
                return false;

            string_callbacks.insert(it, std::make_pair(name, f));

            return true;
        };

        bool register_callback(std::string const & name, vector_byte_callback_type const & f)
        {
            mutex_type::scoped_lock lk(vector_byte_callbacks_mutex);
            vector_byte_callbacks_type::iterator it = vector_byte_callbacks.find(name);

            std::string tmp = "register vector<byte> callback: ";
            tmp += name;
            D(name.c_str());

            if(it != vector_byte_callbacks.end())
                return false;

            vector_byte_callbacks.insert(it, std::make_pair(name, f));

            return true;
        };

        bool register_callback(std::string const & name, void_callback_type const & f)
        {
            mutex_type::scoped_lock lk(void_callbacks_mutex);
            void_callbacks_type::iterator it = void_callbacks.find(name);

            std::string tmp = "register void callback: ";
            tmp += name;
            D(name.c_str());

            if(it != void_callbacks.end())
                return false;

            void_callbacks.insert(it, std::make_pair(name, f));

            return true;
        };

        bool register_callback(std::string const & name, float4_callback_type const & f)
        {
            mutex_type::scoped_lock lk(float4_callbacks_mutex);
            float4_callbacks_type::iterator it = float4_callbacks.find(name);

            std::string tmp = "register float4 callback: ";
            tmp += name;
            D(name.c_str());

            if(it != float4_callbacks.end())
                return false;

            float4_callbacks.insert(it, std::make_pair(name, f));

            return true;
        };

        void new_action(std::string const & action, float f0, float f1, float f2, float f3)
        {
            std::string msg = "new_float4_action: ";
            msg += action;
            msg += " ";
            msg += boost::lexical_cast<std::string>(f0);
            msg += " ";
            msg += boost::lexical_cast<std::string>(f1);
            msg += " ";
            msg += boost::lexical_cast<std::string>(f2);
            msg += " ";
            msg += boost::lexical_cast<std::string>(f3);
            D(msg.c_str());
            {
                mutex_type::scoped_lock lk(float4_actions_mutex);
                boost::array<float, 4> args = {{f0, f1, f2, f3}};
                float4_actions.push_back(std::make_pair(action, args));
            }
        }

        void new_action(std::string const & action, std::string const & arg)
        {
            std::string msg = "new_string_action: ";
            msg += action;
            msg += " ";
            msg += arg;
            D(msg.c_str());
            {
                mutex_type::scoped_lock lk(string_actions_mutex);
                string_actions.push_back(std::make_pair(action, arg));
            }
        }

        void new_action(std::string const & action, HPX_STD_TUPLE<std::vector<jbyte>, int, int> const & arg)
        {
            std::string msg = "new_vector_byte_action: ";
            msg += action;
            D(msg.c_str());
            {
                mutex_type::scoped_lock lk(vector_byte_actions_mutex);
                vector_byte_actions.push_back(std::make_pair(action, arg));
            }
        }

        void new_action(std::string const & action)
        {
            std::string msg = "new_void_action: ";
            msg += action;
            D(msg.c_str());
            {
                mutex_type::scoped_lock lk(void_actions_mutex);
                void_actions.push_back(action);
            }
        }

        std::pair<string_callback_type, std::string> next_string_action()
        {
            std::pair<string_callback_type, std::string> res;

            std::pair<std::string, std::string> action;
            {
                hpx::lcos::local::spinlock::scoped_lock lk(string_actions_mutex);
                if(!string_actions.empty())
                {
                    action = string_actions.back();
                    string_actions.pop_back();
                }
            }
            
            if(!action.first.empty())
            {
                mutex_type::scoped_lock lk(string_callbacks_mutex);
                string_callbacks_type::iterator it = string_callbacks.find(action.first);

                std::string msg = "next_string_action: ";
                msg += action.first;
                msg += " ";
                if(it != string_callbacks.end())
                {
                    res.first = it->second;
                    res.second = action.second;
                    msg += "found!";
                }
                else
                {
                    msg += "not found! (entries in map: ";
                    typedef std::pair<std::string, string_callback_type> pair_type;
                    BOOST_FOREACH(pair_type const & a, string_callbacks)
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

        std::pair<vector_byte_callback_type, HPX_STD_TUPLE<std::vector<jbyte>, int, int> > next_vector_byte_action()
        {
            std::pair<vector_byte_callback_type, HPX_STD_TUPLE<std::vector<jbyte>, int, int> > res;

            std::pair<std::string, HPX_STD_TUPLE<std::vector<jbyte>, int, int> > action;
            {
                hpx::lcos::local::spinlock::scoped_lock lk(vector_byte_actions_mutex);
                if(!vector_byte_actions.empty())
                {
                    action = vector_byte_actions.back();
                    vector_byte_actions.pop_back();
                }
            }
            
            if(!action.first.empty())
            {
                mutex_type::scoped_lock lk(vector_byte_callbacks_mutex);
                vector_byte_callbacks_type::iterator it = vector_byte_callbacks.find(action.first);

                std::string msg = "next_vector_byte_action: ";
                msg += action.first;
                msg += " ";
                if(it != vector_byte_callbacks.end())
                {
                    res.first = it->second;
                    res.second = action.second;
                    msg += "found!";
                }
                else
                {
                    msg += "not found! (entries in map: ";
                    typedef std::pair<std::string, vector_byte_callback_type> pair_type;
                    BOOST_FOREACH(pair_type const & a, vector_byte_callbacks)
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

        std::pair<float4_callback_type, boost::array<float, 4> > next_float4_action()
        {
            std::pair<float4_callback_type, boost::array<float, 4> > res;

            std::pair<std::string, boost::array<float, 4> > action;
            {
                hpx::lcos::local::spinlock::scoped_lock lk(float4_actions_mutex);
                if(!float4_actions.empty())
                {
                    action = float4_actions.back();
                    float4_actions.pop_back();
                }
            }
            
            if(!action.first.empty())
            {
                mutex_type::scoped_lock lk(float4_callbacks_mutex);
                float4_callbacks_type::iterator it = float4_callbacks.find(action.first);

                std::string msg = "next_float4_action: ";
                msg += action.first;
                msg += " ";
                if(it != float4_callbacks.end())
                {
                    res.first = it->second;
                    res.second = action.second;
                    msg += "found!";
                }
                else
                {
                    msg += "not found! (entries in map: ";
                    typedef std::pair<std::string, float4_callback_type> pair_type;
                    BOOST_FOREACH(pair_type const & a, float4_callbacks)
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

        void_callback_type next_void_action()
        {
            void_callback_type res;

            std::string action;
            {
                hpx::lcos::local::spinlock::scoped_lock lk(void_actions_mutex);
                if(!void_actions.empty())
                {
                    action = void_actions.back();
                    void_actions.pop_back();
                }
            }
            
            if(!action.empty())
            {
                mutex_type::scoped_lock lk(void_callbacks_mutex);
                void_callbacks_type::iterator it = void_callbacks.find(action);

                std::string msg = "next_void_action: ";
                msg += action;
                msg += " ";
                if(it != void_callbacks.end())
                {
                    res = it->second;
                    msg += "found!";
                }
                else
                {
                    msg += "not found! (entries in map: ";
                    typedef std::pair<std::string,  void_callback_type> pair_type;
                    BOOST_FOREACH(pair_type const & a, void_callbacks)
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

        int get_num_localities()
        {
            D("get_num_localities");
            pthread_mutex_lock(&runtime_started_mtx);
            while(!runtime_started)
            {
                D("get_num_localities waiting ...");
                pthread_cond_wait(&runtime_started_cond, &runtime_started_mtx);
            }
            pthread_mutex_unlock(&runtime_started_mtx);
            
            D("get_num_localities returning ...");

            return num_localities;
        }

        std::vector<int> const & get_num_threads()
        {
            D("get_num_threads");
            pthread_mutex_lock(&runtime_started_mtx);
            while(!runtime_started)
            {
                D("get_num_threads waiting ...");
                pthread_cond_wait(&runtime_started_cond, &runtime_started_mtx);
            }
            pthread_mutex_unlock(&runtime_started_mtx);
            D("get_num_threads returning ...");

            return num_threads;
        }

        void set_stuff()
        {
            D("set_stuff");
            num_localities = hpx::get_num_localities();
            std::vector<boost::uint32_t> unum_threads = hpx::agas::get_num_threads();
            num_threads.assign(unum_threads.begin(), unum_threads.end());// = hpx::get_num_threads();

            pthread_mutex_lock(&runtime_started_mtx);
            runtime_started = true;
            D("set_stuff signalling");
            pthread_cond_signal(&runtime_started_cond);
            pthread_mutex_unlock(&runtime_started_mtx);
        }

        jobject thiz;
        JavaVM *vm;

        mutex_type string_actions_mutex;
        std::vector<std::pair<std::string, std::string> > string_actions;

        mutex_type vector_byte_actions_mutex;
        std::vector<std::pair<std::string, HPX_STD_TUPLE<std::vector<jbyte>, int, int> > > vector_byte_actions;

        mutex_type float4_actions_mutex;
        std::vector<std::pair<std::string, boost::array<float, 4> > > float4_actions;

        mutex_type void_actions_mutex;
        std::vector<std::string> void_actions;

        mutex_type string_callbacks_mutex;
        string_callbacks_type string_callbacks;

        mutex_type vector_byte_callbacks_mutex;
        vector_byte_callbacks_type vector_byte_callbacks;

        mutex_type float4_callbacks_mutex;
        float4_callbacks_type float4_callbacks;
        
        mutex_type void_callbacks_mutex;
        void_callbacks_type void_callbacks;

        pthread_mutex_t runtime_started_mtx;
        pthread_cond_t runtime_started_cond;
        bool runtime_started;
        int num_localities;
        std::vector<int> num_threads;

        int argc;
        char **argv;
        std::vector<jstring> jargv;
    };

    android_support & get_android_support()
    {
        hpx::util::static_<android_support> as;

        return as.get();
    }

    namespace detail
    {
        template <typename T>
        HPX_API_EXPORT bool register_callback_impl(std::string const & name, typename callback_type<T>::type const & callback)
        {
            return get_android_support().register_callback(name, callback);
        }

        template
        HPX_API_EXPORT bool register_callback_impl<std::string>(std::string const &, HPX_STD_FUNCTION<void(std::string const &)> const &);
        template
        HPX_API_EXPORT bool register_callback_impl<void(std::vector<jbyte> const &, int, int)>(std::string const &, HPX_STD_FUNCTION<void(std::vector<jbyte> const &, int, int)> const &);
        template
        HPX_API_EXPORT bool register_callback_impl<void>(std::string const &, HPX_STD_FUNCTION<void()> const &);
        
        template
        HPX_API_EXPORT bool register_callback_impl<void(float, float, float, float)>(std::string const &, HPX_STD_FUNCTION<void(float, float, float, float)> const &);
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
        hpx::util::static_<hpx::lcos::future<void> > f;
        return f.get();
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
        if(finish().ready())
        {
            return false;
        }
        {
            typedef std::map<std::string, boost::shared_ptr<hpx::util::interval_timer> > perf_counter_map;
            hpx::lcos::local::spinlock::scoped_lock lk(perf_counter_mtx());
            perf_counter_map::iterator it = perf_counter().find(name);
            if(it == perf_counter().end())
            {
                return false;
            }
        }

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
        D("enabling perf counter");

        perf_counter_map::iterator it = perf_counter().find(name);

        if(it == perf_counter().end())
        {
            hpx::naming::id_type id;
            boost::shared_ptr<hpx::util::interval_timer>
                timer;
            {
                hpx::util::unlock_the_lock<
                    hpx::lcos::local::spinlock::scoped_lock
                > ull(lk);

                id = hpx::performance_counters::get_counter(name);

                std::string desc = "get_perf_counter_val: ";
                desc += name;
                timer.reset(
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
                timer->start();
            }

            perf_counter().insert(perf_counter().end(), std::make_pair(name, timer));
        }
    }

    void disable_perf_counter(std::string const & name)
    {
        typedef std::map<std::string, boost::shared_ptr<hpx::util::interval_timer> > perf_counter_map;

        hpx::lcos::local::spinlock::scoped_lock lk(perf_counter_mtx());

        perf_counter_map::iterator it = perf_counter().find(name);

        if(it != perf_counter().end())
        {
            boost::shared_ptr<hpx::util::interval_timer> t = it->second;
            perf_counter().erase(it);
            t->stop();
        }
    }

    void clear_perf_counters()
    {
        typedef std::map<std::string, boost::shared_ptr<hpx::util::interval_timer> > perf_counter_map;
        typedef std::pair<std::string, boost::shared_ptr<hpx::util::interval_timer> > pair_type;
        
        hpx::lcos::local::spinlock::scoped_lock lk(perf_counter_mtx());
        BOOST_FOREACH(pair_type const & p, perf_counter())
        {
            p.second->stop();
        }
        perf_counter_map tmp;
        std::swap(tmp, perf_counter());
    }
    

    bool start(JNIEnv * env, jobject thiz, int argc = 0, char **argv = 0, std::vector<jstring> const & jargv = std::vector<jstring>())
    {
        std::string threads = "Number of threads: ";
        threads += boost::lexical_cast<std::string>(android_getCpuCount());

        D(threads.c_str());
    
        hpx::android::finish_promise().reset();
        
        hpx::android::android_support & as = hpx::android::get_android_support();
        as.argc = argc;
        as.argv = argv;
        as.jargv = jargv;

        env->GetJavaVM(&as.vm);
        as.thiz = env->NewGlobalRef(thiz);
        // We start HPX here ...
        // TODO: add command line parameters and runtime configuration options here
        hpx::android::register_callback<std::string>(
            "enablePerfCounter"
          , HPX_STD_BIND(
                enable_perf_counter
              , HPX_STD_PLACEHOLDERS::_1
            )
        );
        hpx::android::register_callback<std::string>(
            "disablePerfCounter"
          , HPX_STD_BIND(
                disable_perf_counter
              , HPX_STD_PLACEHOLDERS::_1
            )
        );
        hpx::start(argc, argv);
        D("started");

        return true;
    }

    hpx::threads::thread_state_enum thread_function(HPX_STD_FUNCTION<void()> const & f)
    {
        f();
        return hpx::threads::terminated;
    }

    bool run_string_callbacks()
    {
        if(finish().ready())
        {
            return false;
        }
        typedef hpx::android::android_support::string_callback_type string_callback_type;
        {
            std::pair<string_callback_type, std::string> f;
            do
            {
                f = hpx::android::get_android_support().next_string_action();
                if(f.first)
                {
                    // FIXME: we should really use apply here, unfortunately, that makes the HPX threads hang.
                    //hpx::apply(HPX_STD_BIND(f.first, f.second));
                    //f.first(f.second);
                    hpx::threads::thread_init_data data(
                        HPX_STD_BIND(thread_function, HPX_STD_PROTECT(HPX_STD_BIND(f.first, f.second)))
                      , ""
                    );
                    hpx::get_runtime().get_thread_manager().
                        register_thread(data, hpx::threads::pending);
                }
            } while(f.first);
        }

        return true;
    }

    bool run_vector_byte_callbacks()
    {
        if(finish().ready())
        {
            return false;
        }
        typedef hpx::android::android_support::vector_byte_callback_type vector_byte_callback_type;
        {
            std::pair<vector_byte_callback_type, HPX_STD_TUPLE<std::vector<jbyte>, int, int> > f;
            do
            {
                f = hpx::android::get_android_support().next_vector_byte_action();
                if(f.first)
                {
                    // FIXME: we should really use apply here, unfortunately, that makes the HPX threads hang.
                    //hpx::apply(HPX_STD_BIND(f.first, f.second));
                    //f.first(f.second);
                    hpx::threads::thread_init_data data(
                        HPX_STD_BIND(
                            thread_function
                          , HPX_STD_PROTECT(
                                HPX_STD_BIND(
                                    f.first
                                  , HPX_STD_GET(0, f.second)
                                  , HPX_STD_GET(1, f.second)
                                  , HPX_STD_GET(2, f.second)
                                )
                            )
                        )
                      , ""
                    );
                    hpx::get_runtime().get_thread_manager().
                        register_thread(data, hpx::threads::pending);
                }
            } while(f.first);
        }

        return true;
    }

    bool run_float4_callbacks()
    {
        if(finish().ready())
        {
            return false;
        }
        typedef hpx::android::android_support::float4_callback_type float4_callback_type;
        {
            std::pair<float4_callback_type, boost::array<float, 4> > f;
            do {
                f = hpx::android::get_android_support().next_float4_action();
                if(f.first)
                {
                    // FIXME: we should really use apply here, unfortunately, that makes the HPX threads hang.
                    //hpx::apply(HPX_STD_BIND(f.first, f.second));
                    //f.first(f.second);
                    hpx::threads::thread_init_data data(
                        HPX_STD_BIND(thread_function, HPX_STD_PROTECT(HPX_STD_BIND(f.first, f.second[0], f.second[1], f.second[2], f.second[3])))
                      , ""
                    );
                    hpx::get_runtime().get_thread_manager().
                        register_thread(data, hpx::threads::pending);
                }
            } while(f.first);
        }

        return true;
    }
        
    bool run_void_callbacks()
    {
        if(finish().ready())
        {
            return false;
        }
        typedef hpx::android::android_support::void_callback_type void_callback_type;
        {
            void_callback_type f;
            do 
            {
                f = hpx::android::get_android_support().next_void_action();
                if(f)
                {
                    // FIXME: we should really use apply here, unfortunately, that makes the HPX threads hang.
                    //hpx::apply(HPX_STD_BIND(f.first, f.second));
                    hpx::threads::thread_init_data data(
                        HPX_STD_BIND(thread_function, f)
                      , ""
                    );
                    hpx::get_runtime().get_thread_manager().
                        register_thread(data, hpx::threads::pending);
                    //f();
                }
            } while(f);
        }

        return true;
    }

    void new_action(std::string const & act)
    {
        hpx::android::get_android_support().new_action(act);
    }
    void new_action(std::string const & act, std::string const & arg)
    {
        hpx::android::get_android_support().new_action(act, arg);
    }
    void new_action(std::string const & act, HPX_STD_TUPLE<std::vector<jbyte>, int, int> const & arg)
    {
        hpx::android::get_android_support().new_action(act, arg);
    }
    void new_action(std::string const & act, float f1, float f2, float f3, float f4)
    {
        hpx::android::get_android_support().new_action(act, f1, f2, f3, f4);
    }

}}

int hpx_main(boost::program_options::variables_map&)
{
    {
        D("in hpx main ...");
        hpx::android::finish() = hpx::android::finish_promise().get_future();
        hpx::util::interval_timer
            string_callback_timer(
                boost::bind(
                    hpx::android::run_string_callbacks
                )
              , 25000
              , "hpx::android::run_string_callbacks"
            );

        string_callback_timer.start();
        
        hpx::util::interval_timer
            vector_byte_callback_timer(
                boost::bind(
                    hpx::android::run_vector_byte_callbacks
                )
              , 25010
              , "hpx::android::run_vector_byte_callbacks"
            );

        vector_byte_callback_timer.start();
        
        hpx::util::interval_timer
            float4_callback_timer(
                boost::bind(
                    hpx::android::run_float4_callbacks
                )
              , 25020
              , "hpx::android::run_float4_callbacks"
            );

        float4_callback_timer.start();

        hpx::util::interval_timer
            void_callback_timer(
                boost::bind(
                    hpx::android::run_void_callbacks
                )
              , 25030
              , "hpx::android::run_void_callbacks"
            );

        void_callback_timer.start();

        hpx::android::get_android_support().set_stuff();

        hpx::wait(hpx::android::finish());
        
        string_callback_timer.stop();
        void_callback_timer.stop();

        D("finalizing");
        return hpx::finalize();
    }
}

#ifdef __cplusplus
extern "C" {
#endif

// Implementation of native method to start HPX
JNIEXPORT void JNICALL Java_hpx_android_Runtime_initE(JNIEnv * env, jobject thiz)
{
    hpx::android::start(env, thiz);
}

JNIEXPORT void JNICALL Java_hpx_android_Runtime_initA(JNIEnv * env, jobject thiz, jobjectArray args)
{
    int argc = 0;
    char **argv = 0;
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

    hpx::android::start(env, thiz, argc, argv, jargv);
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

    hpx::android::clear_perf_counters();
    hpx::android::finish_promise().set_value();

    hpx::stop();

    if(as.argv != 0)
    {
        for(int i = 1; i < as.argc; ++i)
        {
            env->DeleteGlobalRef(as.jargv[i-1]);
            free(as.argv[i]);
        }

        free(as.argv);
    }
    
    D("stopped for real");

    as.reset();
}

JNIEXPORT void JNICALL Java_hpx_android_Runtime_applyS(JNIEnv * env, jobject thiz, jstring action, jstring arg)
{

    // FIXME: memory leak when exceptions occur

    const char * action_cstr = env->GetStringUTFChars(action, NULL);
    const char * arg_cstr = env->GetStringUTFChars(arg, NULL);
    
    D("string apply got:");
    D(action_cstr);
    D(arg_cstr);

    std::string action_str(action_cstr);
    std::string arg_str(arg_cstr);

    env->ReleaseStringUTFChars(action, action_cstr);
    env->ReleaseStringUTFChars(arg, arg_cstr);

    hpx::android::get_android_support().new_action(action_str, arg_str);
}

JNIEXPORT void JNICALL Java_hpx_android_Runtime_applyV(JNIEnv * env, jobject thiz, jstring action)
{

    // FIXME: memory leak when exceptions occur

    const char * action_cstr = env->GetStringUTFChars(action, NULL);
    
    D("void apply got:");
    D(action_cstr);

    std::string action_str(action_cstr);

    env->ReleaseStringUTFChars(action, action_cstr);

    hpx::android::get_android_support().new_action(action_str);
}

JNIEXPORT void JNICALL Java_hpx_android_Runtime_applyAB(JNIEnv * env, jobject thiz, jstring action, jbyteArray arg, int width, int height)
{
    // FIXME: memory leak when exceptions occur

    const char * action_cstr = env->GetStringUTFChars(action, NULL);
    
    D("byte[] apply got:");
    D(action_cstr);

    std::string action_str(action_cstr);

    env->ReleaseStringUTFChars(action, action_cstr);

    int length = 0;
    // FIXME: free memory sometimes, maybe?
    length = env->GetArrayLength(arg);
    std::vector<jbyte> arr(length);

    jbyte * data = env->GetByteArrayElements(arg, NULL);
    if(data)
    {
        std::copy(data, data + length, arr.begin());
        env->ReleaseByteArrayElements(arg, data, JNI_ABORT);
    }

    HPX_STD_TUPLE<std::vector<jbyte>, int, int> args(arr, width, height);

    hpx::android::get_android_support().new_action(action_str, args);
}

JNIEXPORT int JNICALL Java_hpx_android_Runtime_getNumLocalities(JNIEnv * env, jobject thiz)
{
    return hpx::android::get_android_support().get_num_localities();
}

JNIEXPORT jintArray JNICALL Java_hpx_android_Runtime_getNumThreads(JNIEnv * env, jobject thiz)
{
    std::vector<int> const & num_threads(hpx::android::get_android_support().get_num_threads());

    jintArray jnum_threads = env->NewIntArray(num_threads.size());

    BOOST_ASSERT(sizeof(int) == sizeof(boost::uint32_t));

    env->SetIntArrayRegion(jnum_threads, 0, num_threads.size(), &(num_threads[0]));

    return jnum_threads;
}

#ifdef __cplusplus
}
#endif
