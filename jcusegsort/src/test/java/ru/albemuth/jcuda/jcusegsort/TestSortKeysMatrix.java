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
public class TestSortKeysMatrix {

    public static int ROWS = 5000;
    public static int COLS = 8000;

    @BeforeClass
    public static void init() {
        TestUtils.initCuda();
    }

    @After
    public void postTest() {
        showMemory();
    }

    @Test
    public void testSortBooleanKeysMatrix() throws Exception {
        boolean[] keys = booleanArray(ROWS * COLS);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, ROWS, COLS);
        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] bytes = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(bytes, i * COLS, (i + 1) * COLS);
        }
        boolean[] hostSortedKeys = booleanArray(bytes);
        assertArrayEquals(byteArray(sortedKeys), byteArray(hostSortedKeys));

        deletePtr(keysPtr);
    }

    @Test
    public void testSortByteKeysMatrix() throws Exception {
        byte[] keys = byteArray(ROWS * COLS);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        Sorting.sort(keysPtr, Datatype.BYTE, ROWS, COLS);
        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }
        assertArrayEquals(sortedKeys, hostSortedKeys);

        deletePtr(keysPtr);
    }

    @Test
    public void testSortCharKeysMatrix() throws Exception {
        char[] keys = charArray(ROWS * COLS);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        Sorting.sort(keysPtr, Datatype.CHAR, ROWS, COLS);
        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }
        assertArrayEquals(sortedKeys, hostSortedKeys);

        deletePtr(keysPtr);
    }

    @Test
    public void testSortShortKeysMatrix() throws Exception {
        short[] keys = shortArray(ROWS * COLS);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        Sorting.sort(keysPtr, Datatype.SHORT, ROWS, COLS);
        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }
        assertArrayEquals(sortedKeys, hostSortedKeys);

        deletePtr(keysPtr);
    }

    @Test
    public void testSortIntKeysMatrix() throws Exception {
        int[] keys = intArray(ROWS * COLS);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        Sorting.sort(keysPtr, Datatype.INT, ROWS, COLS);
        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }
        assertArrayEquals(sortedKeys, hostSortedKeys);

        deletePtr(keysPtr);
    }

    @Test
    public void testSortLongKeysMatrix() throws Exception {
        long[] keys = longArray(ROWS * COLS);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        Sorting.sort(keysPtr, Datatype.LONG, ROWS, COLS);
        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }
        assertArrayEquals(sortedKeys, hostSortedKeys);

        deletePtr(keysPtr);
    }

    @Test
    public void testSortFloatKeysMatrix() throws Exception {
        float[] keys = floatArray(ROWS * COLS);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        Sorting.sort(keysPtr, Datatype.FLOAT, ROWS, COLS);
        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);

        deletePtr(keysPtr);
    }

    @Test
    public void testSortDoubleKeysMatrix() throws Exception {
        double[] keys = doubleArray(ROWS * COLS);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        Sorting.sort(keysPtr, Datatype.DOUBLE, ROWS, COLS);
        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);

        deletePtr(keysPtr);
    }

}
