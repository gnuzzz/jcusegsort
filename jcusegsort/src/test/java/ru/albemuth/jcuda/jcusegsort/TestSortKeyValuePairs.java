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
public class TestSortKeyValuePairs {

    public static int N = 40000000;

    public static class Generator {
        public static void main(String[] args) {
            String[] types = {"boolean", "byte", "char", "short", "int", "long", "float", "double"};
            for (String keyType: types) {
                for (String valueType: types) {
                    String testMethodSrc = "    @Test\n" +
                            "    public void testSort" + flu(keyType) + "Keys" + flu(valueType) + "Vals() throws Exception {\n" +
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
                            "        Sorting.sort(keysPtr, Datatype." + keyType.toUpperCase() + ", valsPtr, Datatype." + valueType.toUpperCase() + ", keys.length);\n" +
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
    public void testSortBooleanKeysBooleanVals() throws Exception {
        boolean[] keys = booleanArray(N);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.BOOLEAN, keys.length);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysByteVals() throws Exception {
        boolean[] keys = booleanArray(N);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.BYTE, keys.length);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysCharVals() throws Exception {
        boolean[] keys = booleanArray(N);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.CHAR, keys.length);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysShortVals() throws Exception {
        boolean[] keys = booleanArray(N);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.SHORT, keys.length);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysIntVals() throws Exception {
        boolean[] keys = booleanArray(N);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.INT, keys.length);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysLongVals() throws Exception {
        boolean[] keys = booleanArray(N);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.LONG, keys.length);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysFloatVals() throws Exception {
        boolean[] keys = booleanArray(N);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.FLOAT, keys.length);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysDoubleVals() throws Exception {
        boolean[] keys = booleanArray(N);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.DOUBLE, keys.length);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysBooleanVals() throws Exception {
        byte[] keys = byteArray(N);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.BOOLEAN, keys.length);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysByteVals() throws Exception {
        byte[] keys = byteArray(N);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.BYTE, keys.length);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysCharVals() throws Exception {
        byte[] keys = byteArray(N);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.CHAR, keys.length);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysShortVals() throws Exception {
        byte[] keys = byteArray(N);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.SHORT, keys.length);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysIntVals() throws Exception {
        byte[] keys = byteArray(N);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.INT, keys.length);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysLongVals() throws Exception {
        byte[] keys = byteArray(N);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.LONG, keys.length);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysFloatVals() throws Exception {
        byte[] keys = byteArray(N);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.FLOAT, keys.length);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysDoubleVals() throws Exception {
        byte[] keys = byteArray(N);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.DOUBLE, keys.length);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        Arrays.sort(hostSortedKeys);
        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysBooleanVals() throws Exception {
        char[] keys = charArray(N);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.BOOLEAN, keys.length);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        Arrays.sort(hostSortedKeys);
        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysByteVals() throws Exception {
        char[] keys = charArray(N);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.BYTE, keys.length);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        Arrays.sort(hostSortedKeys);
        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysCharVals() throws Exception {
        char[] keys = charArray(N);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.CHAR, keys.length);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        Arrays.sort(hostSortedKeys);
        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysShortVals() throws Exception {
        char[] keys = charArray(N);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.SHORT, keys.length);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        Arrays.sort(hostSortedKeys);
        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysIntVals() throws Exception {
        char[] keys = charArray(N);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.INT, keys.length);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        Arrays.sort(hostSortedKeys);
        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysLongVals() throws Exception {
        char[] keys = charArray(N);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.LONG, keys.length);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        Arrays.sort(hostSortedKeys);
        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysFloatVals() throws Exception {
        char[] keys = charArray(N);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.FLOAT, keys.length);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        Arrays.sort(hostSortedKeys);
        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysDoubleVals() throws Exception {
        char[] keys = charArray(N);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.DOUBLE, keys.length);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        Arrays.sort(hostSortedKeys);
        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysBooleanVals() throws Exception {
        short[] keys = shortArray(N);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.BOOLEAN, keys.length);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        Arrays.sort(hostSortedKeys);
        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysByteVals() throws Exception {
        short[] keys = shortArray(N);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.BYTE, keys.length);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        Arrays.sort(hostSortedKeys);
        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysCharVals() throws Exception {
        short[] keys = shortArray(N);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.CHAR, keys.length);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        Arrays.sort(hostSortedKeys);
        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysShortVals() throws Exception {
        short[] keys = shortArray(N);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.SHORT, keys.length);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        Arrays.sort(hostSortedKeys);
        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysIntVals() throws Exception {
        short[] keys = shortArray(N);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.INT, keys.length);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        Arrays.sort(hostSortedKeys);
        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysLongVals() throws Exception {
        short[] keys = shortArray(N);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.LONG, keys.length);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        Arrays.sort(hostSortedKeys);
        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysFloatVals() throws Exception {
        short[] keys = shortArray(N);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.FLOAT, keys.length);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        Arrays.sort(hostSortedKeys);
        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysDoubleVals() throws Exception {
        short[] keys = shortArray(N);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.DOUBLE, keys.length);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        Arrays.sort(hostSortedKeys);
        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysBooleanVals() throws Exception {
        int[] keys = intArray(N);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.BOOLEAN, keys.length);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        Arrays.sort(hostSortedKeys);
        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysByteVals() throws Exception {
        int[] keys = intArray(N);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.BYTE, keys.length);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        Arrays.sort(hostSortedKeys);
        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysCharVals() throws Exception {
        int[] keys = intArray(N);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.CHAR, keys.length);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        Arrays.sort(hostSortedKeys);
        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysShortVals() throws Exception {
        int[] keys = intArray(N);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.SHORT, keys.length);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        Arrays.sort(hostSortedKeys);
        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysIntVals() throws Exception {
        int[] keys = intArray(N);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.INT, keys.length);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        Arrays.sort(hostSortedKeys);
        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysLongVals() throws Exception {
        int[] keys = intArray(N);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.LONG, keys.length);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        Arrays.sort(hostSortedKeys);
        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysFloatVals() throws Exception {
        int[] keys = intArray(N);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.FLOAT, keys.length);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        Arrays.sort(hostSortedKeys);
        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysDoubleVals() throws Exception {
        int[] keys = intArray(N);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.DOUBLE, keys.length);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        Arrays.sort(hostSortedKeys);
        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysBooleanVals() throws Exception {
        long[] keys = longArray(N);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.BOOLEAN, keys.length);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        Arrays.sort(hostSortedKeys);
        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysByteVals() throws Exception {
        long[] keys = longArray(N);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.BYTE, keys.length);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        Arrays.sort(hostSortedKeys);
        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysCharVals() throws Exception {
        long[] keys = longArray(N);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.CHAR, keys.length);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        Arrays.sort(hostSortedKeys);
        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysShortVals() throws Exception {
        long[] keys = longArray(N);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.SHORT, keys.length);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        Arrays.sort(hostSortedKeys);
        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysIntVals() throws Exception {
        long[] keys = longArray(N);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.INT, keys.length);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        Arrays.sort(hostSortedKeys);
        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysLongVals() throws Exception {
        long[] keys = longArray(N);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.LONG, keys.length);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        Arrays.sort(hostSortedKeys);
        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysFloatVals() throws Exception {
        long[] keys = longArray(N);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.FLOAT, keys.length);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        Arrays.sort(hostSortedKeys);
        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysDoubleVals() throws Exception {
        long[] keys = longArray(N);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.DOUBLE, keys.length);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        Arrays.sort(hostSortedKeys);
        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysBooleanVals() throws Exception {
        float[] keys = floatArray(N);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.BOOLEAN, keys.length);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        Arrays.sort(hostSortedKeys);
        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysByteVals() throws Exception {
        float[] keys = floatArray(N);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.BYTE, keys.length);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        Arrays.sort(hostSortedKeys);
        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysCharVals() throws Exception {
        float[] keys = floatArray(N);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.CHAR, keys.length);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        Arrays.sort(hostSortedKeys);
        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysShortVals() throws Exception {
        float[] keys = floatArray(N);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.SHORT, keys.length);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        Arrays.sort(hostSortedKeys);
        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysIntVals() throws Exception {
        float[] keys = floatArray(N);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.INT, keys.length);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        Arrays.sort(hostSortedKeys);
        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysLongVals() throws Exception {
        float[] keys = floatArray(N);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.LONG, keys.length);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        Arrays.sort(hostSortedKeys);
        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysFloatVals() throws Exception {
        float[] keys = floatArray(N);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.FLOAT, keys.length);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        Arrays.sort(hostSortedKeys);
        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysDoubleVals() throws Exception {
        float[] keys = floatArray(N);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.DOUBLE, keys.length);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        Arrays.sort(hostSortedKeys);
        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysBooleanVals() throws Exception {
        double[] keys = doubleArray(N);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.BOOLEAN, keys.length);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        Arrays.sort(hostSortedKeys);
        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysByteVals() throws Exception {
        double[] keys = doubleArray(N);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.BYTE, keys.length);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        Arrays.sort(hostSortedKeys);
        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysCharVals() throws Exception {
        double[] keys = doubleArray(N);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.CHAR, keys.length);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        Arrays.sort(hostSortedKeys);
        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysShortVals() throws Exception {
        double[] keys = doubleArray(N);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.SHORT, keys.length);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        Arrays.sort(hostSortedKeys);
        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysIntVals() throws Exception {
        double[] keys = doubleArray(N);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.INT, keys.length);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        Arrays.sort(hostSortedKeys);
        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysLongVals() throws Exception {
        double[] keys = doubleArray(N);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.LONG, keys.length);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        Arrays.sort(hostSortedKeys);
        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysFloatVals() throws Exception {
        double[] keys = doubleArray(N);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.FLOAT, keys.length);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        Arrays.sort(hostSortedKeys);
        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysDoubleVals() throws Exception {
        double[] keys = doubleArray(N);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.DOUBLE, keys.length);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        Arrays.sort(hostSortedKeys);
        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

}
