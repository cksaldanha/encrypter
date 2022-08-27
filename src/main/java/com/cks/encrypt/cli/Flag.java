/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author colin.saldanha
 */
public class Flag {

    private final String key;
    private final Optional<Object> value;

    public Flag(String key, Object value) {
        if (key == null || key.equals("")) {
            throw new IllegalArgumentException("Cannot have a null or empty flag key.");
        }
        this.key = key;
        this.value = Optional.ofNullable(value);
    }

    public String getKey() {
        return key;
    }

    public Optional<Object> getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        Flag other = (Flag) obj;
        return Objects.equals(key, other.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "Flag[key=" + key + ", value=" + value.orElse("") + "]";
    }
}
