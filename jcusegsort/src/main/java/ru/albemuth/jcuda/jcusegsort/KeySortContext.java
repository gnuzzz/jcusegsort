package ru.albemuth.jcuda.jcusegsort;

import jcuda.NativePointerObject;

/**
 * @author Vladimir Kornyshev { @literal <gnuzzz@mail.ru>}
 */
public class KeySortContext extends NativePointerObject {

    private Datatype keyType;
    private int keysLength;
    private int segmentsLength;

    public KeySortContext(Datatype keyType, int keysLength, int segmentsLength) {
        this.keyType = keyType;
        this.keysLength = keysLength;
        this.segmentsLength = segmentsLength;
    }

    public Datatype getKeyType() {
        return keyType;
    }

    public int getKeysLength() {
        return keysLength;
    }

    public int getSegmentsLength() {
        return segmentsLength;
    }

    protected native int init(int keyType, int keysLength, int segmentsLength);

    protected native int free();

    @Override
    protected void finalize() {
        free();
    }

}
