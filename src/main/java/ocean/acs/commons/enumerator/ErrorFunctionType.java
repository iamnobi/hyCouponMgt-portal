package ocean.acs.commons.enumerator;

public enum ErrorFunctionType {

    CHALLENGE_REASON(1L), FAILURE_REASON(2L);

    private Long id;

    ErrorFunctionType(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    // public static List<ErrorIssueGroupDTO> getAllType() { // TODO
    // List<ErrorIssueGroupDTO> errorIssueGroupDTOList = new ArrayList<>();
    //
    // for(ErrorFunctionType type : ErrorFunctionType.values()) {
    // ErrorIssueGroupDTO errorIssueGroupDTO = new ErrorIssueGroupDTO();
    // errorIssueGroupDTO.setGroupName(type.toString());
    // errorIssueGroupDTO.setId(type.getId());
    // errorIssueGroupDTOList.add(errorIssueGroupDTO);
    // }
    //
    // return errorIssueGroupDTOList;
    // }
}
