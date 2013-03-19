
# Copyright (c) 2012 Thomas Heller
#
# Distributed under the Boost Software License, Version 1.0. (See accompanying
# file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)

SNAPPY_SRC_ROOT:=$(strip $(SNAPPY_SRC_ROOT))

ifndef SNAPPY_SRC_ROOT
  $(call __ndk_info,ERROR: You SNAPPY_SRC_ROOT not set)
  $(call __ndk_info,Please set SNAPPY_SRC_ROOT to point to your Boost source directory and start again.)
  $(call __ndk_error,Aborting)
endif

include $(CLEAR_VARS)
LOCAL_MODULE:=snappy
LOCAL_C_INCLUDES:=$(SNAPPY_SRC_ROOT)
LOCAL_PATH:=$(SNAPPY_SRC_ROOT)
LOCAL_SRC_FILES:=snappy.cc
LOCAL_SRC_FILES+=snappy-sinksource.cc
LOCAL_SRC_FILES+=snappy-stubs-internal.cc
LOCAL_SRC_FILES+=snappy-c.cc

NDK_TOOLCHAIN_VERSION:=4.6
LOCAL_ARM_NEON:=true
LOCAL_LDLIBS := -fuse-ld=gold
include $(BUILD_SHARED_LIBRARY)
