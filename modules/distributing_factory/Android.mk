
# Copyright (c) 2012 Thomas Heller
#
# Distributed under the Boost Software License, Version 1.0. (See accompanying
# file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)

MY_DIR:=$(call my-dir)
include $(MY_DIR)/../hpx_common.mk

include $(CLEAR_VARS)
LOCAL_MODULE:=distributing_factory

LOCAL_CPP_EXTENSION:=.cpp
LOCAL_C_INCLUDES:=$(LOCAL_PATH)/boost
LOCAL_C_INCLUDES+=$(HPX_INCLUDES)
LOCAL_PATH:=$(HPX_SRC_ROOT)

LOCAL_SRC_FILES:=src/components/distributing_factory/distributing_factory.cpp
LOCAL_SRC_FILES+=src/components/distributing_factory/server/distributing_factory.cpp

LOCAL_CPPFLAGS:=$(HPX_CPPFLAGS)
LOCAL_CPPFLAGS+=-DHPX_COMPONENT_NAME=distributing_factory
LOCAL_CPPFLAGS+=-DHPX_COMPONENT_NAME_STRING=\"distributing_factory\"
LOCAL_CPPFLAGS+=-DHPX_COMPONENT_EXPORTS
#LOCAL_CPPFLAGS+=-DBOOST_ENABLE_ASSERT_HANDLER

NDK_TOOLCHAIN_VERSION:=4.6
LOCAL_ARM_NEON:=true
LOCAL_LDLIBS := -fuse-ld=gold -llog

LOCAL_STATIC_LIBRARIES := $(HPX_STATIC_LIBRARIES)
LOCAL_SHARED_LIBRARIES := $(HPX_SHARED_LIBRARIES)
include $(BUILD_SHARED_LIBRARY)

HPX_SHARED_LIBRARIES += distributing_factory

$(call import-module, cpufeatures)
