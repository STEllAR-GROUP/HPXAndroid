
# Copyright (c) 2012 Thomas Heller
#
# Distributed under the Boost Software License, Version 1.0. (See accompanying
# file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)

MY_DIR:=$(call my-dir)
include $(MY_DIR)/../boost_common.mk

include $(CLEAR_VARS)
LOCAL_MODULE:=boost_iostreams
LOCAL_CPP_EXTENSION:=.cpp
LOCAL_C_INCLUDES:=$(BOOST_SRC_ROOT)
LOCAL_C_INCLUDES+=$(LIBBZ2_SRC_ROOT)
#LOCAL_EXPORT_C_INCLUDES:=$(LOCAL_C_INCLUDES)
LOCAL_PATH:=$(BOOST_SRC_ROOT)
LOCAL_SRC_FILES:=libs/iostreams/src/file_descriptor.cpp
LOCAL_SRC_FILES+=libs/iostreams/src/mapped_file.cpp
LOCAL_SRC_FILES+=libs/iostreams/src/zlib.cpp
LOCAL_SRC_FILES+=libs/iostreams/src/bzip2.cpp
LOCAL_CPPFLAGS:=-DBOOST_IOSTREAMS_DYN_LINK=1
LOCAL_CPPFLAGS+=-DBOOST_SYSTEM_NO_DEPRECATED
#LOCAL_CPPFLAGS+=-DBOOST_DISABLE_ASSERT_HANDLER
LOCAL_SHARED_LIBRARIES:=boost_system libbz2
NDK_TOOLCHAIN_VERSION:=4.6
LOCAL_ARM_NEON:=true
LOCAL_LDLIBS := -fuse-ld=gold -lz

include $(BUILD_SHARED_LIBRARY)

$(call import-module,boost_system)
$(call import-module,libbz2)
