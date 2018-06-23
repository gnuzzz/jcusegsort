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
public class TestSortKeyValuePairsSegmentsWithContext {

    public static int N = 40000000;
    public static int SEGMENTS = 5000;

    public static class Generator {
        public static void main(String[] args) {
            String[] types = {"boolean", "byte", "char", "short", "int", "long", "float", "double"};
            for (String keyType : types) {
                for (String valueType : types) {
                    String testMethodSrc = "    @Test\n" +
                            "    public void testSort" + flu(keyType) + "Keys" + flu(valueType) + "ValsSegmentsWithContext() throws Exception {\n" +
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
                            "        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype." + keyType.toUpperCase() + ", Datatype." + valueType.toUpperCase() + ", keys.length, segs.length);\n" +
                            "        Sorting.sort(keysPtr, Datatype." + keyType.toUpperCase() + ", valsPtr, Datatype." + valueType.toUpperCase() + ", keys.length, segsPtr, segs.length, context);\n" +
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
                            "        context.free();\n" +
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
    public void testSortBooleanKeysBooleanValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.BOOLEAN, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.BOOLEAN, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortBooleanKeysByteValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.BYTE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.BYTE, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortBooleanKeysCharValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.CHAR, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.CHAR, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortBooleanKeysShortValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.SHORT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.SHORT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortBooleanKeysIntValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.INT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.INT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortBooleanKeysLongValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.LONG, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.LONG, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortBooleanKeysFloatValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.FLOAT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.FLOAT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortBooleanKeysDoubleValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.DOUBLE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.DOUBLE, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortByteKeysBooleanValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.BOOLEAN, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.BOOLEAN, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortByteKeysByteValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.BYTE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.BYTE, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortByteKeysCharValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.CHAR, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.CHAR, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortByteKeysShortValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.SHORT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.SHORT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortByteKeysIntValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.INT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.INT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortByteKeysLongValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.LONG, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.LONG, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortByteKeysFloatValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.FLOAT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.FLOAT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortByteKeysDoubleValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.DOUBLE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.DOUBLE, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortCharKeysBooleanValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.BOOLEAN, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.BOOLEAN, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortCharKeysByteValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.BYTE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.BYTE, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortCharKeysCharValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.CHAR, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.CHAR, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortCharKeysShortValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.SHORT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.SHORT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortCharKeysIntValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.INT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.INT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortCharKeysLongValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.LONG, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.LONG, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortCharKeysFloatValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.FLOAT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.FLOAT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortCharKeysDoubleValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.DOUBLE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.DOUBLE, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortShortKeysBooleanValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.BOOLEAN, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.BOOLEAN, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortShortKeysByteValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.BYTE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.BYTE, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortShortKeysCharValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.CHAR, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.CHAR, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortShortKeysShortValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.SHORT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.SHORT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortShortKeysIntValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.INT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.INT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortShortKeysLongValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.LONG, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.LONG, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortShortKeysFloatValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.FLOAT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.FLOAT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortShortKeysDoubleValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.DOUBLE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.DOUBLE, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortIntKeysBooleanValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.BOOLEAN, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.BOOLEAN, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortIntKeysByteValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.BYTE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.BYTE, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortIntKeysCharValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.CHAR, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.CHAR, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortIntKeysShortValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.SHORT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.SHORT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortIntKeysIntValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.INT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.INT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortIntKeysLongValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.LONG, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.LONG, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortIntKeysFloatValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.FLOAT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.FLOAT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortIntKeysDoubleValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.DOUBLE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.DOUBLE, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortLongKeysBooleanValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.BOOLEAN, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.BOOLEAN, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortLongKeysByteValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.BYTE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.BYTE, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortLongKeysCharValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.CHAR, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.CHAR, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortLongKeysShortValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.SHORT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.SHORT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortLongKeysIntValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.INT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.INT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortLongKeysLongValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.LONG, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.LONG, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortLongKeysFloatValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.FLOAT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.FLOAT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortLongKeysDoubleValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.DOUBLE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.DOUBLE, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortFloatKeysBooleanValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.BOOLEAN, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.BOOLEAN, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortFloatKeysByteValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.BYTE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.BYTE, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortFloatKeysCharValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.CHAR, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.CHAR, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortFloatKeysShortValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.SHORT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.SHORT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortFloatKeysIntValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.INT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.INT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortFloatKeysLongValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.LONG, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.LONG, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortFloatKeysFloatValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.FLOAT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.FLOAT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortFloatKeysDoubleValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.DOUBLE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.DOUBLE, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortDoubleKeysBooleanValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.BOOLEAN, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.BOOLEAN, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortDoubleKeysByteValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.BYTE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.BYTE, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortDoubleKeysCharValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.CHAR, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.CHAR, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortDoubleKeysShortValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.SHORT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.SHORT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortDoubleKeysIntValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.INT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.INT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortDoubleKeysLongValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.LONG, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.LONG, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortDoubleKeysFloatValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.FLOAT, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.FLOAT, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

    @Test
    public void testSortDoubleKeysDoubleValsSegmentsWithContext() throws Exception {
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
        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.DOUBLE, keys.length, segs.length);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.DOUBLE, keys.length, segsPtr, segs.length, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
        deletePtr(segsPtr);
    }

}
