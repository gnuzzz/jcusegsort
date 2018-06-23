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
import static ru.albemuth.jcuda.jcusegsort.TestUtils.flu;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.intArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.longArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.segments;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.shortArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.showMemory;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.sizeOfItem;

/**
 * @author Vladimir Kornyshev { @literal <gnuzzz@mail.ru>}
 */
public class TestSortKeyValuePairsSegments {

    public static int N = 40000000;
    public static int SEGMENTS = 5000;

    public static class Generator {
        public static void main(String[] args) {
            String[] types = {"boolean", "byte", "char", "short", "int", "long", "float", "double"};
            for (String keyType : types) {
                for (String valueType : types) {
                    String testMethodSrc = "    @Test\n" +
                            "    public void testSort" + flu(keyType) + "Keys" + flu(valueType) + "ValsSegments() throws Exception {\n" +
                            "        " + keyType + "[] keys = " + keyType + "Array(N);\n" +
                            "        " + valueType + "[] vals = " + valueType + "Array(keys);\n" +
                            "        int[] segs = segments(keys.length, SEGMENTS);\n" +
                            "\n" +
                            "        long keysSize = keys.length * sizeOfItem(keys);\n" +
                            "        CUdeviceptr keysPtr = devicePtr(keysSize);\n" +
                            "        copy2device(keysPtr, keys);\n" +
                            "        long valsSize = vals.length * sizeOfItem(vals);\n" +
                            "        CUdeviceptr valsPtr = devicePtr(valsSize);\n" +
                            "        copy2device(valsPtr, vals);\n" +
                            "        long segsSize = segs.length * sizeOfItem(segs);\n" +
                            "        CUdeviceptr segsPtr = devicePtr(segsSize);\n" +
                            "        copy2device(segsPtr, segs);" +
                            "\n" +
                            "        Sorting.sort(keysPtr, Datatype." + keyType.toUpperCase() + ", valsPtr, Datatype." + valueType.toUpperCase() + ", keys.length, segsPtr, segs.length);\n" +
                            "\n" +
                            "        " + keyType + "[] sortedKeys = new " + keyType + "[keys.length];\n" +
                            "        copy2host(sortedKeys, keysPtr);\n" +
                            "        " + valueType + "[] sortedVals = new " + valueType + "[vals.length];\n" +
                            "        copy2host(sortedVals, valsPtr);\n" +
                            "\n" +
                            ("boolean".equals(keyType) ?
                                    "        byte[] hostSortedKeys = byteArray(keys);\n" :
                                    "        " + keyType + "[] hostSortedKeys = " + keyType + "Array(keys);\n") +

                            "        for (int i = 0; i < segs.length; i++) {\n" +
                            "            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);\n" +
                            "        }" +
                            "        " + valueType + "[] hostSortedVals = " + valueType + "Array(hostSortedKeys);\n" +
                            ("boolean".equals(keyType) ?
                                    "        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);\n" :
                                    "float".equals(keyType) || "double".equals(keyType) ?
                                            "        assertArrayEquals(sortedKeys, hostSortedKeys, 0);\n" :
                                            "        assertArrayEquals(sortedKeys, hostSortedKeys);\n") +
                            ("boolean".equals(valueType) ?
                                    "        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));\n" :
                                    "float".equals(valueType) || "double".equals(valueType) ?
                                            "        assertArrayEquals(sortedVals, hostSortedVals, 0);\n" :
                                            "        assertArrayEquals(sortedVals, hostSortedVals);\n") +
                            "\n" +
                            "        deletePtr(keysPtr);\n" +
                            "        deletePtr(valsPtr);\n" +
                            "        deletePtr(segsPtr);\n" +
                            "    }";
                    System.out.println(testMethodSrc);
                    System.out.println();
                }
            }
        }
    }

    @BeforeClass
    public static void init() {
        TestUtils.initCuda();
    }

    @After
    public void postTest() {
        showMemory();
    }

    @Test
    public void testSortBooleanKeysBooleanValsSegments() throws Exception {
        boolean[] keys = booleanArray(N);
        boolean[] vals = booleanArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.BOOLEAN, keys.length, segsPtr, segs.length);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortBooleanKeysByteValsSegments() throws Exception {
        boolean[] keys = booleanArray(N);
        byte[] vals = byteArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.BYTE, keys.length, segsPtr, segs.length);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortBooleanKeysCharValsSegments() throws Exception {
        boolean[] keys = booleanArray(N);
        char[] vals = charArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.CHAR, keys.length, segsPtr, segs.length);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortBooleanKeysShortValsSegments() throws Exception {
        boolean[] keys = booleanArray(N);
        short[] vals = shortArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.SHORT, keys.length, segsPtr, segs.length);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortBooleanKeysIntValsSegments() throws Exception {
        boolean[] keys = booleanArray(N);
        int[] vals = intArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.INT, keys.length, segsPtr, segs.length);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortBooleanKeysLongValsSegments() throws Exception {
        boolean[] keys = booleanArray(N);
        long[] vals = longArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.LONG, keys.length, segsPtr, segs.length);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortBooleanKeysFloatValsSegments() throws Exception {
        boolean[] keys = booleanArray(N);
        float[] vals = floatArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.FLOAT, keys.length, segsPtr, segs.length);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortBooleanKeysDoubleValsSegments() throws Exception {
        boolean[] keys = booleanArray(N);
        double[] vals = doubleArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.DOUBLE, keys.length, segsPtr, segs.length);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortByteKeysBooleanValsSegments() throws Exception {
        byte[] keys = byteArray(N);
        boolean[] vals = booleanArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.BOOLEAN, keys.length, segsPtr, segs.length);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortByteKeysByteValsSegments() throws Exception {
        byte[] keys = byteArray(N);
        byte[] vals = byteArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.BYTE, keys.length, segsPtr, segs.length);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortByteKeysCharValsSegments() throws Exception {
        byte[] keys = byteArray(N);
        char[] vals = charArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.CHAR, keys.length, segsPtr, segs.length);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortByteKeysShortValsSegments() throws Exception {
        byte[] keys = byteArray(N);
        short[] vals = shortArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.SHORT, keys.length, segsPtr, segs.length);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortByteKeysIntValsSegments() throws Exception {
        byte[] keys = byteArray(N);
        int[] vals = intArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.INT, keys.length, segsPtr, segs.length);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortByteKeysLongValsSegments() throws Exception {
        byte[] keys = byteArray(N);
        long[] vals = longArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.LONG, keys.length, segsPtr, segs.length);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortByteKeysFloatValsSegments() throws Exception {
        byte[] keys = byteArray(N);
        float[] vals = floatArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.FLOAT, keys.length, segsPtr, segs.length);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortByteKeysDoubleValsSegments() throws Exception {
        byte[] keys = byteArray(N);
        double[] vals = doubleArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.DOUBLE, keys.length, segsPtr, segs.length);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortCharKeysBooleanValsSegments() throws Exception {
        char[] keys = charArray(N);
        boolean[] vals = booleanArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.BOOLEAN, keys.length, segsPtr, segs.length);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortCharKeysByteValsSegments() throws Exception {
        char[] keys = charArray(N);
        byte[] vals = byteArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.BYTE, keys.length, segsPtr, segs.length);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortCharKeysCharValsSegments() throws Exception {
        char[] keys = charArray(N);
        char[] vals = charArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.CHAR, keys.length, segsPtr, segs.length);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortCharKeysShortValsSegments() throws Exception {
        char[] keys = charArray(N);
        short[] vals = shortArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.SHORT, keys.length, segsPtr, segs.length);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortCharKeysIntValsSegments() throws Exception {
        char[] keys = charArray(N);
        int[] vals = intArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.INT, keys.length, segsPtr, segs.length);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortCharKeysLongValsSegments() throws Exception {
        char[] keys = charArray(N);
        long[] vals = longArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.LONG, keys.length, segsPtr, segs.length);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortCharKeysFloatValsSegments() throws Exception {
        char[] keys = charArray(N);
        float[] vals = floatArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.FLOAT, keys.length, segsPtr, segs.length);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortCharKeysDoubleValsSegments() throws Exception {
        char[] keys = charArray(N);
        double[] vals = doubleArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.DOUBLE, keys.length, segsPtr, segs.length);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortShortKeysBooleanValsSegments() throws Exception {
        short[] keys = shortArray(N);
        boolean[] vals = booleanArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.BOOLEAN, keys.length, segsPtr, segs.length);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortShortKeysByteValsSegments() throws Exception {
        short[] keys = shortArray(N);
        byte[] vals = byteArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.BYTE, keys.length, segsPtr, segs.length);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortShortKeysCharValsSegments() throws Exception {
        short[] keys = shortArray(N);
        char[] vals = charArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.CHAR, keys.length, segsPtr, segs.length);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortShortKeysShortValsSegments() throws Exception {
        short[] keys = shortArray(N);
        short[] vals = shortArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.SHORT, keys.length, segsPtr, segs.length);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortShortKeysIntValsSegments() throws Exception {
        short[] keys = shortArray(N);
        int[] vals = intArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.INT, keys.length, segsPtr, segs.length);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortShortKeysLongValsSegments() throws Exception {
        short[] keys = shortArray(N);
        long[] vals = longArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.LONG, keys.length, segsPtr, segs.length);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortShortKeysFloatValsSegments() throws Exception {
        short[] keys = shortArray(N);
        float[] vals = floatArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.FLOAT, keys.length, segsPtr, segs.length);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortShortKeysDoubleValsSegments() throws Exception {
        short[] keys = shortArray(N);
        double[] vals = doubleArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.DOUBLE, keys.length, segsPtr, segs.length);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortIntKeysBooleanValsSegments() throws Exception {
        int[] keys = intArray(N);
        boolean[] vals = booleanArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.BOOLEAN, keys.length, segsPtr, segs.length);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortIntKeysByteValsSegments() throws Exception {
        int[] keys = intArray(N);
        byte[] vals = byteArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.BYTE, keys.length, segsPtr, segs.length);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortIntKeysCharValsSegments() throws Exception {
        int[] keys = intArray(N);
        char[] vals = charArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.CHAR, keys.length, segsPtr, segs.length);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortIntKeysShortValsSegments() throws Exception {
        int[] keys = intArray(N);
        short[] vals = shortArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.SHORT, keys.length, segsPtr, segs.length);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortIntKeysIntValsSegments() throws Exception {
        int[] keys = intArray(N);
        int[] vals = intArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.INT, keys.length, segsPtr, segs.length);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortIntKeysLongValsSegments() throws Exception {
        int[] keys = intArray(N);
        long[] vals = longArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.LONG, keys.length, segsPtr, segs.length);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortIntKeysFloatValsSegments() throws Exception {
        int[] keys = intArray(N);
        float[] vals = floatArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.FLOAT, keys.length, segsPtr, segs.length);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortIntKeysDoubleValsSegments() throws Exception {
        int[] keys = intArray(N);
        double[] vals = doubleArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.DOUBLE, keys.length, segsPtr, segs.length);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortLongKeysBooleanValsSegments() throws Exception {
        long[] keys = longArray(N);
        boolean[] vals = booleanArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.BOOLEAN, keys.length, segsPtr, segs.length);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortLongKeysByteValsSegments() throws Exception {
        long[] keys = longArray(N);
        byte[] vals = byteArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.BYTE, keys.length, segsPtr, segs.length);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortLongKeysCharValsSegments() throws Exception {
        long[] keys = longArray(N);
        char[] vals = charArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.CHAR, keys.length, segsPtr, segs.length);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortLongKeysShortValsSegments() throws Exception {
        long[] keys = longArray(N);
        short[] vals = shortArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.SHORT, keys.length, segsPtr, segs.length);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortLongKeysIntValsSegments() throws Exception {
        long[] keys = longArray(N);
        int[] vals = intArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.INT, keys.length, segsPtr, segs.length);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortLongKeysLongValsSegments() throws Exception {
        long[] keys = longArray(N);
        long[] vals = longArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.LONG, keys.length, segsPtr, segs.length);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortLongKeysFloatValsSegments() throws Exception {
        long[] keys = longArray(N);
        float[] vals = floatArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.FLOAT, keys.length, segsPtr, segs.length);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortLongKeysDoubleValsSegments() throws Exception {
        long[] keys = longArray(N);
        double[] vals = doubleArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.DOUBLE, keys.length, segsPtr, segs.length);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortFloatKeysBooleanValsSegments() throws Exception {
        float[] keys = floatArray(N);
        boolean[] vals = booleanArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.BOOLEAN, keys.length, segsPtr, segs.length);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortFloatKeysByteValsSegments() throws Exception {
        float[] keys = floatArray(N);
        byte[] vals = byteArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.BYTE, keys.length, segsPtr, segs.length);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortFloatKeysCharValsSegments() throws Exception {
        float[] keys = floatArray(N);
        char[] vals = charArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.CHAR, keys.length, segsPtr, segs.length);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortFloatKeysShortValsSegments() throws Exception {
        float[] keys = floatArray(N);
        short[] vals = shortArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.SHORT, keys.length, segsPtr, segs.length);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortFloatKeysIntValsSegments() throws Exception {
        float[] keys = floatArray(N);
        int[] vals = intArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.INT, keys.length, segsPtr, segs.length);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortFloatKeysLongValsSegments() throws Exception {
        float[] keys = floatArray(N);
        long[] vals = longArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.LONG, keys.length, segsPtr, segs.length);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortFloatKeysFloatValsSegments() throws Exception {
        float[] keys = floatArray(N);
        float[] vals = floatArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.FLOAT, keys.length, segsPtr, segs.length);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortFloatKeysDoubleValsSegments() throws Exception {
        float[] keys = floatArray(N);
        double[] vals = doubleArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.DOUBLE, keys.length, segsPtr, segs.length);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortDoubleKeysBooleanValsSegments() throws Exception {
        double[] keys = doubleArray(N);
        boolean[] vals = booleanArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.BOOLEAN, keys.length, segsPtr, segs.length);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortDoubleKeysByteValsSegments() throws Exception {
        double[] keys = doubleArray(N);
        byte[] vals = byteArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.BYTE, keys.length, segsPtr, segs.length);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortDoubleKeysCharValsSegments() throws Exception {
        double[] keys = doubleArray(N);
        char[] vals = charArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.CHAR, keys.length, segsPtr, segs.length);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortDoubleKeysShortValsSegments() throws Exception {
        double[] keys = doubleArray(N);
        short[] vals = shortArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.SHORT, keys.length, segsPtr, segs.length);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortDoubleKeysIntValsSegments() throws Exception {
        double[] keys = doubleArray(N);
        int[] vals = intArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.INT, keys.length, segsPtr, segs.length);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortDoubleKeysLongValsSegments() throws Exception {
        double[] keys = doubleArray(N);
        long[] vals = longArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.LONG, keys.length, segsPtr, segs.length);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortDoubleKeysFloatValsSegments() throws Exception {
        double[] keys = doubleArray(N);
        float[] vals = floatArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.FLOAT, keys.length, segsPtr, segs.length);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortDoubleKeysDoubleValsSegments() throws Exception {
        double[] keys = doubleArray(N);
        double[] vals = doubleArray(keys);
        int[] segs = segments(keys.length, SEGMENTS);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);
        long segsSize = segs.length * sizeOfItem(segs);
        CUdeviceptr segsPtr = devicePtr(segsSize);
        copy2device(segsPtr, segs);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.DOUBLE, keys.length, segsPtr, segs.length);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < segs.length; i++) {
            Arrays.sort(hostSortedKeys, segs[i], i < segs.length -1 ? segs[i + 1] : keys.length);
        }        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

}
