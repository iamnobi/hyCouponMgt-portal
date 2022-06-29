package ocean.acs.commons.exception;

import ocean.acs.commons.enumerator.ResultStatus;

public class NoSuchDataException extends OceanExceptionForPortal {

    public NoSuchDataException() {
        super(ResultStatus.NO_SUCH_DATA);
    }

    public NoSuchDataException(String message) {
        super(ResultStatus.NO_SUCH_DATA, message);
    }

    @Override
    public ResultStatus getResultStatus() {
        return ResultStatus.NO_SUCH_DATA;
    }

}
