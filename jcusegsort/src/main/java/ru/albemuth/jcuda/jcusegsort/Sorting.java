package ru.albemuth.jcuda.jcusegsort;

import jcuda.driver.CUdeviceptr;

import static ru.albemuth.jcuda.jcusegsort.JCusegsort.checkResult;

/**
 * @author Vladimir Kornyshev { @literal <gnuzzz@mail.ru>}
 */
public final class Sorting {

    static {
        JCusegsort.assertInit();
    }

    public static KeySortContext keySortContext(Datatype keyType, int keysLength, int segmentsLength) {
        KeySortContext context = new KeySortContext(keyType, keysLength, segmentsLength);
        checkResult(context.init(keyType.type, keysLength, segmentsLength));
        return context;
    }

    public static KeyValueSortContext keyValueSortContext(Datatype keyType, Datatype valueType, int keysLength, int segmentsLength) {
        KeyValueSortContext context = new KeyValueSortContext(keyType, valueType, keysLength, segmentsLength);
        checkResult(context.init(keyType.type, valueType.type, keysLength, segmentsLength));
        return context;
    }

    public static void sort(CUdeviceptr keys, Datatype keyType, int keysLength, KeySortContext context) {
        checkResult(sortNative(keys, keyType.type, keysLength, context));
    }

    public static void sort(CUdeviceptr keys, Datatype keyType, int keysLength) {
        checkResult(sortNative(keys, keyType.type, keysLength));
    }

    public static void sort(CUdeviceptr keys, Datatype keyType, CUdeviceptr vals, Datatype valueType, int keysLength, KeyValueSortContext context) {
        checkResult(sortNative(keys, keyType.type, vals, valueType.type, keysLength, context));
    }

    public static void sort(CUdeviceptr keys, Datatype keyType, CUdeviceptr vals, Datatype valueType, int keysLength) {
        checkResult(sortNative(keys, keyType.type, vals, valueType.type, keysLength));
    }

    public static void sort(CUdeviceptr keys, Datatype keyType, int rows, int cols, KeySortContext context) {
        checkResult(sortNative(keys, keyType.type, rows, cols, context));
    }

    public static void sort(CUdeviceptr keys, Datatype keyType, int rows, int cols) {
        checkResult(sortNative(keys, keyType.type, rows, cols));
    }

    public static void sort(CUdeviceptr keys, Datatype keyType, CUdeviceptr vals, Datatype valueType, int rows, int cols, KeyValueSortContext context) {
        checkResult(sortNative(keys, keyType.type, vals, valueType.type, rows, cols, context));
    }

    public static void sort(CUdeviceptr keys, Datatype keyType, CUdeviceptr vals, Datatype valueType, int rows, int cols) {
        checkResult(sortNative(keys, keyType.type, vals, valueType.type, rows, cols));
    }

    public static void sort(CUdeviceptr keys, Datatype keyType, int keysLength, CUdeviceptr segments, int segmentsLength, KeySortContext context) {
        checkResult(sortNative(keys, keyType.type, keysLength, segments, segmentsLength, context));
    }

    public static void sort(CUdeviceptr keys, Datatype keyType, int keysLength, CUdeviceptr segments, int segmentsLength) {
        checkResult(sortNative(keys, keyType.type, keysLength, segments, segmentsLength));
    }

    public static void sort(CUdeviceptr keys, Datatype keyType, CUdeviceptr vals, Datatype valueType, int keysLength, CUdeviceptr segments, int segmentsLength, KeyValueSortContext context) {
        checkResult(sortNative(keys, keyType.type, vals, valueType.type, keysLength, segments, segmentsLength, context));
    }

    public static void sort(CUdeviceptr keys, Datatype keyType, CUdeviceptr vals, Datatype valueType, int keysLength, CUdeviceptr segments, int segmentsLength) {
        checkResult(sortNative(keys, keyType.type, vals, valueType.type, keysLength, segments, segmentsLength));
    }

    private static native int sortNative(CUdeviceptr keys, int keyType, int keysLength, KeySortContext context);

    private static native int sortNative(CUdeviceptr keys, int keyType, int keysLength);

    private static native int sortNative(CUdeviceptr keys, int keyType, CUdeviceptr vals, int valueType, int keysLength, KeyValueSortContext context);

    private static native int sortNative(CUdeviceptr keys, int keyType, CUdeviceptr vals, int valueType, int keysLength);

    private static native int sortNative(CUdeviceptr keys, int keyType, int rows, int cols, KeySortContext context);

    private static native int sortNative(CUdeviceptr keys, int keyType, int rows, int cols);

    private static native int sortNative(CUdeviceptr keys, int keyType, CUdeviceptr vals, int valueType, int rows, int cols, KeyValueSortContext context);

    private static native int sortNative(CUdeviceptr keys, int keyType, CUdeviceptr vals, int valueType, int rows, int cols);

    private static native int sortNative(CUdeviceptr keys, int keyType, int keysLength, CUdeviceptr segments, int segmentsLength, KeySortContext context);

    private static native int sortNative(CUdeviceptr keys, int keyType, int keysLength, CUdeviceptr segments, int segmentsLength);

    private static native int sortNative(CUdeviceptr keys, int keyType, CUdeviceptr vals, int valueType, int keysLength, CUdeviceptr segments, int segmentsLength, KeyValueSortContext context);

    private static native int sortNative(CUdeviceptr keys, int keyType, CUdeviceptr vals, int valueType, int keysLength, CUdeviceptr segments, int segmentsLength);

}
