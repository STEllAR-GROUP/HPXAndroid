
//  Copyright (c) 2012 Thomas Heller
//
//  Distributed under the Boost Software License, Version 1.0. (See accompanying
//  file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)

#ifndef HPX_ANDROID_ANDROID_HPP
#define HPX_ANDROID_ANDROID_HPP

#include <hpx/config.hpp>

#include <string>

#include <jni.h>

namespace hpx { namespace android {

    namespace detail
    {
        template <typename T>
        struct callback_type
        {
            typedef HPX_STD_FUNCTION<void(T const &)> type;
        };
        
        template <>
        struct callback_type<void>
        {
            typedef HPX_STD_FUNCTION<void()> type;
        };
        
        template <>
        struct callback_type<void(float, float, float, float)>
        {
            typedef HPX_STD_FUNCTION<void(float, float, float, float)> type;
        };
        
        template <>
        struct callback_type<void(std::vector<jbyte> const &, int, int)>
        {
            typedef HPX_STD_FUNCTION<void(std::vector<jbyte> const &, int, int)> type;
        };
        
        template <typename T>
        HPX_API_EXPORT bool register_callback_impl(std::string const & name, typename callback_type<T>::type const & callback);
    }
    

    template <typename T, typename F>
    inline bool register_callback(std::string const & name, F const & callback)
    {
        return detail::register_callback_impl<T>(name, callback);
    }

    void new_action(std::string const & act);
    void new_action(std::string const & act, std::string const & arg);
    void new_action(std::string const & act, std::vector<jbyte> const & arg, int, int);
    void new_action(std::string const & act, float, float, float, float);

    HPX_API_EXPORT void apply(std::string const & method, std::string const & arg);
}}

#endif
