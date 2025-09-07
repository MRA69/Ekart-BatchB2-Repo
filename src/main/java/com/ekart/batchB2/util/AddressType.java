package com.ekart.batchB2.util;

public enum AddressType {
    HOME, WORK, CUSTOM;

    @Override
    public String toString() {
        return this.name();
    }
}
