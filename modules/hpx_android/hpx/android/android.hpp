
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

    HPX_API_EXPORT bool register_callback(std::string const & name, HPX_STD_FUNCTION<void(std::string const &)> const & callback);

    HPX_API_EXPORT void apply(std::string const & method, std::string const & arg);
}}

#endif
