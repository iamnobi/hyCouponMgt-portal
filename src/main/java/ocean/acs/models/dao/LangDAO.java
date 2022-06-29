package ocean.acs.models.dao;

import ocean.acs.models.data_object.entity.LangDO;

import java.util.List;

public interface LangDAO {
    List<LangDO> findAll();
}
