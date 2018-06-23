package ru.albemuth.jcuda.jcusegsort;

import jcuda.Pointer;
import jcuda.driver.CUcontext;
import jcuda.driver.CUdevice;
import jcuda.driver.CUdeviceptr;
import jcuda.driver.JCudaDriver;

/**
 * @author Vladimir Kornyshev { @literal <gnuzzz@mail.ru>}
 */
public class TestUtils {

    public static void initCuda() {
        CUcontext context = new CUcontext();
        if (JCudaDriver.cuCtxGetCurrent(context) != 0) {
            JCudaDriver.setExceptionsEnabled(true);
            JCudaDriver.cuInit(0);
            CUdevice device = new CUdevice();
            JCudaDriver.cuDeviceGet(device, 0);
            JCudaDriver.cuCtxCreate(context, 0, device);
        }
    }

    public static boolean[] booleanArray(int length) {
        boolean[] array = new boolean[length];
        for (int i = 0; i < length; i++) {
            array[i] = Math.random() < 0.5;
        }
        return array;
    }

    public static boolean[] booleanArray(boolean[] array) {
        boolean[] booleanArray = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            booleanArray[i] = array[i];
        }
        return booleanArray;
    }

    public static boolean[] booleanArray(byte[] array) {
        boolean[] booleanArray = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            booleanArray[i] = array[i] > 0;
        }
        return booleanArray;
    }

    public static boolean[] booleanArray(char[] array) {
        boolean[] booleanArray = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            booleanArray[i] = array[i] > 0;
        }
        return booleanArray;
    }

    public static boolean[] booleanArray(short[] array) {
        boolean[] booleanArray = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            booleanArray[i] = array[i] > 0;
        }
        return booleanArray;
    }

    public static boolean[] booleanArray(int[] array) {
        boolean[] booleanArray = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            booleanArray[i] = array[i] > 0;
        }
        return booleanArray;
    }

    public static boolean[] booleanArray(long[] array) {
        boolean[] booleanArray = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            booleanArray[i] = array[i] > 0;
        }
        return booleanArray;
    }

    public static boolean[] booleanArray(float[] array) {
        boolean[] booleanArray = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            booleanArray[i] = array[i] > 0;
        }
        return booleanArray;
    }

    public static boolean[] booleanArray(double[] array) {
        boolean[] booleanArray = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            booleanArray[i] = array[i] > 0;
        }
        return booleanArray;
    }

    public static byte[] byteArray(int length) {
        byte[] array = new byte[length];
        for (int i = 0; i < length; i++) {
            array[i] = (byte) (Math.random() * Byte.MAX_VALUE);
        }
        return array;
    }

    public static byte[] byteArray(boolean[] array) {
        byte[] byteArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            byteArray[i] = array[i] ? (byte)1 : 0;
        }
        return byteArray;
    }

    public static byte[] byteArray(byte[] array) {
        byte[] byteArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            byteArray[i] = array[i];
        }
        return byteArray;
    }

    public static byte[] byteArray(char[] array) {
        byte[] byteArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            byteArray[i] = (byte) array[i];
        }
        return byteArray;
    }

    public static byte[] byteArray(short[] array) {
        byte[] byteArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            byteArray[i] = (byte) array[i];
        }
        return byteArray;
    }

    public static byte[] byteArray(int[] array) {
        byte[] byteArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            byteArray[i] = (byte) array[i];
        }
        return byteArray;
    }

    public static byte[] byteArray(long[] array) {
        byte[] byteArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            byteArray[i] = (byte) array[i];
        }
        return byteArray;
    }

    public static byte[] byteArray(float[] array) {
        byte[] byteArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            byteArray[i] = (byte) array[i];
        }
        return byteArray;
    }

    public static byte[] byteArray(double[] array) {
        byte[] byteArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            byteArray[i] = (byte) array[i];
        }
        return byteArray;
    }

    public static char[] charArray(int length) {
        char[] array = new char[length];
        for (int i = 0; i < length; i++) {
            array[i] = (char) (Math.random() * Character.MAX_VALUE);
        }
        return array;
    }

    public static char[] charArray(boolean[] array) {
        char[] charArray = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            charArray[i] = array[i] ? (char)1 : 0;
        }
        return charArray;
    }

    public static char[] charArray(byte[] array) {
        char[] charArray = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            charArray[i] = (char) array[i];
        }
        return charArray;
    }

    public static char[] charArray(char[] array) {
        char[] charArray = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            charArray[i] = (char) array[i];
        }
        return charArray;
    }

    public static char[] charArray(short[] array) {
        char[] charArray = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            charArray[i] = (char) array[i];
        }
        return charArray;
    }

    public static char[] charArray(int[] array) {
        char[] charArray = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            charArray[i] = (char) array[i];
        }
        return charArray;
    }

    public static char[] charArray(long[] array) {
        char[] charArray = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            charArray[i] = (char) array[i];
        }
        return charArray;
    }

    public static char[] charArray(float[] array) {
        char[] charArray = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            charArray[i] = (char) array[i];
        }
        return charArray;
    }

    public static char[] charArray(double[] array) {
        char[] charArray = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            charArray[i] = (char) array[i];
        }
        return charArray;
    }

    public static short[] shortArray(int length) {
        short[] array = new short[length];
        for (int i = 0; i < length; i++) {
            array[i] = (short) (Math.random() * Short.MAX_VALUE);
        }
        return array;
    }

    public static short[] shortArray(boolean[] array) {
        short[] shortArray = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            shortArray[i] = array[i] ? (short)1 : 0;
        }
        return shortArray;
    }

    public static short[] shortArray(byte[] array) {
        short[] shortArray = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            shortArray[i] = array[i];
        }
        return shortArray;
    }

    public static short[] shortArray(char[] array) {
        short[] shortArray = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            shortArray[i] = (short) array[i];
        }
        return shortArray;
    }
    
    public static short[] shortArray(short[] array) {
        short[] shortArray = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            shortArray[i] = array[i];
        }
        return shortArray;
    }

    public static short[] shortArray(int[] array) {
        short[] shortArray = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            shortArray[i] = (short) array[i];
        }
        return shortArray;
    }

    public static short[] shortArray(long[] array) {
        short[] shortArray = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            shortArray[i] = (short) array[i];
        }
        return shortArray;
    }

    public static short[] shortArray(float[] array) {
        short[] shortArray = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            shortArray[i] = (short) array[i];
        }
        return shortArray;
    }

    public static short[] shortArray(double[] array) {
        short[] shortArray = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            shortArray[i] = (short) array[i];
        }
        return shortArray;
    }

    public static int[] intArray(int length) {
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = (int) (Math.random() * Integer.MAX_VALUE);
        }
        return array;
    }

    public static int[] intArray(boolean[] array) {
        int[] intArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            intArray[i] = array[i] ? 1 : 0;
        }
        return intArray;
    }

    public static int[] intArray(byte[] array) {
        int[] intArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            intArray[i] = array[i];
        }
        return intArray;
    }

    public static int[] intArray(char[] array) {
        int[] intArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            intArray[i] = array[i];
        }
        return intArray;
    }

    public static int[] intArray(short[] array) {
        int[] intArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            intArray[i] = array[i];
        }
        return intArray;
    }

    public static int[] intArray(int[] array) {
        int[] intArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            intArray[i] = array[i];
        }
        return intArray;
    }

    public static int[] intArray(long[] array) {
        int[] intArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            intArray[i] = (int) array[i];
        }
        return intArray;
    }

    public static int[] intArray(float[] array) {
        int[] intArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            intArray[i] = (int) array[i];
        }
        return intArray;
    }

    public static int[] intArray(double[] array) {
        int[] intArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            intArray[i] = (int) array[i];
        }
        return intArray;
    }

    public static long[] longArray(int length) {
        long[] array = new long[length];
        for (int i = 0; i < length; i++) {
            array[i] = (long) (Math.random() * Long.MAX_VALUE);
        }
        return array;
    }

    public static long[] longArray(boolean[] array) {
        long[] longArray = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longArray[i] = array[i] ? 1 : 0;
        }
        return longArray;
    }

    public static long[] longArray(byte[] array) {
        long[] longArray = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longArray[i] = array[i];
        }
        return longArray;
    }

    public static long[] longArray(char[] array) {
        long[] longArray = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longArray[i] = array[i];
        }
        return longArray;
    }

    public static long[] longArray(short[] array) {
        long[] longArray = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longArray[i] = array[i];
        }
        return longArray;
    }

    public static long[] longArray(int[] array) {
        long[] longArray = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longArray[i] = array[i];
        }
        return longArray;
    }

    public static long[] longArray(long[] array) {
        long[] longArray = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longArray[i] = array[i];
        }
        return longArray;
    }

    public static long[] longArray(float[] array) {
        long[] longArray = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longArray[i] = (long) array[i];
        }
        return longArray;
    }

    public static long[] longArray(double[] array) {
        long[] longArray = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longArray[i] = (long) array[i];
        }
        return longArray;
    }

    public static float[] floatArray(int length) {
        float[] array = new float[length];
        for (int i = 0; i < length; i++) {
            array[i] = (float) (Math.random() * Float.MAX_VALUE);
        }
        return array;
    }

    public static float[] floatArray(boolean[] array) {
        float[] floatArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            floatArray[i] = array[i] ? 1 : 0;
        }
        return floatArray;
    }

    public static float[] floatArray(byte[] array) {
        float[] floatArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            floatArray[i] = array[i];
        }
        return floatArray;
    }

    public static float[] floatArray(char[] array) {
        float[] floatArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            floatArray[i] = array[i];
        }
        return floatArray;
    }

    public static float[] floatArray(short[] array) {
        float[] floatArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            floatArray[i] = array[i];
        }
        return floatArray;
    }

    public static float[] floatArray(int[] array) {
        float[] floatArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            floatArray[i] = array[i];
        }
        return floatArray;
    }

    public static float[] floatArray(long[] array) {
        float[] floatArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            floatArray[i] = array[i];
        }
        return floatArray;
    }

    public static float[] floatArray(float[] array) {
        float[] floatArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            floatArray[i] = array[i];
        }
        return floatArray;
    }

    public static float[] floatArray(double[] array) {
        float[] floatArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            floatArray[i] = (float) array[i];
        }
        return floatArray;
    }

    public static double[] doubleArray(int length) {
        double[] array = new double[length];
        for (int i = 0; i < length; i++) {
            array[i] = Math.random() * Double.MAX_VALUE;
        }
        return array;
    }

    public static double[] doubleArray(boolean[] array) {
        double[] doubleArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubleArray[i] = array[i] ? 1 : 0;
        }
        return doubleArray;
    }

    public static double[] doubleArray(byte[] array) {
        double[] doubleArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubleArray[i] = array[i];
        }
        return doubleArray;
    }

    public static double[] doubleArray(char[] array) {
        double[] doubleArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubleArray[i] = array[i];
        }
        return doubleArray;
    }

    public static double[] doubleArray(short[] array) {
        double[] doubleArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubleArray[i] = array[i];
        }
        return doubleArray;
    }

    public static double[] doubleArray(int[] array) {
        double[] doubleArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubleArray[i] = array[i];
        }
        return doubleArray;
    }

    public static double[] doubleArray(long[] array) {
        double[] doubleArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubleArray[i] = array[i];
        }
        return doubleArray;
    }

    public static double[] doubleArray(float[] array) {
        double[] doubleArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubleArray[i] = array[i];
        }
        return doubleArray;
    }

    public static double[] doubleArray(double[] array) {
        double[] doubleArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubleArray[i] = array[i];
        }
        return doubleArray;
    }

    public static int sizeOfItem(Object array) {
        Class itemClass = array.getClass().getComponentType();
        int sizeOf = 0;
        if (boolean.class.equals(itemClass)) {
            sizeOf = 1;
        } else if (byte.class.equals(itemClass)) {
            sizeOf = 1;
        } else if (char.class.equals(itemClass)) {
            sizeOf = 2;
        } else if (short.class.equals(itemClass)) {
            sizeOf = 2;
        } else if (int.class.equals(itemClass)) {
            sizeOf = 4;
        } else if (long.class.equals(itemClass)) {
            sizeOf = 8;
        } else if (float.class.equals(itemClass)) {
            sizeOf = 4;
        } else if (double.class.equals(itemClass)) {
            sizeOf = 8;
        }
        return sizeOf;
    }

    public static Pointer pointer(boolean[] array) {
        return Pointer.to(byteArray(array));
    }

    public static Pointer pointer(byte[] array) {
        return Pointer.to(array);
    }

    public static Pointer pointer(char[] array) {
        return Pointer.to(array);
    }

    public static Pointer pointer(short[] array) {
        return Pointer.to(array);
    }

    public static Pointer pointer(int[] array) {
        return Pointer.to(array);
    }

    public static Pointer pointer(long[] array) {
        return Pointer.to(array);
    }

    public static Pointer pointer(float[] array) {
        return Pointer.to(array);
    }

    public static Pointer pointer(double[] array) {
        return Pointer.to(array);
    }

    public static CUdeviceptr devicePtr(long size) {
        CUdeviceptr ptr = new CUdeviceptr();
        JCudaDriver.cuMemAlloc(ptr, size);
        return ptr;
    }

    public static void deletePtr(CUdeviceptr ptr) {
        JCudaDriver.cuMemFree(ptr);
    }

    public static void showMemory() {
        long[] total = new long[1];
        long[] free = new long[1];
        JCudaDriver.cuMemGetInfo(free, total);
        System.out.println(total[0] + "/" + free[0]);
    }

    public static void copy2device(CUdeviceptr devicePtr, boolean[] data) {
        Pointer hostPtr = pointer(data);
        JCudaDriver.cuMemcpyHtoD(devicePtr, hostPtr, data.length * sizeOfItem(data));
    }

    public static void copy2device(CUdeviceptr devicePtr, byte[] data) {
        Pointer hostPtr = pointer(data);
        JCudaDriver.cuMemcpyHtoD(devicePtr, hostPtr, data.length * sizeOfItem(data));
    }

    public static void copy2device(CUdeviceptr devicePtr, char[] data) {
        Pointer hostPtr = pointer(data);
        JCudaDriver.cuMemcpyHtoD(devicePtr, hostPtr, data.length * sizeOfItem(data));
    }

    public static void copy2device(CUdeviceptr devicePtr, short[] data) {
        Pointer hostPtr = pointer(data);
        JCudaDriver.cuMemcpyHtoD(devicePtr, hostPtr, data.length * sizeOfItem(data));
    }

    public static void copy2device(CUdeviceptr devicePtr, int[] data) {
        Pointer hostPtr = pointer(data);
        JCudaDriver.cuMemcpyHtoD(devicePtr, hostPtr, data.length * sizeOfItem(data));
    }

    public static void copy2device(CUdeviceptr devicePtr, long[] data) {
        Pointer hostPtr = pointer(data);
        JCudaDriver.cuMemcpyHtoD(devicePtr, hostPtr, data.length * sizeOfItem(data));
    }

    public static void copy2device(CUdeviceptr devicePtr, float[] data) {
        Pointer hostPtr = pointer(data);
        JCudaDriver.cuMemcpyHtoD(devicePtr, hostPtr, data.length * sizeOfItem(data));
    }

    public static void copy2device(CUdeviceptr devicePtr, double[] data) {
        Pointer hostPtr = pointer(data);
        JCudaDriver.cuMemcpyHtoD(devicePtr, hostPtr, data.length * sizeOfItem(data));
    }

    public static void copy2host(boolean[] result, CUdeviceptr devicePtr) {
        Pointer hostPtr = pointer(result);
        JCudaDriver.cuMemcpyDtoH(hostPtr, devicePtr, result.length * sizeOfItem(result));
        byte[] bytes = hostPtr.getByteBuffer(0, result.length).array();
        for (int i = 0; i < result.length; i++) {
            result[i] = bytes[i] > 0;
        }
    }

    public static void copy2host(byte[] result, CUdeviceptr devicePtr) {
        Pointer hostPtr = pointer(result);
        JCudaDriver.cuMemcpyDtoH(hostPtr, devicePtr, result.length * sizeOfItem(result));
    }

    public static void copy2host(char[] result, CUdeviceptr devicePtr) {
        Pointer hostPtr = pointer(result);
        JCudaDriver.cuMemcpyDtoH(hostPtr, devicePtr, result.length * sizeOfItem(result));
    }

    public static void copy2host(short[] result, CUdeviceptr devicePtr) {
        Pointer hostPtr = pointer(result);
        JCudaDriver.cuMemcpyDtoH(hostPtr, devicePtr, result.length * sizeOfItem(result));
    }

    public static void copy2host(int[] result, CUdeviceptr devicePtr) {
        Pointer hostPtr = pointer(result);
        JCudaDriver.cuMemcpyDtoH(hostPtr, devicePtr, result.length * sizeOfItem(result));
    }

    public static void copy2host(long[] result, CUdeviceptr devicePtr) {
        Pointer hostPtr = pointer(result);
        JCudaDriver.cuMemcpyDtoH(hostPtr, devicePtr, result.length * sizeOfItem(result));
    }

    public static void copy2host(float[] result, CUdeviceptr devicePtr) {
        Pointer hostPtr = pointer(result);
        JCudaDriver.cuMemcpyDtoH(hostPtr, devicePtr, result.length * sizeOfItem(result));
    }

    public static void copy2host(double[] result, CUdeviceptr devicePtr) {
        Pointer hostPtr = pointer(result);
        JCudaDriver.cuMemcpyDtoH(hostPtr, devicePtr, result.length * sizeOfItem(result));
    }

    public static void show(boolean[] array) {
        for (boolean k: array) {
            System.out.print(k + " ");
        }
        System.out.println();
    }

    public static void show(byte[] array) {
        for (byte k: array) {
            System.out.print(k + " ");
        }
        System.out.println();
    }

    public static void show(char[] array) {
        for (char k: array) {
            System.out.print(k + " ");
        }
        System.out.println();
    }

    public static void show(short[] array) {
        for (short k: array) {
            System.out.print(k + " ");
        }
        System.out.println();
    }

    public static void show(int[] array) {
        for (int k: array) {
            System.out.print(k + " ");
        }
        System.out.println();
    }

    public static void show(long[] array) {
        for (long k: array) {
            System.out.print(k + " ");
        }
        System.out.println();
    }

    public static void show(float[] array) {
        for (float k: array) {
            System.out.print(k + " ");
        }
        System.out.println();
    }

    public static void show(double[] array) {
        for (double k: array) {
            System.out.print(k + " ");
        }
        System.out.println();
    }

    public static String flu(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1, s.length());
    }

    public static int[] segments(int keysLength, int segmentsLength) {
        int[] segs = new int[segmentsLength];
        segs[0] = 0;
        for (int i = 1; i < segmentsLength; i++) {
            int max = segs[i - 1] + (segmentsLength - segs[i - 1]) / 2;
            if (segmentsLength - segs[i - 1] <= segmentsLength - i) {
                max = 1;
            }
            int d = (int) (Math.random() * max);
            if (d == 0) d = 1;
            segs[i] = segs[i - 1] + d;
        }
        return segs;
    }

}
