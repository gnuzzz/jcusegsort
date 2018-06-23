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
public class TestSortKeyValuePairsMatrix {

    public static int ROWS = 5000;
    public static int COLS = 8000;

    public static class Generator {
        public static void main(String[] args) {
            String[] types = {"boolean", "byte", "char", "short", "int", "long", "float", "double"};
            for (String keyType: types) {
                for (String valueType: types) {
                    String testMethodSrc = "    @Test\n" +
                            "    public void testSort" + flu(keyType) + "Keys" + flu(valueType) + "ValsMatrixWithContext() throws Exception {\n" +
                            "        " + keyType + "[] keys = " + keyType +"Array(ROWS * COLS);\n" +
                            "        " + valueType + "[] vals = " + valueType + "Array(keys);\n" +
                            "\n" +
                            "        long keysSize = keys.length * sizeOfItem(keys);\n" +
                            "        CUdeviceptr keysPtr = devicePtr(keysSize);\n" +
                            "        copy2device(keysPtr, keys);\n" +
                            "        long valsSize = vals.length * sizeOfItem(vals);\n" +
                            "        CUdeviceptr valsPtr = devicePtr(valsSize);\n" +
                            "        copy2device(valsPtr, vals);\n" +
                            "\n" +
                            "        Sorting.sort(keysPtr, Datatype." + keyType.toUpperCase() + ", valsPtr, Datatype." + valueType.toUpperCase() + ", ROWS, COLS);\n" +
                            "\n" +
                            "        " + keyType + "[] sortedKeys = new " + keyType + "[keys.length];\n" +
                            "        copy2host(sortedKeys, keysPtr);\n" +
                            "        " + valueType + "[] sortedVals = new " + valueType + "[vals.length];\n" +
                            "        copy2host(sortedVals, valsPtr);\n" +
                            "\n" +
                            ("boolean".equals(keyType) ?
                                    "        byte[] hostSortedKeys = byteArray(keys);\n" :
                                    "        " + keyType + "[] hostSortedKeys = " + keyType + "Array(keys);\n") +

                            "        for (int i = 0; i < ROWS; i++) {\n" +
                            "            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);\n" +
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
    public void testSortBooleanKeysBooleanValsMatrixWithContext() throws Exception {
        boolean[] keys = booleanArray(ROWS * COLS);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.BOOLEAN, ROWS, COLS);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysByteValsMatrixWithContext() throws Exception {
        boolean[] keys = booleanArray(ROWS * COLS);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.BYTE, ROWS, COLS);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysCharValsMatrixWithContext() throws Exception {
        boolean[] keys = booleanArray(ROWS * COLS);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.CHAR, ROWS, COLS);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysShortValsMatrixWithContext() throws Exception {
        boolean[] keys = booleanArray(ROWS * COLS);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.SHORT, ROWS, COLS);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysIntValsMatrixWithContext() throws Exception {
        boolean[] keys = booleanArray(ROWS * COLS);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.INT, ROWS, COLS);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysLongValsMatrixWithContext() throws Exception {
        boolean[] keys = booleanArray(ROWS * COLS);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.LONG, ROWS, COLS);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysFloatValsMatrixWithContext() throws Exception {
        boolean[] keys = booleanArray(ROWS * COLS);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.FLOAT, ROWS, COLS);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortBooleanKeysDoubleValsMatrixWithContext() throws Exception {
        boolean[] keys = booleanArray(ROWS * COLS);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.DOUBLE, ROWS, COLS);

        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(byteArray(sortedKeys), hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysBooleanValsMatrixWithContext() throws Exception {
        byte[] keys = byteArray(ROWS * COLS);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.BOOLEAN, ROWS, COLS);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysByteValsMatrixWithContext() throws Exception {
        byte[] keys = byteArray(ROWS * COLS);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.BYTE, ROWS, COLS);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysCharValsMatrixWithContext() throws Exception {
        byte[] keys = byteArray(ROWS * COLS);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.CHAR, ROWS, COLS);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysShortValsMatrixWithContext() throws Exception {
        byte[] keys = byteArray(ROWS * COLS);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.SHORT, ROWS, COLS);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysIntValsMatrixWithContext() throws Exception {
        byte[] keys = byteArray(ROWS * COLS);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.INT, ROWS, COLS);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysLongValsMatrixWithContext() throws Exception {
        byte[] keys = byteArray(ROWS * COLS);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.LONG, ROWS, COLS);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysFloatValsMatrixWithContext() throws Exception {
        byte[] keys = byteArray(ROWS * COLS);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.FLOAT, ROWS, COLS);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortByteKeysDoubleValsMatrixWithContext() throws Exception {
        byte[] keys = byteArray(ROWS * COLS);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.DOUBLE, ROWS, COLS);

        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        byte[] hostSortedKeys = byteArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysBooleanValsMatrixWithContext() throws Exception {
        char[] keys = charArray(ROWS * COLS);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.BOOLEAN, ROWS, COLS);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysByteValsMatrixWithContext() throws Exception {
        char[] keys = charArray(ROWS * COLS);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.BYTE, ROWS, COLS);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysCharValsMatrixWithContext() throws Exception {
        char[] keys = charArray(ROWS * COLS);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.CHAR, ROWS, COLS);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysShortValsMatrixWithContext() throws Exception {
        char[] keys = charArray(ROWS * COLS);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.SHORT, ROWS, COLS);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysIntValsMatrixWithContext() throws Exception {
        char[] keys = charArray(ROWS * COLS);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.INT, ROWS, COLS);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysLongValsMatrixWithContext() throws Exception {
        char[] keys = charArray(ROWS * COLS);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.LONG, ROWS, COLS);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysFloatValsMatrixWithContext() throws Exception {
        char[] keys = charArray(ROWS * COLS);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.FLOAT, ROWS, COLS);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortCharKeysDoubleValsMatrixWithContext() throws Exception {
        char[] keys = charArray(ROWS * COLS);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.DOUBLE, ROWS, COLS);

        char[] sortedKeys = new char[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        char[] hostSortedKeys = charArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysBooleanValsMatrixWithContext() throws Exception {
        short[] keys = shortArray(ROWS * COLS);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.BOOLEAN, ROWS, COLS);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysByteValsMatrixWithContext() throws Exception {
        short[] keys = shortArray(ROWS * COLS);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.BYTE, ROWS, COLS);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysCharValsMatrixWithContext() throws Exception {
        short[] keys = shortArray(ROWS * COLS);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.CHAR, ROWS, COLS);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysShortValsMatrixWithContext() throws Exception {
        short[] keys = shortArray(ROWS * COLS);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.SHORT, ROWS, COLS);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysIntValsMatrixWithContext() throws Exception {
        short[] keys = shortArray(ROWS * COLS);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.INT, ROWS, COLS);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysLongValsMatrixWithContext() throws Exception {
        short[] keys = shortArray(ROWS * COLS);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.LONG, ROWS, COLS);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysFloatValsMatrixWithContext() throws Exception {
        short[] keys = shortArray(ROWS * COLS);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.FLOAT, ROWS, COLS);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortShortKeysDoubleValsMatrixWithContext() throws Exception {
        short[] keys = shortArray(ROWS * COLS);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.DOUBLE, ROWS, COLS);

        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        short[] hostSortedKeys = shortArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysBooleanValsMatrixWithContext() throws Exception {
        int[] keys = intArray(ROWS * COLS);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.BOOLEAN, ROWS, COLS);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysByteValsMatrixWithContext() throws Exception {
        int[] keys = intArray(ROWS * COLS);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.BYTE, ROWS, COLS);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysCharValsMatrixWithContext() throws Exception {
        int[] keys = intArray(ROWS * COLS);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.CHAR, ROWS, COLS);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysShortValsMatrixWithContext() throws Exception {
        int[] keys = intArray(ROWS * COLS);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.SHORT, ROWS, COLS);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysIntValsMatrixWithContext() throws Exception {
        int[] keys = intArray(ROWS * COLS);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.INT, ROWS, COLS);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysLongValsMatrixWithContext() throws Exception {
        int[] keys = intArray(ROWS * COLS);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.LONG, ROWS, COLS);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysFloatValsMatrixWithContext() throws Exception {
        int[] keys = intArray(ROWS * COLS);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.FLOAT, ROWS, COLS);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortIntKeysDoubleValsMatrixWithContext() throws Exception {
        int[] keys = intArray(ROWS * COLS);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.DOUBLE, ROWS, COLS);

        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        int[] hostSortedKeys = intArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysBooleanValsMatrixWithContext() throws Exception {
        long[] keys = longArray(ROWS * COLS);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.BOOLEAN, ROWS, COLS);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysByteValsMatrixWithContext() throws Exception {
        long[] keys = longArray(ROWS * COLS);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.BYTE, ROWS, COLS);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysCharValsMatrixWithContext() throws Exception {
        long[] keys = longArray(ROWS * COLS);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.CHAR, ROWS, COLS);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysShortValsMatrixWithContext() throws Exception {
        long[] keys = longArray(ROWS * COLS);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.SHORT, ROWS, COLS);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysIntValsMatrixWithContext() throws Exception {
        long[] keys = longArray(ROWS * COLS);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.INT, ROWS, COLS);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysLongValsMatrixWithContext() throws Exception {
        long[] keys = longArray(ROWS * COLS);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.LONG, ROWS, COLS);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysFloatValsMatrixWithContext() throws Exception {
        long[] keys = longArray(ROWS * COLS);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.FLOAT, ROWS, COLS);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortLongKeysDoubleValsMatrixWithContext() throws Exception {
        long[] keys = longArray(ROWS * COLS);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.DOUBLE, ROWS, COLS);

        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        long[] hostSortedKeys = longArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysBooleanValsMatrixWithContext() throws Exception {
        float[] keys = floatArray(ROWS * COLS);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.BOOLEAN, ROWS, COLS);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysByteValsMatrixWithContext() throws Exception {
        float[] keys = floatArray(ROWS * COLS);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.BYTE, ROWS, COLS);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysCharValsMatrixWithContext() throws Exception {
        float[] keys = floatArray(ROWS * COLS);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.CHAR, ROWS, COLS);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysShortValsMatrixWithContext() throws Exception {
        float[] keys = floatArray(ROWS * COLS);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.SHORT, ROWS, COLS);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysIntValsMatrixWithContext() throws Exception {
        float[] keys = floatArray(ROWS * COLS);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.INT, ROWS, COLS);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysLongValsMatrixWithContext() throws Exception {
        float[] keys = floatArray(ROWS * COLS);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.LONG, ROWS, COLS);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysFloatValsMatrixWithContext() throws Exception {
        float[] keys = floatArray(ROWS * COLS);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.FLOAT, ROWS, COLS);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortFloatKeysDoubleValsMatrixWithContext() throws Exception {
        float[] keys = floatArray(ROWS * COLS);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.DOUBLE, ROWS, COLS);

        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        float[] hostSortedKeys = floatArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysBooleanValsMatrixWithContext() throws Exception {
        double[] keys = doubleArray(ROWS * COLS);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.BOOLEAN, ROWS, COLS);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        boolean[] sortedVals = new boolean[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        boolean[] hostSortedVals = booleanArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(byteArray(sortedVals), byteArray(hostSortedVals));

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysByteValsMatrixWithContext() throws Exception {
        double[] keys = doubleArray(ROWS * COLS);
        byte[] vals = byteArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.BYTE, ROWS, COLS);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        byte[] sortedVals = new byte[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        byte[] hostSortedVals = byteArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysCharValsMatrixWithContext() throws Exception {
        double[] keys = doubleArray(ROWS * COLS);
        char[] vals = charArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.CHAR, ROWS, COLS);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        char[] sortedVals = new char[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        char[] hostSortedVals = charArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysShortValsMatrixWithContext() throws Exception {
        double[] keys = doubleArray(ROWS * COLS);
        short[] vals = shortArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.SHORT, ROWS, COLS);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        short[] sortedVals = new short[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        short[] hostSortedVals = shortArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysIntValsMatrixWithContext() throws Exception {
        double[] keys = doubleArray(ROWS * COLS);
        int[] vals = intArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.INT, ROWS, COLS);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        int[] sortedVals = new int[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        int[] hostSortedVals = intArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysLongValsMatrixWithContext() throws Exception {
        double[] keys = doubleArray(ROWS * COLS);
        long[] vals = longArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.LONG, ROWS, COLS);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        long[] sortedVals = new long[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        long[] hostSortedVals = longArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysFloatValsMatrixWithContext() throws Exception {
        double[] keys = doubleArray(ROWS * COLS);
        float[] vals = floatArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.FLOAT, ROWS, COLS);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        float[] sortedVals = new float[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        float[] hostSortedVals = floatArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

    @Test
    public void testSortDoubleKeysDoubleValsMatrixWithContext() throws Exception {
        double[] keys = doubleArray(ROWS * COLS);
        double[] vals = doubleArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.DOUBLE, ROWS, COLS);

        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        double[] sortedVals = new double[vals.length];
        copy2host(sortedVals, valsPtr);

        double[] hostSortedKeys = doubleArray(keys);
        for (int i = 0; i < ROWS; i++) {
            Arrays.sort(hostSortedKeys, i * COLS, (i + 1) * COLS);
        }        double[] hostSortedVals = doubleArray(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
        assertArrayEquals(sortedVals, hostSortedVals, 0);

        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

}
