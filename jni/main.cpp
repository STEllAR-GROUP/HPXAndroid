
//  Copyright (c) 2012 Thomas Heller
//
//  Distributed under the Boost Software License, Version 1.0. (See accompanying
//  file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)

#include <hpx/hpx_fwd.hpp>
#include <hpx/config.hpp>
#include <hpx/include/lcos.hpp>
#include <hpx/lcos/async.hpp>
#include <hpx/include/actions.hpp>

#include <hpx/util/high_resolution_timer.hpp>

#include <hpx/android/android.hpp>

#include <boost/lexical_cast.hpp>

#include <android/log.h>

#define LOG(x...) __android_log_print(ANDROID_LOG_INFO, "HelloHPX", x)

void hello_world(std::size_t i, double now)
{
    hpx::util::high_resolution_timer t(now);
    LOG("inside hpx hello_world...");
    double elapsed_time = t.elapsed();

    std::string s = "Hello World from thread ";
    s += boost::lexical_cast<std::string>(i);
    s += " took ";
    s += boost::lexical_cast<std::string>(elapsed_time);
    s += " seconds.";
    
    hpx::android::apply("setHelloWorldText", s);
}

HPX_PLAIN_ACTION(hello_world, hello_world_action);

void run_hello_world(std::string const &)
{
    const std::size_t N(10);
    LOG("run hello_world functions...");
    for(std::size_t i = 0; i < N; ++i)
    {
        hpx::apply<hello_world_action>(
            hpx::find_here()
          , i
          , hpx::util::high_resolution_timer::now()
        );
    }
}

jint JNI_OnLoad(JavaVM *vm, void*)
{
    LOG("init called...\n");

    JNIEnv *env;
    if(vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK)
    {
        return -1;
    }

    hpx::android::register_callback(
        "runHelloWorld"
      , HPX_STD_BIND(
            run_hello_world
          , HPX_STD_PLACEHOLDERS::_1
        )
    );

    return JNI_VERSION_1_6;
}
