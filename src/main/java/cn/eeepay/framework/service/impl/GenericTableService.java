package cn.eeepay.framework.service.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("genericTableService")
@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
public class GenericTableService {
	private static final Logger log = LoggerFactory.getLogger(GenericTableService.class);
	@Resource
	private DataSource dataSource;

	public String noErrorKey(String table) {
		Connection conn=null;
		String num = "";
		try {
			conn=dataSource.getConnection();
			conn.setAutoCommit(true);
			PreparedStatement pst = conn.prepareStatement("select primary_key from generic_table where table_name=?");
			pst.setString(1, table);
			ResultSet rs= pst.executeQuery();
			if(rs.next())
				num=rs.getString(1);
			rs.close();
			pst.close();
			BigInteger big = new BigInteger(num).add(new BigInteger("1"));
			num = big.toString();
			pst = conn.prepareStatement("update generic_table set primary_key = ? where table_name=? ");
			pst.setString(1, num);
			pst.setString(2, table);
			pst.executeUpdate();
			pst.close();
		} catch (Exception e) {
			log.error("产生序列失败！",e);
		}finally {
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return num;
	}
	public String createExtKey(String tableName,String format) {
		String num = noErrorKey(tableName);
		return String.format(format, new BigInteger(num));
	}
}
