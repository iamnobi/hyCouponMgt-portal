package com.cherri.acs_kernel.plugin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
public class IssuerPropertyDefinition {

    /**
     * The name of this Issuer Property
     */
    @Getter
    @Setter
    private String name;

    /**
     * The value of this Issuer Property stored in database should be encrypted or not.
     */
    @Getter
    @Setter
    private boolean isEncrypt;

    /**
     * The description of this Issuer Property.
     */
    @Getter
    @Setter
    private String description;

    /**
     * The propertyId of this Issuer Property. Used for retrieving data from issuerPropertiesMap.
     */
    public int getPropertyId() {
        return name.hashCode();
    }

    protected IssuerPropertyDefinition(String name, boolean isEncrypt, String description) {
        this.name = name;
        this.isEncrypt = isEncrypt;
        this.description = description;
    }

    public static IssuerPropertyDefinition newInstance(
            String name, boolean isEncrypt, String description) {
        return new IssuerPropertyDefinition(name, isEncrypt, description);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        IssuerPropertyDefinition target = (IssuerPropertyDefinition) obj;

        return target.getPropertyId() == getPropertyId();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
