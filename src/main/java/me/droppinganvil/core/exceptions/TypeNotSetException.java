/*
 * Copyright (c) DroppingAnvil 2020.
 * All Rights Reserved.
 */

package me.droppinganvil.core.exceptions;

public class TypeNotSetException extends Exception {
    public TypeNotSetException() {
        super("The Type of this MySQL object has not been set, you cannot call generics methods without type arguments on MySQL such as getObject(String key, String value)");
    }

}
