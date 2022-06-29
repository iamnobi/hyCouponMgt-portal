package ocean.acs.commons.utils;

import org.apache.commons.lang3.builder.ToStringStyle;

public class AuditLogToStringStyle extends ToStringStyle {

    private static final long serialVersionUID = 1L;

    public AuditLogToStringStyle() {
        super();

        this.setUseClassName(false);
        this.setUseIdentityHashCode(false);

        this.setContentStart("");
        this.setContentEnd("");

        this.setFieldSeparatorAtStart(false);
        this.setFieldSeparatorAtEnd(false);

        this.setArrayStart("");
        this.setArrayEnd("");

        this.setFieldSeparator("|");
        this.setFieldNameValueSeparator("=");

        this.setNullText("");
    }

    @Override
    public void append(
      final StringBuffer buffer,
      final String fieldName,
      final Object[] array,
      final Boolean fullDetail) {

        if (fieldName == null) {
            throw new UnsupportedOperationException(
              "Field names are mandatory when using AuditLog_Style");
        }
        if (!isFullDetail(fullDetail)) {
            throw new UnsupportedOperationException(
              "FullDetail must be true when using AuditLog_Style");
        }

        super.append(buffer, fieldName, array, fullDetail);
    }

    @Override
    public void append(
      final StringBuffer buffer,
      final String fieldName,
      final long[] array,
      final Boolean fullDetail) {

        if (fieldName == null) {
            throw new UnsupportedOperationException(
              "Field names are mandatory when using AuditLog_Style");
        }
        if (!isFullDetail(fullDetail)) {
            throw new UnsupportedOperationException(
              "FullDetail must be true when using AuditLog_Style");
        }

        super.append(buffer, fieldName, array, fullDetail);
    }

    @Override
    public void append(
      final StringBuffer buffer,
      final String fieldName,
      final int[] array,
      final Boolean fullDetail) {

        if (fieldName == null) {
            throw new UnsupportedOperationException(
              "Field names are mandatory when using AuditLog_Style");
        }
        if (!isFullDetail(fullDetail)) {
            throw new UnsupportedOperationException(
              "FullDetail must be true when using AuditLog_Style");
        }

        super.append(buffer, fieldName, array, fullDetail);
    }

    @Override
    public void append(
      final StringBuffer buffer,
      final String fieldName,
      final short[] array,
      final Boolean fullDetail) {

        if (fieldName == null) {
            throw new UnsupportedOperationException(
              "Field names are mandatory when using AuditLog_Style");
        }
        if (!isFullDetail(fullDetail)) {
            throw new UnsupportedOperationException(
              "FullDetail must be true when using AuditLog_Style");
        }

        super.append(buffer, fieldName, array, fullDetail);
    }

    @Override
    public void append(
      final StringBuffer buffer,
      final String fieldName,
      final byte[] array,
      final Boolean fullDetail) {

        if (fieldName == null) {
            throw new UnsupportedOperationException(
              "Field names are mandatory when using AuditLog_Style");
        }
        if (!isFullDetail(fullDetail)) {
            throw new UnsupportedOperationException(
              "FullDetail must be true when using AuditLog_Style");
        }

        super.append(buffer, fieldName, array, fullDetail);
    }

    @Override
    public void append(
      final StringBuffer buffer,
      final String fieldName,
      final char[] array,
      final Boolean fullDetail) {

        if (fieldName == null) {
            throw new UnsupportedOperationException(
              "Field names are mandatory when using AuditLog_Style");
        }
        if (!isFullDetail(fullDetail)) {
            throw new UnsupportedOperationException(
              "FullDetail must be true when using AuditLog_Style");
        }

        super.append(buffer, fieldName, array, fullDetail);
    }

    @Override
    public void append(
      final StringBuffer buffer,
      final String fieldName,
      final double[] array,
      final Boolean fullDetail) {

        if (fieldName == null) {
            throw new UnsupportedOperationException(
              "Field names are mandatory when using AuditLog_Style");
        }
        if (!isFullDetail(fullDetail)) {
            throw new UnsupportedOperationException(
              "FullDetail must be true when using AuditLog_Style");
        }

        super.append(buffer, fieldName, array, fullDetail);
    }

    @Override
    public void append(
      final StringBuffer buffer,
      final String fieldName,
      final float[] array,
      final Boolean fullDetail) {

        if (fieldName == null) {
            throw new UnsupportedOperationException(
              "Field names are mandatory when using AuditLog_Style");
        }
        if (!isFullDetail(fullDetail)) {
            throw new UnsupportedOperationException(
              "FullDetail must be true when using AuditLog_Style");
        }

        super.append(buffer, fieldName, array, fullDetail);
    }

    @Override
    public void append(
      final StringBuffer buffer,
      final String fieldName,
      final boolean[] array,
      final Boolean fullDetail) {

        if (fieldName == null) {
            throw new UnsupportedOperationException(
              "Field names are mandatory when using AuditLog_Style");
        }
        if (!isFullDetail(fullDetail)) {
            throw new UnsupportedOperationException(
              "FullDetail must be true when using AuditLog_Style");
        }

        super.append(buffer, fieldName, array, fullDetail);
    }

    @Override
    public void append(
      final StringBuffer buffer,
      final String fieldName,
      final Object value,
      final Boolean fullDetail) {

        if (fieldName == null) {
            throw new UnsupportedOperationException(
              "Field names are mandatory when using AuditLog_Style");
        }
        if (!isFullDetail(fullDetail)) {
            throw new UnsupportedOperationException(
              "FullDetail must be true when using AuditLog_Style");
        }

        super.append(buffer, fieldName, value, fullDetail);
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName,
      final char value) {
        appendValueAsString(buffer, String.valueOf(value));
    }

    @Override
    protected void appendDetail(
      final StringBuffer buffer, final String fieldName, final Object value) {

        if (value == null) {
            appendNullText(buffer, fieldName);
            return;
        }

        if (value instanceof String || value instanceof Character) {
            final String fn = fieldName.toLowerCase();
            if ("pannumber".equals(fn)
              || "pan".equals(fn)
              || "realcardnumber".equals(fn)
              || "cardnumber".equals(fn)) {
                appendValueAsString(buffer, MaskUtils.acctNumberMask(value.toString()));
                return;
            } else if ("identitynumber".equals(fn)) {
                appendValueAsString(buffer, MaskUtils.identityNumberMask(value.toString()));
            }
            appendValueAsString(buffer, value.toString());

            return;
        }

        if (value instanceof Number || value instanceof Boolean) {
            buffer.append(value);
            return;
        }

        final String valueAsString = value.toString();
        appendDetail(buffer, fieldName, valueAsString);
    }

    /**
     * Appends the given String enclosed to the given StringBuffer.
     *
     * @param buffer the StringBuffer to append the value to.
     * @param value  the value to append.
     */
    private void appendValueAsString(final StringBuffer buffer, final String value) {
        buffer.append(value);
    }

    @Override
    protected void appendFieldStart(final StringBuffer buffer, final String fieldName) {

        if (fieldName == null) {
            throw new UnsupportedOperationException(
              "Field names are mandatory when using AuditLog_Style");
        }

        super.appendFieldStart(buffer, fieldName);
    }
}
