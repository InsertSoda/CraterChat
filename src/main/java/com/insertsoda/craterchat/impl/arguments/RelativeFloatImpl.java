package com.insertsoda.craterchat.impl.arguments;

import com.insertsoda.craterchat.api.v1.arguments.RelativeFloat;

public class RelativeFloatImpl implements RelativeFloat {

    private float value;

    private boolean isRelative;

    public RelativeFloatImpl(float value, boolean isRelative){
        this.value = value;
        this.isRelative = isRelative;
    }

    @Override
    public float getValue() {
        return this.value;
    }

    @Override
    public boolean isRelative() {
        return this.isRelative;
    }
}
