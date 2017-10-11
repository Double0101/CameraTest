cmake_minimum_required(VERSION 3.4.1)

set ( Project_DIR /Users/Double/Documents/workspace/Android/CameraTest )
set ( OpenCV_PATH /Users/Double/Documents/libs/OpenCV-android-sdk )


# set ( CMAKE_VERBOSE_MAKEFILE on )
# set ( CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11" )

include_directories ( ${OpenCV_PATH}/sdk/native/jni/include )

add_library ( native-lib
              SHARED
              src/main/cpp/native-lib.cpp )

add_library ( lib_opencv SHARED IMPORTED )
set_target_properties ( lib_opencv
                        PROPERTIES
                        IMPORTED_LOCATION
                        ${Project_DIR}/app/src/main/jniLibs/${ANDROID_ABI}/libopencv_java3.so )

find_library( log-lib
              log )

add_library ( npddetect src/main/cpp/npddetect.cpp )
add_library ( npdmodel src/main/cpp/npdmodel.cpp )

target_link_libraries( native-lib
                       npddetect
                       npdmodel
                       ${log-lib}
                       lib_opencv )