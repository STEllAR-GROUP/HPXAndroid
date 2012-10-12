
# Copyright (c) 2012 Thomas Heller
#
# Distributed under the Boost Software License, Version 1.0. (See accompanying
# file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)

THIS_PATH := $(call my-dir)

$(call import-module,hpx_android)

LOCAL_PATH := $(THIS_PATH)

include $(CLEAR_VARS)
LOCAL_MODULE:=hello_hpx
LOCAL_SRC_FILES:=main.cpp

LOCAL_C_INCLUDES:=$(HPX_INCLUDES)

LOCAL_CPPFLAGS:=$(HPX_CPPFLAGS)
LOCAL_CPPFLAGS+=-DHPX_APPLICATION_NAME=hello_hpx
LOCAL_CPPFLAGS+=-DHPX_APPLICATION_STRING=\"hello_hpx\"
LOCAL_CPPFLAGS+=-DHPX_APPLICATION_EXPORTS
LOCAL_CPPFLAGS+=-DBOOST_ENABLE_ASSERT_HANDLER

LOCAL_STATIC_LIBRARIES := $(HPX_STATIC_LIBRARIES)
LOCAL_SHARED_LIBRARIES := $(HPX_SHARED_LIBRARIES)

LOCAL_LDLIBS := -llog
LOCAL_LDLIBS += -fuse-ld=gold

NDK_TOOLCHAIN_VERSION:=4.6
LOCAL_ARM_NEON:=true
LOCAL_CPP_FEATURES:=exceptions rtti
include $(BUILD_SHARED_LIBRARY)

$(call import-module,cpufeatures)
