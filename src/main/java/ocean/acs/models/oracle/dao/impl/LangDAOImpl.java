package ocean.acs.models.oracle.dao.impl;

import lombok.AllArgsConstructor;
import ocean.acs.models.dao.LangDAO;
import ocean.acs.models.data_object.entity.LangDO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
@AllArgsConstructor
public class LangDAOImpl implements LangDAO {

    private final ocean.acs.models.oracle.repository.LangRepository repo;

    @Override
    public List<LangDO> findAll() {
        return StreamSupport.stream(repo.findAll().spliterator(), false)
                .map(LangDO::valueOf)
                .collect(Collectors.toList());
    }
}
