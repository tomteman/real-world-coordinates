/*==============================================================================
            Copyright (c) 2012 QUALCOMM Austria Research Center GmbH.
            All Rights Reserved.
            Qualcomm Confidential and Proprietary
            
@file 
    SampleUtils.h

@brief
    A utility class.

==============================================================================*/


#ifndef _QCAR_SAMPLEUTILS_H_
#define _QCAR_SAMPLEUTILS_H_

// Includes:
#include <stdio.h>
#include <android/log.h>

// Utility for logging:
#define LOG_TAG    "QCAR"
#define CUSTOM_LOG_TAG    "QCAR-TOM"
#define LOG(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define CUSTOMLOG(...)  __android_log_print(ANDROID_LOG_DEBUG, CUSTOM_LOG_TAG, __VA_ARGS__)
#define CUSTOMLOG2(...)  __android_log_print(ANDROID_LOG_DEBUG, "POSE", __VA_ARGS__)
#define CUSTOMLOG3(...)  __android_log_print(ANDROID_LOG_DEBUG, "WWW", __VA_ARGS__)


/// A utility class used by the QCAR SDK samples.
class SampleUtils
{
public:

	/// Prints a 3 (rows) x 4 (columns) matrix.
	static void printPoseMatrix(const float* matrix);

	/// Prints a 4 (rows) x 3 (columns) matrix.
	static void printTransposePoseMatrix(const float* matrix);

    /// Prints a 4x4 matrix.
    static void printMatrix(const float* matrix);

    static void printMatrixW(const float* matrix);

    static void Transpose(float* psrc, int R, int C);

    static void multiplyMatrices(float* matA, int rA, int cA, float* matB,
    		int rB, int cB, float* matC, int rC, int cC);

    /// Prints GL error information.
    static void checkGlError(const char* operation);
    
    /// Set the rotation components of this 4x4 matrix.
    static void setRotationMatrix(float angle, float x, float y, float z, 
        float *nMatrix);
    
    /// Set the translation components of this 4x4 matrix.
    static void translatePoseMatrix(float x, float y, float z,
        float* nMatrix = NULL);
    
    /// Applies a rotation.
    static void rotatePoseMatrix(float angle, float x, float y, float z, 
        float* nMatrix = NULL);
    
    /// Applies a scaling transformation.
    static void scalePoseMatrix(float x, float y, float z, 
        float* nMatrix = NULL);
    
    /// Multiplies the two matrices A and B and writes the result to C.
    static void multiplyMatrix(float *matrixA, float *matrixB, 
        float *matrixC);
    
    /// Initialize a shader.
    static unsigned int initShader(unsigned int shaderType, 
        const char* source);
    
    /// Create a shader program.
    static unsigned int createProgramFromBuffer(const char* vertexShaderBuffer,
        const char* fragmentShaderBuffer);
};

#endif // _QCAR_SAMPLEUTILS_H_
