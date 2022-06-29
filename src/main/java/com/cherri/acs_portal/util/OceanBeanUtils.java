package com.cherri.acs_portal.util;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class OceanBeanUtils {

    public static void copyProperties(Object source, Object target) {
        String[] ignorePropName = getNotNullPropertyNames(target);
        BeanUtils.copyProperties(source, target, ignorePropName);
    }

    private static String[] getNotNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
          .map(FeatureDescriptor::getName)
          .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) != null)
          .toArray(String[]::new);
    }
}
