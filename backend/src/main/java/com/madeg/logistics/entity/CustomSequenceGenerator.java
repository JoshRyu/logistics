package com.madeg.logistics.entity;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.type.Type;

@Slf4j
public class CustomSequenceGenerator extends SequenceStyleGenerator {

  private String prefix = "_";

  @Override
  public Serializable generate(
    SharedSessionContractImplementor session,
    Object object
  ) throws HibernateException {
    String sequenceName = prefix + "seq";

    Connection connection = null;
    try {
      ConnectionProvider connectionProvider = session
        .getFactory()
        .getServiceRegistry()
        .getService(ConnectionProvider.class);
      connection = connectionProvider.getConnection();

      PreparedStatement preparedStatement = connection.prepareStatement(
        "SELECT nextval('" + sequenceName + "')"
      );
      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        int nextValue = resultSet.getInt(1);
        String generatedId =
          (prefix.contains("_") ? prefix.split("_")[0] : "") + "_" + nextValue;
        return generatedId;
      }
    } catch (SQLException e) {
      throw new HibernateException("Unable to generate sequence", e);
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          log.debug("Unexpected error occurs while closing the connection");
        }
      }
    }
    return null;
  }

  @Override
  public void configure(
    Type type,
    Properties params,
    org.hibernate.service.ServiceRegistry serviceRegistry
  ) {
    super.configure(type, params, serviceRegistry);
    String parameterPrefix = params.getProperty("prefix");
    if (parameterPrefix != null) {
      prefix = parameterPrefix;
    }
  }
}
