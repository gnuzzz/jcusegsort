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
public class TestSortKeyValuePairsMatrixWithContext {

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
                            "        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype." + keyType.toUpperCase() + ", Datatype." + valueType.toUpperCase() + ", ROWS * COLS, COLS);\n" +
                            "        Sorting.sort(keysPtr, Datatype." + keyType.toUpperCase() + ", valsPtr, Datatype." + valueType.toUpperCase() + ", ROWS, COLS, context);\n" +
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
    public void testSortBooleanKeysBooleanValsMatrixWithContext() throws Exception {
        boolean[] keys = booleanArray(ROWS * COLS);
        boolean[] vals = booleanArray(keys);

        long keysSize = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(keysSize);
        copy2device(keysPtr, keys);
        long valsSize = vals.length * sizeOfItem(vals);
        CUdeviceptr valsPtr = devicePtr(valsSize);
        copy2device(valsPtr, vals);

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.BOOLEAN, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.BOOLEAN, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.BYTE, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.BYTE, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.CHAR, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.CHAR, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.SHORT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.SHORT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.INT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.INT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.LONG, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.LONG, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.FLOAT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.FLOAT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BOOLEAN, Datatype.DOUBLE, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.BOOLEAN, valsPtr, Datatype.DOUBLE, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.BOOLEAN, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.BOOLEAN, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.BYTE, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.BYTE, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.CHAR, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.CHAR, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.SHORT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.SHORT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.INT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.INT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.LONG, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.LONG, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.FLOAT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.FLOAT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.BYTE, Datatype.DOUBLE, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.BYTE, valsPtr, Datatype.DOUBLE, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.BOOLEAN, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.BOOLEAN, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.BYTE, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.BYTE, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.CHAR, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.CHAR, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.SHORT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.SHORT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.INT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.INT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.LONG, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.LONG, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.FLOAT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.FLOAT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.CHAR, Datatype.DOUBLE, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.CHAR, valsPtr, Datatype.DOUBLE, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.BOOLEAN, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.BOOLEAN, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.BYTE, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.BYTE, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.CHAR, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.CHAR, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.SHORT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.SHORT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.INT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.INT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.LONG, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.LONG, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.FLOAT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.FLOAT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.SHORT, Datatype.DOUBLE, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.SHORT, valsPtr, Datatype.DOUBLE, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.BOOLEAN, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.BOOLEAN, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.BYTE, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.BYTE, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.CHAR, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.CHAR, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.SHORT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.SHORT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.INT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.INT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.LONG, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.LONG, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.FLOAT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.FLOAT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.INT, Datatype.DOUBLE, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.INT, valsPtr, Datatype.DOUBLE, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.BOOLEAN, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.BOOLEAN, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.BYTE, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.BYTE, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.CHAR, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.CHAR, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.SHORT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.SHORT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.INT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.INT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.LONG, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.LONG, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.FLOAT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.FLOAT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.LONG, Datatype.DOUBLE, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.LONG, valsPtr, Datatype.DOUBLE, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.BOOLEAN, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.BOOLEAN, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.BYTE, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.BYTE, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.CHAR, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.CHAR, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.SHORT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.SHORT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.INT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.INT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.LONG, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.LONG, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.FLOAT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.FLOAT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.FLOAT, Datatype.DOUBLE, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.FLOAT, valsPtr, Datatype.DOUBLE, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.BOOLEAN, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.BOOLEAN, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.BYTE, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.BYTE, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.CHAR, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.CHAR, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.SHORT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.SHORT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.INT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.INT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.LONG, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.LONG, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.FLOAT, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.FLOAT, ROWS, COLS, context);

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

        context.free();
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

        KeyValueSortContext context = Sorting.keyValueSortContext(Datatype.DOUBLE, Datatype.DOUBLE, ROWS * COLS, COLS);
        Sorting.sort(keysPtr, Datatype.DOUBLE, valsPtr, Datatype.DOUBLE, ROWS, COLS, context);

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

        context.free();
        deletePtr(keysPtr);
        deletePtr(valsPtr);
    }

}
