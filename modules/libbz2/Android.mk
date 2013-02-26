
# Copyright (c) 2012 Thomas Heller
#
# Distributed under the Boost Software License, Version 1.0. (See accompanying
# file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)

LIBBZ2_SRC_ROOT:=$(strip $(LIBBZ2_SRC_ROOT))

ifndef LIBBZ2_SRC_ROOT
  $(call __ndk_info,ERROR: You LIBBZ2_SRC_ROOT not set)
  $(call __ndk_info,Please set LIBBZ2_SRC_ROOT to point to your Boost source directory and start again.)
  $(call __ndk_error,Aborting)
endif

include $(CLEAR_VARS)
LOCAL_MODULE:=libbz2
LOCAL_C_INCLUDES:=$(LIBBZ2_SRC_ROOT)
LOCAL_PATH:=$(LIBBZ2_SRC_ROOT)
LOCAL_SRC_FILES:=blocksort.c
LOCAL_SRC_FILES+=huffman.c
LOCAL_SRC_FILES+=crctable.c
LOCAL_SRC_FILES+=randtable.c
LOCAL_SRC_FILES+=compress.c
LOCAL_SRC_FILES+=decompress.c
LOCAL_SRC_FILES+=bzlib.c

NDK_TOOLCHAIN_VERSION:=4.6
LOCAL_ARM_NEON:=true
LOCAL_LDLIBS := -fuse-ld=gold
include $(BUILD_SHARED_LIBRARY)
