package ru.albemuth.jcuda.jcusegsort;

import jcuda.NativePointerObject;

/**
 * @author Vladimir Kornyshev { @literal <gnuzzz@mail.ru>}
 */
public class KeyValueSortContext extends NativePointerObject {

    private Datatype keyType;
    private Datatype valueType;
    private int keysLength;
    private int segmentsLength;

    public KeyValueSortContext(Datatype keyType, Datatype valueType, int keysLength, int segmentsLength) {
        this.keyType = keyType;
        this.valueType = valueType;
        this.keysLength = keysLength;
        this.segmentsLength = segmentsLength;
    }

    public Datatype getKeyType() {
        return keyType;
    }

    public Datatype getValueType() {
        return valueType;
    }

    public int getKeysLength() {
        return keysLength;
    }

    public int getSegmentsLength() {
        return segmentsLength;
    }

    protected native int init(int keyType, int valueType, int keysLength, int segmentsLength);

    public native int free();

    @Override
    protected void finalize() {
        free();
    }

}
