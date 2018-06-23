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
import static ru.albemuth.jcuda.jcusegsort.TestUtils.shortArray;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.showMemory;
import static ru.albemuth.jcuda.jcusegsort.TestUtils.sizeOfItem;

/**
 * @author Vladimir Kornyshev { @literal <gnuzzz@mail.ru>}
 */
public class TestSortKeyValuePairsWithContext {

    public static int N = 40000000;

    public static class Generator {
        public static void main(String[] args) {
            String[] types = {"boolean", "byte", "char", "short", "int", "long", "float", "double"};
            for (String keyType: types) {
                for (String valueType: types) {
                    String testMethodSrc = "    @Test\n" +
                            "    public void testSort" + flu(keyType) + "Keys" + flu(valueType) + "ValsWithContext() throws Exception {\n" +
                            "        " + keyType + "[] keys = " + keyType +"Array(N);\n" +
                            "        " + valueType + "[] vals = " + valueType + "Array(keys);\n" +
                            "\n" +
                            "        long keysSize = keys.length * sizeOfItem(keys);\n" +
                            "        CUdeviceptr keysPtr = devicePtr(keysSize);\n" +
                            "        copy2device(keysPtr, keys);\n" +
                            "        long valsSize = vals.length * sizeOfItem(vals);\n" +
                            "        CUdeviceptr valsPtr = devicePtr(valsSize);\n" +
                            "        copy2device(valsPtr, vals);\n" +
                            "\n" +
                            "        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype." + keyType.toUpperCase() + ", Datatype." + valueType.toUpperCase() + ", keys.length, 1);\n" +
                            "        Sorting.sort(keysPtr, Datatype." + keyType.toUpperCase() + ", valsPtr, Datatype." + valueType.toUpperCase() + ", keys.length, context);\n" +
                            "\n" +
                            "        " + keyType + "[] sortedKeys = new " + keyType + "[keys.length];\n" +
                            "        copy2host(sortedKeys, keysPtr);\n" +
                            "        " + valueType + "[] sortedVals = new " + valueType + "[vals.length];\n" +
                            "        copy2host(sortedVals, valsPtr);\n" +
                            "\n" +
                            ("boolean".equals(keyType) ?
                            "        byte[] hostSortedKeys = byteArray(keys);\n" :
                            "        " + keyType + "[] hostSortedKeys = " + keyType + "Array(keys);\n") +

                            "        Arrays.sort(hostSortedKeys);\n" +
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
    public void testSortBooleanKeysBooleanValsWithContext() throws Exception {
        boolean[] keys = booleanArray(N);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.BOOLEAN, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.BOOLEAN, keys.length, context);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysByteValsWithContext() throws Exception {
        boolean[] keys = booleanArray(N);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.BYTE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.BYTE, keys.length, context);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysCharValsWithContext() throws Exception {
        boolean[] keys = booleanArray(N);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.CHAR, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.CHAR, keys.length, context);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysShortValsWithContext() throws Exception {
        boolean[] keys = booleanArray(N);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.SHORT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.SHORT, keys.length, context);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysIntValsWithContext() throws Exception {
        boolean[] keys = booleanArray(N);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.INT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.INT, keys.length, context);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysLongValsWithContext() throws Exception {
        boolean[] keys = booleanArray(N);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.LONG, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.LONG, keys.length, context);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysFloatValsWithContext() throws Exception {
        boolean[] keys = booleanArray(N);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.FLOAT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.FLOAT, keys.length, context);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysDoubleValsWithContext() throws Exception {
        boolean[] keys = booleanArray(N);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.DOUBLE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.DOUBLE, keys.length, context);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysBooleanValsWithContext() throws Exception {
        byte[] keys = byteArray(N);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.BOOLEAN, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.BOOLEAN, keys.length, context);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysByteValsWithContext() throws Exception {
        byte[] keys = byteArray(N);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.BYTE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.BYTE, keys.length, context);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysCharValsWithContext() throws Exception {
        byte[] keys = byteArray(N);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.CHAR, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.CHAR, keys.length, context);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysShortValsWithContext() throws Exception {
        byte[] keys = byteArray(N);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.SHORT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.SHORT, keys.length, context);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysIntValsWithContext() throws Exception {
        byte[] keys = byteArray(N);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.INT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.INT, keys.length, context);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysLongValsWithContext() throws Exception {
        byte[] keys = byteArray(N);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.LONG, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.LONG, keys.length, context);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysFloatValsWithContext() throws Exception {
        byte[] keys = byteArray(N);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.FLOAT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.FLOAT, keys.length, context);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysDoubleValsWithContext() throws Exception {
        byte[] keys = byteArray(N);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.DOUBLE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.DOUBLE, keys.length, context);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysBooleanValsWithContext() throws Exception {
        char[] keys = charArray(N);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.BOOLEAN, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.BOOLEAN, keys.length, context);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        Arrays.sort(hostSortedKeys);
        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysByteValsWithContext() throws Exception {
        char[] keys = charArray(N);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.BYTE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.BYTE, keys.length, context);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        Arrays.sort(hostSortedKeys);
        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysCharValsWithContext() throws Exception {
        char[] keys = charArray(N);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.CHAR, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.CHAR, keys.length, context);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        Arrays.sort(hostSortedKeys);
        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysShortValsWithContext() throws Exception {
        char[] keys = charArray(N);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.SHORT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.SHORT, keys.length, context);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        Arrays.sort(hostSortedKeys);
        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysIntValsWithContext() throws Exception {
        char[] keys = charArray(N);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.INT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.INT, keys.length, context);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        Arrays.sort(hostSortedKeys);
        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysLongValsWithContext() throws Exception {
        char[] keys = charArray(N);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.LONG, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.LONG, keys.length, context);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        Arrays.sort(hostSortedKeys);
        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysFloatValsWithContext() throws Exception {
        char[] keys = charArray(N);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.FLOAT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.FLOAT, keys.length, context);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        Arrays.sort(hostSortedKeys);
        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysDoubleValsWithContext() throws Exception {
        char[] keys = charArray(N);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.DOUBLE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.DOUBLE, keys.length, context);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        Arrays.sort(hostSortedKeys);
        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysBooleanValsWithContext() throws Exception {
        short[] keys = shortArray(N);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.BOOLEAN, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.BOOLEAN, keys.length, context);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        Arrays.sort(hostSortedKeys);
        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysByteValsWithContext() throws Exception {
        short[] keys = shortArray(N);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.BYTE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.BYTE, keys.length, context);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        Arrays.sort(hostSortedKeys);
        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysCharValsWithContext() throws Exception {
        short[] keys = shortArray(N);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.CHAR, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.CHAR, keys.length, context);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        Arrays.sort(hostSortedKeys);
        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysShortValsWithContext() throws Exception {
        short[] keys = shortArray(N);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.SHORT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.SHORT, keys.length, context);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        Arrays.sort(hostSortedKeys);
        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysIntValsWithContext() throws Exception {
        short[] keys = shortArray(N);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.INT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.INT, keys.length, context);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        Arrays.sort(hostSortedKeys);
        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysLongValsWithContext() throws Exception {
        short[] keys = shortArray(N);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.LONG, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.LONG, keys.length, context);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        Arrays.sort(hostSortedKeys);
        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysFloatValsWithContext() throws Exception {
        short[] keys = shortArray(N);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.FLOAT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.FLOAT, keys.length, context);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        Arrays.sort(hostSortedKeys);
        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysDoubleValsWithContext() throws Exception {
        short[] keys = shortArray(N);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.DOUBLE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.DOUBLE, keys.length, context);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        Arrays.sort(hostSortedKeys);
        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysBooleanValsWithContext() throws Exception {
        int[] keys = intArray(N);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.BOOLEAN, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.BOOLEAN, keys.length, context);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        Arrays.sort(hostSortedKeys);
        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysByteValsWithContext() throws Exception {
        int[] keys = intArray(N);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.BYTE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.BYTE, keys.length, context);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        Arrays.sort(hostSortedKeys);
        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysCharValsWithContext() throws Exception {
        int[] keys = intArray(N);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.CHAR, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.CHAR, keys.length, context);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        Arrays.sort(hostSortedKeys);
        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysShortValsWithContext() throws Exception {
        int[] keys = intArray(N);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.SHORT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.SHORT, keys.length, context);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        Arrays.sort(hostSortedKeys);
        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysIntValsWithContext() throws Exception {
        int[] keys = intArray(N);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.INT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.INT, keys.length, context);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        Arrays.sort(hostSortedKeys);
        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysLongValsWithContext() throws Exception {
        int[] keys = intArray(N);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.LONG, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.LONG, keys.length, context);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        Arrays.sort(hostSortedKeys);
        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysFloatValsWithContext() throws Exception {
        int[] keys = intArray(N);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.FLOAT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.FLOAT, keys.length, context);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        Arrays.sort(hostSortedKeys);
        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysDoubleValsWithContext() throws Exception {
        int[] keys = intArray(N);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.DOUBLE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.DOUBLE, keys.length, context);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        Arrays.sort(hostSortedKeys);
        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysBooleanValsWithContext() throws Exception {
        long[] keys = longArray(N);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.BOOLEAN, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.BOOLEAN, keys.length, context);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        Arrays.sort(hostSortedKeys);
        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysByteValsWithContext() throws Exception {
        long[] keys = longArray(N);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.BYTE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.BYTE, keys.length, context);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        Arrays.sort(hostSortedKeys);
        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysCharValsWithContext() throws Exception {
        long[] keys = longArray(N);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.CHAR, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.CHAR, keys.length, context);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        Arrays.sort(hostSortedKeys);
        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysShortValsWithContext() throws Exception {
        long[] keys = longArray(N);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.SHORT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.SHORT, keys.length, context);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        Arrays.sort(hostSortedKeys);
        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysIntValsWithContext() throws Exception {
        long[] keys = longArray(N);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.INT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.INT, keys.length, context);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        Arrays.sort(hostSortedKeys);
        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysLongValsWithContext() throws Exception {
        long[] keys = longArray(N);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.LONG, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.LONG, keys.length, context);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        Arrays.sort(hostSortedKeys);
        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysFloatValsWithContext() throws Exception {
        long[] keys = longArray(N);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.FLOAT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.FLOAT, keys.length, context);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        Arrays.sort(hostSortedKeys);
        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysDoubleValsWithContext() throws Exception {
        long[] keys = longArray(N);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.DOUBLE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.DOUBLE, keys.length, context);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        Arrays.sort(hostSortedKeys);
        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysBooleanValsWithContext() throws Exception {
        float[] keys = floatArray(N);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.BOOLEAN, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.BOOLEAN, keys.length, context);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        Arrays.sort(hostSortedKeys);
        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysByteValsWithContext() throws Exception {
        float[] keys = floatArray(N);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.BYTE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.BYTE, keys.length, context);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        Arrays.sort(hostSortedKeys);
        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysCharValsWithContext() throws Exception {
        float[] keys = floatArray(N);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.CHAR, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.CHAR, keys.length, context);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        Arrays.sort(hostSortedKeys);
        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysShortValsWithContext() throws Exception {
        float[] keys = floatArray(N);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.SHORT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.SHORT, keys.length, context);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        Arrays.sort(hostSortedKeys);
        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysIntValsWithContext() throws Exception {
        float[] keys = floatArray(N);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.INT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.INT, keys.length, context);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        Arrays.sort(hostSortedKeys);
        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysLongValsWithContext() throws Exception {
        float[] keys = floatArray(N);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.LONG, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.LONG, keys.length, context);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        Arrays.sort(hostSortedKeys);
        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysFloatValsWithContext() throws Exception {
        float[] keys = floatArray(N);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.FLOAT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.FLOAT, keys.length, context);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        Arrays.sort(hostSortedKeys);
        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysDoubleValsWithContext() throws Exception {
        float[] keys = floatArray(N);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.DOUBLE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.DOUBLE, keys.length, context);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        Arrays.sort(hostSortedKeys);
        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysBooleanValsWithContext() throws Exception {
        double[] keys = doubleArray(N);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.BOOLEAN, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.BOOLEAN, keys.length, context);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        Arrays.sort(hostSortedKeys);
        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysByteValsWithContext() throws Exception {
        double[] keys = doubleArray(N);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.BYTE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.BYTE, keys.length, context);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        Arrays.sort(hostSortedKeys);
        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysCharValsWithContext() throws Exception {
        double[] keys = doubleArray(N);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.CHAR, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.CHAR, keys.length, context);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        Arrays.sort(hostSortedKeys);
        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysShortValsWithContext() throws Exception {
        double[] keys = doubleArray(N);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.SHORT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.SHORT, keys.length, context);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        Arrays.sort(hostSortedKeys);
        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysIntValsWithContext() throws Exception {
        double[] keys = doubleArray(N);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.INT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.INT, keys.length, context);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        Arrays.sort(hostSortedKeys);
        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysLongValsWithContext() throws Exception {
        double[] keys = doubleArray(N);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.LONG, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.LONG, keys.length, context);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        Arrays.sort(hostSortedKeys);
        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysFloatValsWithContext() throws Exception {
        double[] keys = doubleArray(N);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.FLOAT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.FLOAT, keys.length, context);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        Arrays.sort(hostSortedKeys);
        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysDoubleValsWithContext() throws Exception {
        double[] keys = doubleArray(N);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.DOUBLE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.DOUBLE, keys.length, context);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        Arrays.sort(hostSortedKeys);
        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

}
