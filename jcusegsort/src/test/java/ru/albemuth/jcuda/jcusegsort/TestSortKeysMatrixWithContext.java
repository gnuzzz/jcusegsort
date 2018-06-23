package ru.albemuth.jcuda.jcusegsort;

import jcuda.driver.CUdeviceptr;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.booleanArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.byteArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.charArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.copy2device;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.copy2host;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.deletePtr;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.devicePtr;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.doubleArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.floatArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.intArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.longArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.shortArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.showMemory;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.sizeOfItem;

/**
 * @author Vladimir Kornyshev { @literal <gnuzzz@mail.ru>}
 */
public class TestSortKeysMatrixWithContext {

    public static int ROWS = 5000;
//    public static int ROWS = 3;
    public static int COLS = 8000;
//    public static int COLS = 5;

    @BeforeClass
    public static void init() {
        TestUtils.initCuda();
    }

    @After
    public void postTest() {
        showMemory();
    }

    @Test
    public void testSortBooleanKeysMatrixWithContext() throws Exception {
        boolean[] keys = booleanArray(ROWS * COLS);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        KeySortContext context = Sorting.keySortContext(Datatype.BOOLEAN, ROWS * COLS, ROWS);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, ROWS, COLS, context);
        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] bytes = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(bytes, i * COLS, (i + 1) * COLS);
        }
        boolean[] hostSortedKeys = booleanArray(bytes);
        assertArrayEquals(byteArray(sortedKeys), byteArray(hostSortedKeys));

        context.free();
        deletePtr(keysPtr);
    }

    @Test
    public void testSortByteKeysMatrixWithContext() throws Exception {
        byte[] keys = byteArray(ROWS * COLS);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        KeySortContext context = Sorting.keySortContext(Datatype.BYTE, ROWS * COLS, ROWS);
        Sorting.sort(keysPtr, Datatype.BYTE, ROWS, COLS, context);
        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }
        assertArrayEquals(sortedKeys, hostSortedKeys);

        context.free();
        deletePtr(keysPtr);
    }

    @Test
    public void testSortCharKeysMatrixWithContext() throws Exception {
        char[] keys = charArray(ROWS * COLS);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        KeySortContext context = Sorting.keySortContext(Datatype.CHAR, ROWS * COLS, ROWS);
        Sorting.sort(keysPtr, Datatype.CHAR, ROWS, COLS, context);
        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }
        assertArrayEquals(sortedKeys, hostSortedKeys);

        context.free();
        deletePtr(keysPtr);
    }

    @Test
    public void testSortShortKeysMatrixWithContext() throws Exception {
        short[] keys = shortArray(ROWS * COLS);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        KeySortContext context = Sorting.keySortContext(Datatype.SHORT, ROWS * COLS, ROWS);
        Sorting.sort(keysPtr, Datatype.SHORT, ROWS, COLS, context);
        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }
        assertArrayEquals(sortedKeys, hostSortedKeys);

        context.free();
        deletePtr(keysPtr);
    }

    @Test
    public void testSortIntKeysMatrixWithContext() throws Exception {
        int[] keys = intArray(ROWS * COLS);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        KeySortContext context = Sorting.keySortContext(Datatype.INT, ROWS * COLS, ROWS);
        Sorting.sort(keysPtr, Datatype.INT, ROWS, COLS, context);
        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }
        assertArrayEquals(sortedKeys, hostSortedKeys);

        context.free();
        deletePtr(keysPtr);
    }

    @Test
    public void testSortLongKeysMatrixWithContext() throws Exception {
        long[] keys = longArray(ROWS * COLS);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        KeySortContext context = Sorting.keySortContext(Datatype.LONG, ROWS * COLS, ROWS);
        Sorting.sort(keysPtr, Datatype.LONG, ROWS, COLS, context);
        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }
        assertArrayEquals(sortedKeys, hostSortedKeys);

        context.free();
        deletePtr(keysPtr);
    }

    @Test
    public void testSortFloatKeysMatrixWithContext() throws Exception {
        float[] keys = floatArray(ROWS * COLS);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        KeySortContext context = Sorting.keySortContext(Datatype.FLOAT, ROWS * COLS, ROWS);
        Sorting.sort(keysPtr, Datatype.FLOAT, ROWS, COLS, context);
        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);

        context.free();
        deletePtr(keysPtr);
    }

    @Test
    public void testSortDoubleKeysMatrixWithContext() throws Exception {
        double[] keys = doubleArray(ROWS * COLS);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        KeySortContext context = Sorting.keySortContext(Datatype.DOUBLE, ROWS * COLS, ROWS);
        Sorting.sort(keysPtr, Datatype.DOUBLE, ROWS, COLS, context);
        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);

        context.free();
        deletePtr(keysPtr);
    }
}
