
# Copyright (c) 2012 Thomas Heller
#
# Distributed under the Boost Software License, Version 1.0. (See accompanying
# file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)

MY_DIR:=$(call my-dir)
include $(MY_DIR)/../hpx_common.mk

include $(CLEAR_VARS)
LOCAL_MODULE:=hpx_serializationd
LOCAL_CPP_EXTENSION:=.cpp
LOCAL_C_INCLUDES:=$(LOCAL_PATH)/boost
LOCAL_C_INCLUDES+=$(HPX_INCLUDES)
LOCAL_PATH:=$(HPX_SRC_ROOT)
LOCAL_SRC_FILES:=$(wildcard $(HPX_SRC_ROOT)/src/util/portable_binary_*archive.cpp)
LOCAL_SRC_FILES:=$(patsubst $(HPX_SRC_ROOT)/%, %, $(LOCAL_SRC_FILES))
LOCAL_CPPFLAGS:=$(filter-out -DBOOST_ENABLE_ASSERT_HANDLER, $(HPX_CPPFLAGS))
LOCAL_CPPFLAGS+=-DHPX_DLL_STRING=\"hpx_serialization\"
LOCAL_CPPFLAGS+=-DHPX_EXPORTS
LOCAL_CPPFLAGS+=-DHPX_COROUTINE_EXPORTS
LOCAL_LDLIBS := -fuse-ld=gold
LOCAL_STATIC_LIBRARIES := cpufeatures
LOCAL_SHARED_LIBRARIES := boost_system boost_serialization
NDK_TOOLCHAIN_VERSION:=4.6
LOCAL_ARM_NEON:=true

include $(BUILD_SHARED_LIBRARY)

$(call import-module, boost_system)
$(call import-module, boost_serialization)
