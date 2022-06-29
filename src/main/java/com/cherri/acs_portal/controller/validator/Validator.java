package com.cherri.acs_portal.controller.validator;

/**
 * Validator Interface
 *
 * @param <T> Class name
 * @author Alan Chen
 */
public interface Validator<T> {

    /**
     * Valid value
     *
     * @param data will check data
     * @return Valid Is pass
     */
    boolean isValid(T data);
}
