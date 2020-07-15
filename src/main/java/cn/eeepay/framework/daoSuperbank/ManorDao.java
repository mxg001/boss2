package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ManorDao {

	class SqlProvider{
		public String selectManorMoneyPage(Map<String, Object> param){
			ManorMoney baseInfo = (ManorMoney)param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("rtlp.*,ui.user_name,ui.nick_name,ui.phone,ui.user_id,oi.org_name,rt.province_name,rt.city_name,rt.region_name");
			whereManorMoneySql(sql,baseInfo);
			return sql.toString() + " order by rtlp.create_time desc";
		}

		public String selectManorMoneySum(Map<String, Object> param){
			ManorMoney baseInfo = (ManorMoney)param.get("baseInfo");
			SQL sql = new SQL(){{
					SELECT("sum(rtlp.total_profit) as totalAccumulatedIncome");
					SELECT("sum(rtlp.red_account_profit) as totalRedIncome");
			}};
			whereManorMoneySql(sql,baseInfo);
			return sql.toString() + " order by rtlp.create_time desc";
		}
		public String selectManorManager(){
			SQL sql = new SQL();
			sql.SELECT("rtc.*");
			sql.FROM("red_territory_conf rtc");
			return sql.toString();
		}

		public String selectDailyBusinessPage(Map<String, Object> param){
			List<Long> list = (List<Long>) param.get("list");
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT rtbd.*   from  red_territory_bonus_detail rtbd where everyday_id in (");
			MessageFormat mf = new MessageFormat("#'{'list[{0}]}");
			for (int i = 0; i < list.size(); i++) {
				sb.append(mf.format(new Object[]{i}));
				if (i < list.size() - 1) {
					sb.append(",");
				}
			}
			sb.append(")");
			return sb.toString();
		}

		public String selectDailyDividendPage(Map<String, Object> param){
			RedTerritoryBonusEveryday baseInfo = (RedTerritoryBonusEveryday)param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("rtbe.*,ui.user_name,ui.phone,ui.user_code,oi.org_name");
			whereSql(sql,baseInfo);
			return sql.toString() + " order by rtbe.id desc";
		}

		public String selectManorMoneyDetlPage(Map<String, Object> param){
			ManorMoney baseInfo = (ManorMoney)param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("rtlp.*,ui.user_name,ui.nick_name,ui.phone,ui.user_id,oi.org_name,rad.create_date,rad.trans_amount,rt.province_name,rt.city_name,rt.region_name");
			sql.FROM("red_territory_lords_profit rtlp");
			sql.LEFT_OUTER_JOIN("user_info ui on ui.user_code=rtlp.lords_user_code");
			sql.LEFT_OUTER_JOIN("org_info oi on oi.org_id=rtlp.lords_org_id");
			sql.LEFT_OUTER_JOIN("red_account_detail rad on rad.territory_profit_id=rtlp.id");
			sql.LEFT_OUTER_JOIN("red_territory rt on rt.id=rtlp.territory_id");
			if(StringUtils.isNotBlank(baseInfo.getUserName())){
				sql.WHERE("ui.user_name = #{baseInfo.userName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getPhone())){
				sql.WHERE("ui.phone = #{baseInfo.phone}");
			}
			if(baseInfo.getLordsOrgId()!=null && baseInfo.getLordsOrgId()!=-1){
				sql.WHERE("oi.org_id = #{baseInfo.lordsOrgId}");
			}
          /*  if(baseInfo.getUserId()!=null ){
            	sql.WHERE("ui.user_id = #{baseInfo.userId}");
            }*/

			if(StringUtils.isNotBlank(baseInfo.getLordsUserCode())){
				sql.WHERE("rtlp.lords_user_code = #{baseInfo.lordsUserCode}");
			}

			//省市区
			if(StringUtils.isNotBlank(baseInfo.getProvinceName())){
				sql.WHERE("rt.province_name = #{baseInfo.provinceName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getCityName())){
				sql.WHERE("rt.city_name = #{baseInfo.cityName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getRegionName())){
				sql.WHERE("rt.region_name = #{baseInfo.regionName}");
			}

			if(StringUtils.isNotBlank(baseInfo.getCreateDateStart())){
				sql.WHERE("rad.create_date >= #{baseInfo.createDateStart}");
			}
			if(StringUtils.isNotBlank(baseInfo.getCreateDateEnd())){
				sql.WHERE("rad.create_date <= #{baseInfo.createDateEnd}");
			}
			return sql.toString() + " order by rad.create_date desc";
		}
		public String selectManorTransactionRecorePage(Map<String, Object> param){
			ManorTransactionRecore baseInfo = (ManorTransactionRecore)param.get("baseInfo");
			SQL sql = new SQL();
			if (baseInfo.getType()!=null && baseInfo.getType()==1) {
				sql.SELECT("sum(om.total_bonus) as sumMoney,sum(om.plate_profit) as sumPlatMoney,sum(rto.premium_price) as sumPremiumMoney ,sum(om.org_profit) as sumDividedMoney , sum(rto.trade_fee) as sumTradeFee "
				);
			} else{
				sql.SELECT("rto.*,om.*,oi1.org_name as oldLordsOrgName,oi2.org_name as newLordsOrgName,"
						+ "uio.user_id oid,uio.user_name userName,uio.nick_name nickName,uio.phone,"
						+ "uin.user_id nid,uin.user_name newUserName,uin.nick_name newNickName,uin.phone newPhone,"
						+ "ui1.user_name oneUserName,ui2.user_name twoUserName,ui3.user_name thrUserName,ui4.user_name fouUserName,"
						+ "rt.province_name,rt.city_name,rt.region_name,rto.create_time as cDate"
				);
			}
			sql.FROM("red_territory_order rto");
			sql.JOIN("order_main om on om.id=rto.order_id");
			sql.LEFT_OUTER_JOIN("org_info oi1 on oi1.org_id=rto.old_lords_org_id");
			sql.LEFT_OUTER_JOIN("org_info oi2 on oi2.org_id=rto.new_lords_org_id");
			sql.LEFT_OUTER_JOIN("user_info uio ON uio.user_code = rto.old_lords_user_code");
			sql.LEFT_OUTER_JOIN("user_info uin ON uin.user_code = rto.new_lords_user_code");
			sql.LEFT_OUTER_JOIN("user_info ui1 ON ui1.user_code = om.one_user_code");
			sql.LEFT_OUTER_JOIN("user_info ui2 ON ui2.user_code = om.two_user_code");
			sql.LEFT_OUTER_JOIN("user_info ui3 ON ui3.user_code = om.thr_user_code");
			sql.LEFT_OUTER_JOIN("user_info ui4 ON ui4.user_code = om.fou_user_code");
			sql.LEFT_OUTER_JOIN("red_territory rt ON rt.id = rto.territory_id");

			if(StringUtils.isNotBlank(baseInfo.getOrderNo())){
				sql.WHERE("om.order_no = #{baseInfo.orderNo}");
			}
			if(StringUtils.isNotBlank(baseInfo.getStatus())){
				sql.WHERE("om.status = #{baseInfo.status}");
			}
			if(StringUtils.isNotBlank(baseInfo.getAccountStatus())){
				sql.WHERE("om.account_status = #{baseInfo.accountStatus}");
			}

			//地区
			if(StringUtils.isNotBlank(baseInfo.getProvinceName())){
				sql.WHERE("rt.province_name = #{baseInfo.provinceName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getCityName())){
				sql.WHERE("rt.city_name = #{baseInfo.cityName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getRegionName())){
				sql.WHERE("rt.region_name = #{baseInfo.regionName}");
			}

			//交易时间
			if(StringUtils.isNotBlank(baseInfo.getPayDateStart())){
				sql.WHERE("om.pay_date >= #{baseInfo.payDateStart}");
			}
			if(StringUtils.isNotBlank(baseInfo.getPayDateEnd())){
				sql.WHERE("om.pay_date <= #{baseInfo.payDateEnd}");
			}

			//创建时间
			if(StringUtils.isNotBlank(baseInfo.getcDateStart())){
				sql.WHERE("rto.create_time >= #{baseInfo.cDateStart}");
			}
			if(StringUtils.isNotBlank(baseInfo.getcDateEnd())){
				sql.WHERE("rto.create_time <= #{baseInfo.cDateEnd}");
			}

			//原领主
			if(baseInfo.getOldLordsOrgId()!=null){
				sql.WHERE("oi1.org_id = #{baseInfo.oldLordsOrgId}");
			}
        	/*if(baseInfo.getOid()!=null){
        		sql.WHERE("uio.user_id = #{baseInfo.oid}");
        	}*/

			if(StringUtils.isNotBlank(baseInfo.getOldLordsUserCode())){
				sql.WHERE("rto.old_lords_user_code = #{baseInfo.oldLordsUserCode}");
			}

			if(StringUtils.isNotBlank(baseInfo.getUserName())){
				sql.WHERE("uio.user_name = #{baseInfo.userName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getPhone())){
				sql.WHERE("uio.phone = #{baseInfo.phone}");
			}

			//新领主
			if(baseInfo.getNewLordsOrgId()!=null){
				sql.WHERE("uin.org_id = #{baseInfo.newLordsOrgId}");
			}
        	/*if(baseInfo.getNid()!=null){
        		sql.WHERE("uin.user_id = #{baseInfo.nid}");
        	}*/

			if(StringUtils.isNotBlank(baseInfo.getNewLordsUserCode())){
				sql.WHERE("rto.new_lords_user_code = #{baseInfo.newLordsUserCode}");
			}


			if(StringUtils.isNotBlank(baseInfo.getNewUserName())){
				sql.WHERE("uin.user_name = #{baseInfo.newUserName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getNewPhone())){
				sql.WHERE("uin.phone = #{baseInfo.newPhone}");
			}
			//支付方式
			if(StringUtils.isNotBlank(baseInfo.getPayMethod())){
				sql.WHERE("om.pay_method = #{baseInfo.payMethod}");
			}
			if(StringUtils.isNotBlank(baseInfo.getPayChannel())){
				sql.WHERE("om.pay_channel = #{baseInfo.payChannel}");
			}
			if(StringUtils.isNotBlank(baseInfo.getPayChannelNo())){
				sql.WHERE("om.pay_channel_no = #{baseInfo.payChannelNo}");
			}
			if(StringUtils.isNotBlank(baseInfo.getPayOrderNo())){
				sql.WHERE("om.pay_order_no = #{baseInfo.payOrderNo}");
			}

			//一级
			if(StringUtils.isNotBlank(baseInfo.getOneUserCode())){
				sql.WHERE("om.one_user_code = #{baseInfo.oneUserCode}");
			}
			if(StringUtils.isNotBlank(baseInfo.getOneUserName())){
				sql.WHERE("ui1.user_name = #{baseInfo.oneUserName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getOneUserType())){
				sql.WHERE("om.one_user_type = #{baseInfo.oneUserType}");
			}
			//一级
			if(StringUtils.isNotBlank(baseInfo.getOneUserCode())){
				sql.WHERE("om.one_user_code = #{baseInfo.oneUserCode}");
			}
			if(StringUtils.isNotBlank(baseInfo.getOneUserName())){
				sql.WHERE("ui1.user_name = #{baseInfo.oneUserName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getOneUserType())){
				sql.WHERE("om.one_user_type = #{baseInfo.oneUserType}");
			}
			//2级
			if(StringUtils.isNotBlank(baseInfo.getTwoUserCode())){
				sql.WHERE("om.two_user_code = #{baseInfo.twoUserCode}");
			}
			if(StringUtils.isNotBlank(baseInfo.getTwoUserName())){
				sql.WHERE("ui2.user_name = #{baseInfo.twoUserName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getTwoUserType())){
				sql.WHERE("om.two_user_type = #{baseInfo.twoUserType}");
			}
			//3级
			if(StringUtils.isNotBlank(baseInfo.getThrUserCode())){
				sql.WHERE("om.thr_user_code = #{baseInfo.thrUserCode}");
			}
			if(StringUtils.isNotBlank(baseInfo.getThrUserName())){
				sql.WHERE("ui3.user_name = #{baseInfo.oneUserName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getThrUserType())){
				sql.WHERE("om.thr_user_type = #{baseInfo.thrUserType}");
			}
			//4级
			if(StringUtils.isNotBlank(baseInfo.getFouUserCode())){
				sql.WHERE("om.fou_user_code = #{baseInfo.fouUserCode}");
			}
			if(StringUtils.isNotBlank(baseInfo.getFouUserName())){
				sql.WHERE("ui4.user_name = #{baseInfo.fouUserName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getFouUserType())){
				sql.WHERE("om.fou_user_type = #{baseInfo.fouUserType}");
			}

			if(StringUtils.isBlank(baseInfo.getPayDateStart()) && StringUtils.isBlank(baseInfo.getPayDateEnd())){
				return sql.toString() + " ORDER BY rto.create_time desc";
			}

			return sql.toString() + " ORDER BY om.pay_date desc";
		}

		public String selectManorTransactionRecorePageById(){
			SQL sql = new SQL();
			sql.SELECT("rto.*,om.*,oi1.org_name as oldLordsOrgName,oi2.org_name as newLordsOrgName,"
					+ "uio.user_id oid,uio.user_name userName,uio.nick_name nickName,uio.phone,"
					+ "uin.user_id nid,uin.user_name newUserName,uin.nick_name newNickName,uin.phone newPhone,"
					+ "ui1.user_name oneUserName,ui2.user_name twoUserName,ui3.user_name thrUserName,ui4.user_name fouUserName,"
					+ "rt.province_name,rt.city_name,rt.region_name,om.company_bonus_conf "
			);
			sql.FROM("red_territory_order rto");
			sql.JOIN("order_main om on om.id=rto.order_id AND rto.order_id=#{id}");
			sql.LEFT_OUTER_JOIN("org_info oi1 on oi1.org_id=rto.old_lords_org_id");
			sql.LEFT_OUTER_JOIN("org_info oi2 on oi2.org_id=rto.new_lords_org_id");
			sql.LEFT_OUTER_JOIN("user_info uio ON uio.user_code = rto.old_lords_user_code");
			sql.LEFT_OUTER_JOIN("user_info uin ON uin.user_code = rto.new_lords_user_code");
			sql.LEFT_OUTER_JOIN("user_info ui1 ON ui1.user_code = om.one_user_code");
			sql.LEFT_OUTER_JOIN("user_info ui2 ON ui2.user_code = om.two_user_code");
			sql.LEFT_OUTER_JOIN("user_info ui3 ON ui3.user_code = om.thr_user_code");
			sql.LEFT_OUTER_JOIN("user_info ui4 ON ui4.user_code = om.fou_user_code");
			sql.LEFT_OUTER_JOIN("red_territory rt ON rt.id = rto.territory_id");

			return sql.toString();
		}


		public String selectManorAdjustRecorePage(Map<String, Object> param){
			ManorAdjustRecore baseInfo = (ManorAdjustRecore)param.get("baseInfo");

			StringBuffer sb = new StringBuffer();

			sb.append("select rtp.*,rt.province_name,rt.city_name,rt.region_name,ui.user_id,ui.user_name,ui.nick_name,ui.phone,oi.org_name,rt.is_trade "
					+ " from red_territory_price rtp join red_territory rt on rtp.territory_id=rt.id ");
			sb.append(" left join user_info ui on rtp.lords_user_code=ui.user_code ");
			sb.append(" left join org_info oi on oi.org_id=rtp.lords_org_id ");

			List<String> arrayList = new ArrayList<>();
			//地区
			if(StringUtils.isNotBlank(baseInfo.getProvinceName())){
				arrayList.add(" rt.province_name = #{baseInfo.provinceName} ");
			}
			if(StringUtils.isNotBlank(baseInfo.getCityName())){
				arrayList.add(" rt.city_name = #{baseInfo.cityName} ");
			}
			if(StringUtils.isNotBlank(baseInfo.getRegionName())){
				arrayList.add(" rt.region_name = #{baseInfo.regionName} ");
			}
			if(StringUtils.isNotBlank(baseInfo.getIsTrade())){
				arrayList.add(" rt.is_trade = #{baseInfo.isTrade} ");
			}
			//lordsOrgId
			if(baseInfo.getLordsOrgId()!=null){
				arrayList.add(" oi.org_id = #{baseInfo.lordsOrgId} ");
			}

        	/*if(baseInfo.getUserId()!=null){
        		arrayList.add(" ui.user_id = #{baseInfo.userId} ");
        	}*/

			if(StringUtils.isNotBlank(baseInfo.getLordsUserCode())){
				arrayList.add(" rtp.lords_user_code = #{baseInfo.lordsUserCode} ");
			}


			if(StringUtils.isNotBlank(baseInfo.getPhone())){
				arrayList.add(" ui.phone = #{baseInfo.phone} ");
			}
			if(StringUtils.isNotBlank(baseInfo.getUserName())){
				arrayList.add(" ui.user_name = #{baseInfo.userName} ");
			}

			if(StringUtils.isNotBlank(baseInfo.getCreateTimeStart())){
				arrayList.add(" rtp.create_time >= #{baseInfo.createTimeStart} ");
			}
			if(StringUtils.isNotBlank(baseInfo.getCreateTimeEnd())){
				arrayList.add(" rtp.create_time <= #{baseInfo.createTimeEnd} ");
			}

			if (arrayList.size()>0) {
				for (int i = 0; i < arrayList.size(); i++) {
					if (i==0) {
						sb.append(" WHERE ");
						sb.append(arrayList.get(i));
					}else {
						sb.append(" AND ");
						sb.append(arrayList.get(i));
					}
				}
			}


			if(StringUtils.isNotBlank(baseInfo.getOderByCol())){
				sb.append(" order by " +baseInfo.getOderByCol());
				if(StringUtils.isNotBlank(baseInfo.getType())){
					sb.append(" "+baseInfo.getType());
				}
			}
			return sb.toString();
		}

		public void whereManorMoneySql(SQL sql,ManorMoney baseInfo){
			sql.FROM("red_territory_lords_profit rtlp");
			sql.LEFT_OUTER_JOIN("user_info ui on ui.user_code=rtlp.lords_user_code");
			sql.LEFT_OUTER_JOIN("org_info oi on oi.org_id=rtlp.lords_org_id");
			sql.LEFT_OUTER_JOIN("red_territory rt on rt.id=rtlp.territory_id");

			//省市区,领地id
			if(StringUtils.isNotBlank(baseInfo.getProvinceName())){
				sql.WHERE("rt.province_name = #{baseInfo.provinceName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getCityName())){
				sql.WHERE("rt.city_name = #{baseInfo.cityName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getRegionName())){
				sql.WHERE("rt.region_name = #{baseInfo.regionName}");
			}

			if(StringUtils.isNotBlank(baseInfo.getUserName())){
				sql.WHERE("ui.user_name = #{baseInfo.userName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getPhone())){
				sql.WHERE("ui.phone = #{baseInfo.phone}");
			}
			if(baseInfo.getLordsOrgId()!=null && baseInfo.getLordsOrgId()!=-1){
				sql.WHERE("oi.org_id = #{baseInfo.lordsOrgId}");
			}

			if(StringUtils.isNotBlank(baseInfo.getLordsUserCode())){
				sql.WHERE("rtlp.lords_user_code = #{baseInfo.lordsUserCode}");
			}
		}

		public void whereSql(SQL sql, RedTerritoryBonusEveryday baseInfo){
			sql.FROM("red_territory_bonus_everyday rtbe");
			sql.LEFT_OUTER_JOIN("user_info ui on ui.user_code=rtbe.user_code");
			sql.LEFT_OUTER_JOIN("org_info oi on oi.org_id=rtbe.org_id");
			if(StringUtils.isNotBlank(baseInfo.getUserName())){
				sql.WHERE("ui.user_name = #{baseInfo.userName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getPhone())){
				sql.WHERE("ui.phone = #{baseInfo.phone}");
			}
			if(StringUtils.isNotBlank(baseInfo.getUserCode())){
				sql.WHERE("ui.user_code = #{baseInfo.userCode}");
			}
			if(baseInfo.getOrgId()!=-1){
				sql.WHERE("rtbe.org_id = #{baseInfo.orgId}");
			}
			//省市区
			if(StringUtils.isNotBlank(baseInfo.getProvinceName())){
				sql.WHERE("rtbe.province_name = #{baseInfo.provinceName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getCityName())){
				sql.WHERE("rtbe.city_name = #{baseInfo.cityName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getRegionName())){
				sql.WHERE("rtbe.region_name = #{baseInfo.regionName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getProfitDateStart())){
				sql.WHERE("rtbe.profit_date >= #{baseInfo.profitDateStart}");
			}
			if(StringUtils.isNotBlank(baseInfo.getProfitDateEnd())){
				sql.WHERE("rtbe.profit_date <= #{baseInfo.profitDateEnd}");
			}
			if(!(null==baseInfo.getStatus())){
				sql.WHERE("rtbe.status = #{baseInfo.status}");
			}
		}


		public String selectOrderSum(Map<String, Object> param){
			final RedTerritoryBonusEveryday baseInfo = (RedTerritoryBonusEveryday)param.get("baseInfo");
			SQL sql = new SQL(){{
				SELECT("sum(rtbe.profit_amount) as totalBonusSum");
				SELECT("sum(rtbe.business_bonus_amount) as totalBusinessSum");
				SELECT("sum(rtbe.premium_trade_amount) as totalPremiumSum");
				SELECT("sum(rtbe.random_bonus_amount) as totalRandomSum");
				SELECT("sum(rtbe.business_bonus_count) as businessSum");
				SELECT("sum(rtbe.business_basic_bonus_amount) as totalBasicBusinessSum");
			}};
			whereSql(sql, baseInfo);
			return sql.toString();
		}

		public String selectManorQueryPage(Map<String, Object> param){
			ManorQuery baseInfo = (ManorQuery)param.get("baseInfo");

			StringBuffer sb = new StringBuffer();

			sb.append("select rt.id,rt.pay_date,rt.pay_price,rt.province_name,rt.city_name,rt.region_name,ui.user_id,ui.user_name,ui.nick_name,ui.phone,oi.org_name,rt.is_trade,rtlp.total_profit,rt.trade_price,rt.lords_user_code"
					+ " from red_territory rt left join red_territory_lords_profit rtlp on rtlp.id=rt.profit_id ");
			sb.append(" left join user_info ui on rt.lords_user_code=ui.user_code ");
			sb.append(" left join org_info oi on oi.org_id=rt.lords_org_id ");

			List<String> arrayList = new ArrayList<>();
			//地区
			if(StringUtils.isNotBlank(baseInfo.getProvinceName())){
				arrayList.add(" rt.province_name = #{baseInfo.provinceName} ");
			}
			if(StringUtils.isNotBlank(baseInfo.getCityName())){
				arrayList.add(" rt.city_name = #{baseInfo.cityName} ");
			}
			if(StringUtils.isNotBlank(baseInfo.getRegionName())){
				arrayList.add(" rt.region_name = #{baseInfo.regionName} ");
			}
			if(StringUtils.isNotBlank(baseInfo.getIsTrade())){
				arrayList.add(" rt.is_trade = #{baseInfo.isTrade} ");
			}
			//lordsOrgId
			if(baseInfo.getLordsOrgId()!=null){
				arrayList.add(" oi.org_id = #{baseInfo.lordsOrgId} ");
			}

        	/*if(baseInfo.getUserId()!=null){
        		arrayList.add(" ui.user_id = #{baseInfo.userId} ");
        	}*/

			if(StringUtils.isNotBlank(baseInfo.getLordsUserCode())){
				arrayList.add(" rt.lords_user_code = #{baseInfo.lordsUserCode} ");
			}

			//lords_user_code
			if(StringUtils.isNotBlank(baseInfo.getPhone())){
				arrayList.add(" ui.phone = #{baseInfo.phone} ");
			}
			if(StringUtils.isNotBlank(baseInfo.getUserName())){
				arrayList.add(" ui.user_name = #{baseInfo.userName} ");
			}

			if (arrayList.size()>0) {
				for (int i = 0; i < arrayList.size(); i++) {
					if (i==0) {
						sb.append(" WHERE ");
						sb.append(arrayList.get(i));
					}else {
						sb.append(" AND ");
						sb.append(arrayList.get(i));
					}
				}
			}


			if(StringUtils.isNotBlank(baseInfo.getOderByCol())){
				sb.append(" order by " +baseInfo.getOderByCol());
				if(StringUtils.isNotBlank(baseInfo.getType())){
					sb.append(" "+baseInfo.getType());
				}
			}
			return sb.toString();
		}
	}

	@SelectProvider(type=SqlProvider.class, method="selectManorMoneyPage")
	@ResultType(ManorMoney.class)
	List<ManorMoney> selectManorMoneyPage(@Param("baseInfo")ManorMoney baseInfo, @Param("page")Page<ManorMoney> page);

	@SelectProvider(type=SqlProvider.class, method="selectManorMoneySum")
	@ResultType(ManorMainSum.class)
	ManorMainSum selectManorMoneySum(@Param("baseInfo")ManorMoney baseInfo);

	@SelectProvider(type=SqlProvider.class, method="selectManorMoneyDetlPage")
	@ResultType(ManorMoney.class)
	List<ManorMoney> selectManorMoneyDetlPage(@Param("baseInfo")ManorMoney baseInfo,  @Param("page")Page<ManorMoney> page);

	@SelectProvider(type=SqlProvider.class, method="selectDailyDividendPage")
	@ResultType(RedTerritoryBonusEveryday.class)
	List<RedTerritoryBonusEveryday> selectDailyDividendPage(@Param("baseInfo")RedTerritoryBonusEveryday baseInfo, @Param("page")Page<RedTerritoryBonusEveryday> page);

	@SelectProvider(type = SqlProvider.class, method = "selectOrderSum")
	@ResultType(ManorMainSum.class)
	ManorMainSum selectOrderSum(@Param("baseInfo") RedTerritoryBonusEveryday baseInfo);

	@SelectProvider(type=SqlProvider.class, method="selectDailyBusinessPage")
	@ResultType(RedTerritoryBonusDetail.class)
	List<RedTerritoryBonusDetail> selectDailyBusinessPage(@Param("list")List<Long> list);

	@SelectProvider(type=SqlProvider.class, method="selectManorTransactionRecorePage")
	@ResultType(ManorTransactionRecore.class)
	List<ManorTransactionRecore> selectManorTransactionRecorePage(@Param("baseInfo")ManorTransactionRecore baseInfo,
																  @Param("page")Page<ManorTransactionRecore> page);

	@SelectProvider(type=SqlProvider.class, method="selectManorTransactionRecorePage")
	@ResultType(ManorTransactionRecore.class)
	ManorTransactionRecore selectManorTransactionRecorePageSum(@Param("baseInfo")ManorTransactionRecore baseInfo);

	@SelectProvider(type=SqlProvider.class, method="selectManorTransactionRecorePageById")
	@ResultType(ManorTransactionRecore.class)
	ManorTransactionRecore selectManorTransactionRecorePageById(@Param("id")String id);

	@SelectProvider(type=SqlProvider.class, method="selectManorAdjustRecorePage")
	@ResultType(ManorAdjustRecore.class)
	List<ManorAdjustRecore> selectManorAdjustRecorePage(@Param("baseInfo")ManorAdjustRecore baseInfo,
														@Param("page")Page<ManorAdjustRecore> page);

	@SelectProvider(type=SqlProvider.class, method="selectManorQueryPage")
	@ResultType(ManorQuery.class)
	List<ManorQuery> selectManorQueryPage(@Param("baseInfo")ManorQuery baseInfo, @Param("page")Page<ManorQuery> page);

	@SelectProvider(type=SqlProvider.class, method="selectManorManager")
	@ResultType(ManorManager.class)
	ManorManager selectManorManager();

	@Insert("insert into red_territory_conf("
			+ "premium_territory,premium_profit,profit_member,profit_manager,"
			+ "profit_banker,premium_oem,territory_price,territory_red_profit,"
			+ "user_max_territory,member_max_territory,manager_max_territory,"
			+ "banker_max_territory,update_time,update_by,fenhong_total_money,fenhong_big_num,fenhong_big_rate," +
			"fenhong_begin_date,fenhong_end_date,premium_dividend,premium_begin_date,premium_end_date,red_profit," +
			"commissioner_profit,credit_card_profit,loan_profit_proportion,loan_profit,receipt_profit,insurance_profit," +
			"big_data_profit,pay_profit,card_payment_profit,benchmark_begin_date,benchmark_end_date,trade_fee,need_image_amount,fenhong_type,fenhong_proportion,fenhong_proportion_max)"
			+"values("
			+ "#{info.premiumTerritory},#{info.premiumProfit},#{info.profitMember},#{info.profitManager},"
			+ "#{info.profitBanker},#{info.premiumOem},#{info.territoryPrice},#{info.territoryRedProfit},"
			+ "#{info.userMaxTerritory},#{info.memberMaxTerritory},#{info.bankerMaxTerritory}#{info.updateTime},#{info.updateBy},#{info.fenhongTotalMoney},#{info.fenhongBigNum},#{info.fenhongBigRate}," +
			"#{info.fenhongBeginDateStr},#{info.fenhongEndDateStr},#{info.premiumDividend},#{info.premiumBeginDateStr},#{info.premiumEndDateStr},#{info.redProfit},"+
			"#{info.commissionerProfit},#{info.creditCardPprofit},#{info.loanProfitProportion},#{info.loanProfit},#{info.receiptProfit},#{info.insuranceProfit},"+
			"#{info.bigDataProfit},#{info.payProfit},#{info.cardPaymentProfit},#{info.benchmarkBeginDateStr},#{info.benchmarkEndDateStr},#{info.tradeFee},#{info.needImageAmount},#{info.fenhongType},#{info.fenhongProportion},#{info.fenhongProportionMax})"
	)
	void insertManorManager(@Param("info")ManorManager info);

	@Update("update red_territory_conf set "
			+ "premium_territory=#{info.premiumTerritory},premium_profit=#{info.premiumProfit},profit_member=#{info.profitMember},"
			+ "profit_manager=#{info.profitManager},profit_banker=#{info.profitBanker},premium_oem=#{info.premiumOem},"
			+ "territory_price=#{info.territoryPrice},territory_red_profit=#{info.territoryRedProfit},user_max_territory=#{info.userMaxTerritory},"
			+ "member_max_territory=#{info.memberMaxTerritory},manager_max_territory=#{info.managerMaxTerritory},banker_max_territory=#{info.bankerMaxTerritory},"
			+ "update_time=#{info.updateTime},update_by=#{info.updateBy},fenhong_total_money=#{info.fenhongTotalMoney},"
			+ "fenhong_big_num=#{info.fenhongBigNum},fenhong_big_rate=#{info.fenhongBigRate},fenhong_begin_date=#{info.fenhongBeginDateStr},"
			+ "fenhong_end_date=#{info.fenhongEndDateStr},premium_dividend=#{info.premiumDividend},premium_begin_date=#{info.premiumBeginDateStr},"
			+ "premium_end_date=#{info.premiumEndDateStr},red_profit=#{info.redProfit},commissioner_profit=#{info.commissionerProfit},"
			+ "credit_card_profit=#{info.creditCardProfit},loan_profit_proportion=#{info.loanProfitProportion},loan_profit=#{info.loanProfit},"
			+ "receipt_profit=#{info.receiptProfit},insurance_profit=#{info.insuranceProfit},big_data_profit=#{info.bigDataProfit},"
			+ "pay_profit=#{info.payProfit},card_payment_profit=#{info.cardPaymentProfit},benchmark_begin_date=#{info.benchmarkBeginDateStr},"
			+ "benchmark_end_date=#{info.benchmarkEndDateStr},trade_fee=#{info.tradeFee},need_image_amount=#{info.needImageAmount},"
			+" fenhong_type=#{info.fenhongType},fenhong_proportion=#{info.fenhongProportion},fenhong_proportion_max=#{info.fenhongProportionMax}")
	void updateManorManager(@Param("info")ManorManager info);
}
