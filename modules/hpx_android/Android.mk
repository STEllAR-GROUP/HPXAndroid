
# Copyright (c) 2012 Thomas Heller
#
# Distributed under the Boost Software License, Version 1.0. (See accompanying
# file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)

MY_PATH:=$(call my-dir)
#include $(MY_DIR)/../hpx_common.mk
$(call import-module, hpx)
$(call import-module, distributing_factory)

HPX_INCLUDES+=$(realpath $(MY_PATH))

LOCAL_PATH := $(MY_PATH)

include $(CLEAR_VARS)
LOCAL_MODULE:=hpx_android

LOCAL_SRC_FILES:=src/android/android.cpp
LOCAL_C_INCLUDES:=$(HPX_INCLUDES)

LOCAL_CPPFLAGS:=$(HPX_CPPFLAGS)
LOCAL_CPPFLAGS+=-DHPX_APPLICATION_NAME=hpx_android
LOCAL_CPPFLAGS+=-DHPX_APPLICATION_NAME_STRING=\"hpx_android\"
LOCAL_CPPFLAGS+=-DHPX_APPLICATION_EXPORTS
#LOCAL_CPPFLAGS+=-DBOOST_ENABLE_ASSERT_HANDLER

NDK_TOOLCHAIN_VERSION:=4.6
LOCAL_ARM_NEON:=true
LOCAL_LDLIBS := -fuse-ld=gold -llog

LOCAL_STATIC_LIBRARIES := $(HPX_STATIC_LIBRARIES)
LOCAL_SHARED_LIBRARIES := $(HPX_SHARED_LIBRARIES)
include $(BUILD_SHARED_LIBRARY)

HPX_SHARED_LIBRARIES+=hpx_android

$(call import-module, cpufeatures)
