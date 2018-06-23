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
import static ru.albemuth.jcuda.jcusegsort.TestUtils.segments;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.shortArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.show;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.showMemory;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.sizeOfItem;

/**
 * @author Vladimir Kornyshev { @literal <gnuzzz@mail.ru>}
 */
public class TestSortKeysSegmentsWithContext {

    public static int N = 40000000;
    public static int SEGMENTS = 5000;

    @BeforeClass
    public static void init() {
        TestUtils.initCuda();
    }

    @After
    public void postTest() {
        showMemory();
    }

    @Test
    public void testSortBooleanKeysSegmentsWithContext() throws Exception {
        boolean[] keys = booleanArray(N);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);

        KeySortContext context = Sorting.keySortContext(Datatype.BOOLEAN, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, keys.length, segsPtr, segs.length, context);
        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }

        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);

        context.free();
        deletePtr(segsPtr);
        deletePtr(keysPtr);
    }

    @Test
    public void testSortByteKeysSegmentsWithContext() throws Exception {
        byte[] keys = byteArray(N);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);

        KeySortContext context = Sorting.keySortContext(Datatype.BYTE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BYTE, keys.length, segsPtr, segs.length, context);
        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }

        assertArrayEquals(sortedKeys, hostSortedKeys);

        context.free();
        deletePtr(segsPtr);
        deletePtr(keysPtr);
    }

    @Test
    public void testSortCharKeysSegmentsWithContext() throws Exception {
        char[] keys = charArray(N);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);

        KeySortContext context = Sorting.keySortContext(Datatype.CHAR, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.CHAR, keys.length, segsPtr, segs.length, context);
        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);

        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }

        assertArrayEquals(sortedKeys, hostSortedKeys);

        context.free();
        deletePtr(segsPtr);
        deletePtr(keysPtr);
    }

    @Test
    public void testSortShortKeysSegmentsWithContext() throws Exception {
        short[] keys = shortArray(N);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);

        KeySortContext context = Sorting.keySortContext(Datatype.SHORT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.SHORT, keys.length, segsPtr, segs.length, context);
        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);

        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }

        assertArrayEquals(sortedKeys, hostSortedKeys);

        context.free();
        deletePtr(segsPtr);
        deletePtr(keysPtr);
    }

    @Test
    public void testSortIntKeysSegmentsWithContext() throws Exception {
        int[] keys = intArray(N);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);

        KeySortContext context = Sorting.keySortContext(Datatype.INT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.INT, keys.length, segsPtr, segs.length, context);
        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);

        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }

        assertArrayEquals(sortedKeys, hostSortedKeys);

        context.free();
        deletePtr(segsPtr);
        deletePtr(keysPtr);
    }

    @Test
    public void testSortLongKeysSegmentsWithContext() throws Exception {
        long[] keys = longArray(N);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);

        KeySortContext context = Sorting.keySortContext(Datatype.LONG, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.LONG, keys.length, segsPtr, segs.length, context);
        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);

        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }

        assertArrayEquals(sortedKeys, hostSortedKeys);

        context.free();
        deletePtr(segsPtr);
        deletePtr(keysPtr);
    }

    @Test
    public void testSortFloatKeysSegmentsWithContext() throws Exception {
        float[] keys = floatArray(N);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);

        KeySortContext context = Sorting.keySortContext(Datatype.FLOAT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.FLOAT, keys.length, segsPtr, segs.length, context);
        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);

        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }

        assertArrayEquals(sortedKeys, hostSortedKeys, 0);

        context.free();
        deletePtr(segsPtr);
        deletePtr(keysPtr);
    }

    @Test
    public void testSortDoubleKeysSegmentsWithContext() throws Exception {
        double[] keys = doubleArray(N);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);

        KeySortContext context = Sorting.keySortContext(Datatype.DOUBLE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.DOUBLE, keys.length, segsPtr, segs.length, context);
        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);

        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }

        assertArrayEquals(sortedKeys, hostSortedKeys, 0);

        context.free();
        deletePtr(segsPtr);
        deletePtr(keysPtr);
    }

}
