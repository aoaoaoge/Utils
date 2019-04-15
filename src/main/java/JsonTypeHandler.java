import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes({Object.class})
public class JsonTypeHandler extends BaseTypeHandler<Object> {

    private static final PGobject JSON_OBJECT = new PGobject();

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws
            SQLException {
        JSON_OBJECT.setType("json");
        if (o instanceof String) {
            if ("".equals(o)) {
                JSON_OBJECT.setValue(null);
            } else {
                JSON_OBJECT.setValue(o+"");
            }
        } else {
            String s = JsonUtil.toJson(o);
            JSON_OBJECT.setValue(s);
        }
        preparedStatement.setObject(i, JSON_OBJECT);
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return JsonUtil.parse(resultSet.getString(s), Object.class);
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return JsonUtil.parse(resultSet.getString(i), Object.class);
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return JsonUtil.parse(callableStatement.getString(i), Object.class);
    }
}
