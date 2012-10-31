
# Copyright (c) 2012 Thomas Heller
#
# Distributed under the Boost Software License, Version 1.0. (See accompanying
# file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)

MY_DIR:=$(call my-dir)
include $(MY_DIR)/../boost_common.mk

include $(CLEAR_VARS)
LOCAL_MODULE:=boost_filesystem
LOCAL_CPP_EXTENSION:=.cpp
LOCAL_C_INCLUDES:=$(BOOST_SRC_ROOT)
#LOCAL_EXPORT_C_INCLUDES:=$(LOCAL_C_INCLUDES)
LOCAL_PATH:=$(BOOST_SRC_ROOT)
LOCAL_SRC_FILES:=libs/filesystem/src/codecvt_error_category.cpp
LOCAL_SRC_FILES+=libs/filesystem/src/operations.cpp
LOCAL_SRC_FILES+=libs/filesystem/src/path.cpp
LOCAL_SRC_FILES+=libs/filesystem/src/path_traits.cpp
LOCAL_SRC_FILES+=libs/filesystem/src/portability.cpp
LOCAL_SRC_FILES+=libs/filesystem/src/unique_path.cpp
LOCAL_SRC_FILES+=libs/filesystem/src/utf8_codecvt_facet.cpp
LOCAL_CPPFLAGS:=-DBOOST_FILESYSTEM_DYN_LINK=1
LOCAL_CPPFLAGS+=-DBOOST_SYSTEM_NO_DEPRECATED
LOCAL_CPPFLAGS+=-DBOOST_DISABLE_ASSERT_HANDLER
LOCAL_SHARED_LIBRARIES:=boost_system
NDK_TOOLCHAIN_VERSION:=4.6
LOCAL_ARM_NEON:=true
LOCAL_LDLIBS := -fuse-ld=gold

include $(BUILD_SHARED_LIBRARY)

$(call import-module,boost_system)
