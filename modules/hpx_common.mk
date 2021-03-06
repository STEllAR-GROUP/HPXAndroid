
# Copyright (c) 2012 Thomas Heller
#
# Distributed under the Boost Software License, Version 1.0. (See accompanying
# file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)

HPX_SRC_ROOT:=$(strip $(HPX_SRC_ROOT))
#/home/heller/programming/boost/trunk

ifndef BOOST_SRC_ROOT
  $(call __ndk_info,ERROR: You HPX_SRC_ROOT not set)
  $(call __ndk_info,Please set HPX_SRC_ROOT to point to your Boost source directory and start again.)
  $(call __ndk_error,Aborting)
endif
include $(realpath $(call my-dir))/boost_common.mk

HPX_CPPFLAGS:=-DBOOST_PARAMETER_MAX_ARITY=7
HPX_CPPFLAGS+=-DHPX_COROUTINE_ARG_MAX=1
HPX_CPPFLAGS+=-DHPX_COROUTINE_NO_SEPARATE_CALL_SITES
HPX_CPPFLAGS+=-DBOOST_LOG_NO_TSS
HPX_CPPFLAGS+=-DBOOST_LOG_NO_TS
HPX_CPPFLAGS+=-DBOOST_BIGINT_HAS_NATIVE_INT64
HPX_CPPFLAGS+=-DBOOST_PLUGIN_PREFIX=hpx
HPX_CPPFLAGS+=-DBOOST_MOVE_USE_STANDARD_LIBRARY_MOVE
HPX_CPPFLAGS+=-DHPX_HAVE_CXX11
HPX_CPPFLAGS+=-DHPX_VERIFY_LOCKS=0
HPX_CPPFLAGS+=-DHPX_THREAD_GUARD_PAGE=1
#HPX_CPPFLAGS+=-DHPX_HAVE_STACKTRACES
HPX_CPPFLAGS+=-DHPX_HAVE_NATIVE_TLS

#HPX_CPPFLAGS+=-DDEBUG
HPX_CPPFLAGS+=-DNDEBUG
#HPX_CPPFLAGS+=-DHPX_BUILD_TYPE=debug
HPX_CPPFLAGS+=-DHPX_BUILD_TYPE=release
HPX_CPPFLAGS+=-DBOOST_DISABLE_ASSERTS
#HPX_CPPFLAGS+=-DBOOST_ENABLE_ASSERTS
#HPX_CPPFLAGS+=-DBOOST_ENABLE_ASSERT_HANDLER

HPX_CPPFLAGS+=-DHPX_UTIL_BIND
HPX_CPPFLAGS+=-DHPX_UTIL_FUNCTION
HPX_CPPFLAGS+=-DHPX_UTIL_TUPLE
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_COPY_DT_NEEDED_ENTRIES
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_RDYNAMIC
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_FPIC
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_STD_CXX0X
HPX_CPPFLAGS+=-DHPX_HAVE_CXX11_RVALUE_REFERENCES
HPX_CPPFLAGS+=-DHPX_HAVE_CXX11_VARIADIC_TEMPLATES
HPX_CPPFLAGS+=-DHPX_HAVE_CXX11_LAMBDAS
HPX_CPPFLAGS+=-DHPX_HAVE_CXX11_AUTO
HPX_CPPFLAGS+=-DHPX_HAVE_CXX11_DECLTYPE
HPX_CPPFLAGS+=-DHPX_HAVE_CXX11_STD_UNIQUE_PTR
HPX_CPPFLAGS+=-DBOOST_LOCKFREE_HAVE_CXX11_STD_UNIQUE_PTR
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WALL
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WNO_STRICT_ALIASING
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WSIGN_PROMO
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WERROR_VLA
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WERROR_RETURN_TYPE
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_FDIAGNOSTICS_SHOW_OPTION
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WNO_UNUSED_BUT_SET_PARAMETER
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WNO_UNUSED_BUT_SET_VARIABLE
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WERROR_UNINITIALIZED
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WNO_SYNC_NAND
#HPX_CPPFLAGS+=-DHPX_HAVE_GNU_ALIGNED_16
#HPX_CPPFLAGS+=-DBOOST_ATOMIC_HAVE_GNU_ALIGNED_16
#HPX_CPPFLAGS+=-DHPX_HAVE_GNU_128BIT_INTEGERS
#HPX_CPPFLAGS+=-DBOOST_ATOMIC_HAVE_GNU_128BIT_INTEGERS
#HPX_CPPFLAGS+=-DHPX_HAVE_RDTSC
#HPX_CPPFLAGS+=-D_GNU_SOURCE
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_PTHREAD
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_FPIC
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WEXTRA
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WTRAMPOLINES
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WNO_UNUSED_PARAMETER
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WNO_IGNORED_QUALIFIERS
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WFORMAT_2
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WNO_FORMAT_NONLITERAL
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WINIT_SELF
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WDOUBLE_PROMOTION
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WCAST_QUAL
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WERROR_TRAMPOLINES
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WERROR_PARENTHESES
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WERROR_REORDER
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WERROR_RETURN_TYPE
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WERROR_SEQUENCE_POINT
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WERROR_UNINITIALIZED
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WERROR_MISSING_FIELD_INITIALIZERS
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WERROR_FORMAT
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WERROR_MISSING_BRACES
HPX_CPPFLAGS+=-DHPX_HAVE_CXX_FLAG_WERROR_SIGN_COMPARE
HPX_CPPFLAGS+=-DHPX_GIT_COMMIT=\"\"
HPX_CPPFLAGS+=-DHPX_COROUTINE_EXPORTS
HPX_CPPFLAGS+=-DHPX_COROUTINE_USE_GENERIC_CONTEXT=1
HPX_CPPFLAGS+=-DHPX_HAVE_GENERIC_CONTEXT_COROUTINES=1

HPX_CPPFLAGS+=-DHPX_PREFIX=\"lib\"
HPX_CPPFLAGS+=-DBOOST_SYSTEM_NO_DEPRECATED
#HPX_CPPFLAGS+=-std=gnu++0x

HPX_CPPFLAGS+=-Wall
HPX_CPPFLAGS+=-Wno-strict-aliasing
HPX_CPPFLAGS+=-Wsign-promo
HPX_CPPFLAGS+=-Werror=vla
HPX_CPPFLAGS+=-Werror=return-type
HPX_CPPFLAGS+=-fdiagnostics-show-option
HPX_CPPFLAGS+=-Wno-unused-but-set-parameter
HPX_CPPFLAGS+=-Wno-unused-but-set-variable
HPX_CPPFLAGS+=-Werror=uninitialized
HPX_CPPFLAGS+=-Wno-sync-nand
HPX_CPPFLAGS+=-Wextra
HPX_CPPFLAGS+=-Wtrampolines
HPX_CPPFLAGS+=-Wno-unused-parameter
HPX_CPPFLAGS+=-Wno-ignored-qualifiers
HPX_CPPFLAGS+=-Wformat=2
HPX_CPPFLAGS+=-Wno-format-nonliteral
HPX_CPPFLAGS+=-Winit-self
HPX_CPPFLAGS+=-Wdouble-promotion
HPX_CPPFLAGS+=-Wcast-qual
HPX_CPPFLAGS+=-Werror=trampolines
HPX_CPPFLAGS+=-Werror=parentheses
HPX_CPPFLAGS+=-Werror=reorder
HPX_CPPFLAGS+=-Werror=return-type
HPX_CPPFLAGS+=-Werror=sequence-point
HPX_CPPFLAGS+=-Werror=uninitialized
HPX_CPPFLAGS+=-Werror=missing-field-initializers
HPX_CPPFLAGS+=-Werror=format
HPX_CPPFLAGS+=-Werror=missing-braces
HPX_CPPFLAGS+=-Werror=sign-compare

HPX_INCLUDES:=$(BOOST_SRC_ROOT)
HPX_INCLUDES+=$(HPX_SRC_ROOT)/external/cache
HPX_INCLUDES+=$(HPX_SRC_ROOT)/external/endian
HPX_INCLUDES+=$(HPX_SRC_ROOT)/external/logging
HPX_INCLUDES+=$(HPX_SRC_ROOT)/external/plugin
HPX_INCLUDES+=$(HPX_SRC_ROOT)/external/backtrace
HPX_INCLUDES+=$(HPX_SRC_ROOT)
HPX_INCLUDES+=$(SNAPPY_SRC_ROOT)
#HPX_INCLUDES+=

HPX_STATIC_LIBRARIES := cpufeatures hpx_init

HPX_SHARED_LIBRARIES := boost_system
HPX_SHARED_LIBRARIES += boost_thread
HPX_SHARED_LIBRARIES += boost_serialization
HPX_SHARED_LIBRARIES += boost_chrono
HPX_SHARED_LIBRARIES += boost_iostreams
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
