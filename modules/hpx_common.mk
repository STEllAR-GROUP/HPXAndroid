
# Copyright (c) 2012 Thomas Heller
#
# Distributed under the Boost Software License, Version 1.0. (See accompanying
# file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)

HPX_SRC_ROOT:=$(realpath $(call my-dir)/..)
include $(HPX_SRC_ROOT)/android-project/boost_common.mk

HPX_CPPFLAGS:=-DHPX_COROUTINE_USE_GENERIC_CONTEXT=1
HPX_CPPFLAGS+=-DBOOST_PARAMETER_MAX_ARITY=7
HPX_CPPFLAGS+=-DHPX_COROUTINE_ARG_MAX=1
HPX_CPPFLAGS+=-DHPX_COROUTINE_NO_SEPERATE_CALL_SITES
HPX_CPPFLAGS+=-DBOOST_LOG_NO_TSS
HPX_CPPFLAGS+=-DBOOST_PLUGIN_PREFIX=hpx
HPX_CPPFLAGS+=-DBOOST_MOVE_USE_STANDARD_LIBRARY_MOVE
HPX_CPPFLAGS+=-DHPX_PREFIX=\"libs/armeabi-v7a\"
HPX_CPPFLAGS+=-DHPX_HAVE_CXX11
HPX_CPPFLAGS+=-DHPX_VERIFY_LOCKS=0
HPX_CPPFLAGS+=-DHPX_THREAD_GUARD_PAGE=1
#HPX_CPPFLAGS+=-DHPX_HAVE_STACKTRACE
HPX_CPPFLAGS+=-DHPX_HAVE_NATIVE_TLS
#HPX_CPPFLAGS+=-DNDEBUG
#HPX_CPPFLAGS+=-DBOOST_DISABLE_ASSERTS
HPX_CPPFLAGS+=-DHPX_HAVE_CXX11_DECLTYPE
HPX_CPPFLAGS+=-DHPX_HAVE_CXX11_STD_UNIQUE_PTR
HPX_CPPFLAGS+=-DHPX_HAVE_CXX11_STD_TUPLE
HPX_CPPFLAGS+=-DHPX_UTIL_BIND
HPX_CPPFLAGS+=-DHPX_UTIL_FUNCTION
HPX_CPPFLAGS+=-pthread
HPX_CPPFLAGS+=-std=gnu++0x
HPX_CPPFLAGS+=-DBOOST_SYSTEM_NO_DEPRECATED
HPX_CPPFLAGS+=-Wno-psabi
#HPX_CPPFLAGS+=

HPX_INCLUDES:=$(BOOST_SRC_ROOT)
HPX_INCLUDES+=$(HPX_SRC_ROOT)/external/cache
HPX_INCLUDES+=$(HPX_SRC_ROOT)/external/endian
HPX_INCLUDES+=$(HPX_SRC_ROOT)/external/logging
HPX_INCLUDES+=$(HPX_SRC_ROOT)/external/plugin
HPX_INCLUDES+=$(HPX_SRC_ROOT)/external/backtrace
HPX_INCLUDES+=$(HPX_SRC_ROOT)
#HPX_INCLUDES+=

HPX_STATIC_LIBRARIES := cpufeatures hpx_init

HPX_SHARED_LIBRARIES := boost_system
HPX_SHARED_LIBRARIES += boost_thread
HPX_SHARED_LIBRARIES += boost_serialization
HPX_SHARED_LIBRARIES += boost_chrono
HPX_SHARED_LIBRARIES += boost_atomic
HPX_SHARED_LIBRARIES += boost_context
HPX_SHARED_LIBRARIES += boost_regex
HPX_SHARED_LIBRARIES += boost_date_time
HPX_SHARED_LIBRARIES += boost_program_options
HPX_SHARED_LIBRARIES += boost_filesystem
HPX_SHARED_LIBRARIES += hpx_serialization
HPX_SHARED_LIBRARIES += hpx

#print_var:
#	@echo $(HPX_INCLUDES)
