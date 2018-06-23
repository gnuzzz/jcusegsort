package ru.albemuth.jcuda.jcusegsort;

import jcuda.driver.CUdeviceptr;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.booleanArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.byteArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.copy2device;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.copy2host;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.deletePtr;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.devicePtr;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.charArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.doubleArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.floatArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.intArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.longArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.shortArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.show;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.showMemory;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.sizeOfItem;

/**
 * @author Vladimir Kornyshev { @literal <gnuzzz@mail.ru>}
 */
public class TestSortKeys {

    public static int N = 40000000;

    @BeforeClass
    public static void init() {
        TestUtils.initCuda();
    }

    @After
    public void postTest() {
        showMemory();
    }

    @Test
    public void testSortBooleanKeys() throws Exception {
        boolean[] keys = booleanArray(N);
//        boolean[] keys = booleanArray(10);
//        show(keys);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, keys.length);
//        System.out.println("keys sort complete");
        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr); //11
//        show(sortedKeys);
        byte[] bytes = byteArray(keys);
        Arrays.sort(bytes);
        boolean[] hostSortedKeys = booleanArray(bytes);
        assertArrayEquals(byteArray(sortedKeys), byteArray(hostSortedKeys));

        deletePtr(keysPtr);
    }

    @Test
    public void testSortByteKeys() throws Exception {
        byte[] keys = byteArray(N);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        Sorting.sort(keysPtr, Datatype.BYTE, keys.length);
        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] hostSortedKeys = keys.clone();
        Arrays.sort(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);

        deletePtr(keysPtr);
    }

    @Test
    public void testSortCharKeys() throws Exception {
        char[] keys = charArray(N);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        Sorting.sort(keysPtr, Datatype.CHAR, keys.length);
        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] hostSortedKeys = keys.clone();
        Arrays.sort(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);

        deletePtr(keysPtr);
    }

    @Test
    public void testSortShortKeys() throws Exception {
        short[] keys = shortArray(N);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        Sorting.sort(keysPtr, Datatype.SHORT, keys.length);
        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] hostSortedKeys = keys.clone();
        Arrays.sort(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);

        deletePtr(keysPtr);
    }

    @Test
    public void testSortIntKeys() throws Exception {
        int[] keys = intArray(N);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        Sorting.sort(keysPtr, Datatype.INT, keys.length);
        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] hostSortedKeys = keys.clone();
        Arrays.sort(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);

        deletePtr(keysPtr);
    }

    @Test
    public void testSortLongKeys() throws Exception {
        long[] keys = longArray(N);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        Sorting.sort(keysPtr, Datatype.LONG, keys.length);
        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] hostSortedKeys = keys.clone();
        Arrays.sort(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);

        deletePtr(keysPtr);
    }

    @Test
    public void testSortFloatKeys() throws Exception {
        float[] keys = floatArray(N);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        Sorting.sort(keysPtr, Datatype.FLOAT, keys.length);
        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] hostSortedKeys = keys.clone();
        Arrays.sort(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);

        deletePtr(keysPtr);
    }

    @Test
    public void testSortDoubleKeys() throws Exception {
        double[] keys = doubleArray(N);
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        Sorting.sort(keysPtr, Datatype.DOUBLE, keys.length);
        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] hostSortedKeys = keys.clone();
        Arrays.sort(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);

        deletePtr(keysPtr);
    }

}
