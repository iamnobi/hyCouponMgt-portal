package com.cherri.acs_kernel.plugin.hsm.impl.model.dao;

import com.cherri.acs_kernel.plugin.hsm.impl.model.domain.KeyType;
import com.cherri.acs_kernel.plugin.hsm.impl.model.domain.MockHsmKey;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
public class MockHsmKeyDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;
    private Map<String, MockHsmKey> mockHsmKeyMap = new HashMap<>();

    public MockHsmKeyDAO(NamedParameterJdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    public MockHsmKey findKeyById(String id) {
        if (mockHsmKeyMap.containsKey(id)) {
            return mockHsmKeyMap.get(id);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return jdbcTemplate.query(
                "select * from MOCK_HSM_KEY where ID = :id",
                params,
                rs -> {
                    if (rs.next()) {
                        MockHsmKey mockHsmKey = toMockHsmKey(rs);
                        mockHsmKeyMap.put(id, mockHsmKey);
                        return mockHsmKey;
                    } else {
                        return null;
                    }
                });
    }

    @Transactional
    public void saveKey(MockHsmKey mockHsmKey) {
        String sql =
                "insert into MOCK_HSM_KEY (ID, KEY_LABEL, KEY_TYPE, KEY_BODY) "
                        + "values (:id, :keyLabel, :keyType, :key)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", mockHsmKey.getId());
        params.put("keyLabel", mockHsmKey.getKeyLabel());
        params.put("keyType", mockHsmKey.getKeyType().toString());
        params.put("key", mockHsmKey.getKey());
        jdbcTemplate.update(sql, params);
    }

    private MockHsmKey toMockHsmKey(ResultSet rs) throws SQLException {
        MockHsmKey mockHsmKey = new MockHsmKey();
        mockHsmKey.setId(rs.getString("ID"));
        mockHsmKey.setKey(rs.getBytes("KEY_BODY"));
        mockHsmKey.setKeyLabel(rs.getString("KEY_LABEL"));
        mockHsmKey.setKeyType(KeyType.valueOf(rs.getString("KEY_TYPE")));
        return mockHsmKey;
    }
}
