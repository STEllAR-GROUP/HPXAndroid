
# Copyright (c) 2012 Thomas Heller
#
# Distributed under the Boost Software License, Version 1.0. (See accompanying
# file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)

MY_DIR:=$(call my-dir)
include $(MY_DIR)/../hpx_common.mk

include $(CLEAR_VARS)
LOCAL_MODULE:=hpx_init
LOCAL_CPP_EXTENSION:=.cpp
LOCAL_C_INCLUDES:=$(HPX_INCLUDES)
#LOCAL_EXPORT_C_INCLUDES:=$(LOCAL_C_INCLUDES)
LOCAL_PATH:=$(HPX_SRC_ROOT)
LOCAL_SRC_FILES:=$(wildcard $(HPX_SRC_ROOT)/src/hpx_*.cpp)
LOCAL_SRC_FILES:=$(filter-out $(HPX_SRC_ROOT)/hpx/hpx_init.cpp, $(LOCAL_SRC_FILES))
LOCAL_SRC_FILES:=$(patsubst $(HPX_SRC_ROOT)/%, %, $(LOCAL_SRC_FILES))
LOCAL_SRC_FILES+=src/main.cpp
LOCAL_CPPFLAGS:=$(HPX_CPPFLAGS)
LOCAL_CPPFLAGS+=-DHPX_APPLICATION_EXPORTS
LOCAL_CPPFLAGS+=-DHPX_ACTION_ARGUMENT_LIMIT=4
LOCAL_CPPFLAGS+=-DHPX_FUNCTION_ARGUMENT_LIMIT=7
#LOCAL_CPPFLAGS+=-DBOOST_ENABLE_ASSERT_HANDLER
NDK_TOOLCHAIN_VERSION:=4.6
LOCAL_ARM_NEON:=true
LOCAL_LDLIBS := -fuse-ld=gold
LOCAL_STATIC_LIBRARIES := cpufeatures

include $(BUILD_STATIC_LIBRARY)
