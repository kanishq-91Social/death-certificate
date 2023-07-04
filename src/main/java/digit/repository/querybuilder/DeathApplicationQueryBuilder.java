package digit.repository.querybuilder;

import digit.web.models.DeathApplicationSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class DeathApplicationQueryBuilder {

    private static final String BASE_DTR_QUERY = " SELECT dtr.id as did, dtr.tenantid as dtenantid, dtr.registrationno as dregistrationno, dtr.firstname as dfirstname, dtr.middlename as dmiddlename, dtr.lastname as dlastname, dtr.dateofreport as ddateofreport, dtr.dateofdeath as ddateofdeath, dtr.hospitalname as dhospitalname, dtr.informantsname as dinformantsname, dtr.placeofdeath as dplaceofdeath, dtr.informantsaddress as dinformantsaddress, dtr.createdby as dcreatedby, dtr.lastmodifiedby as dlastmodifiedby, dtr.createdtime as dcreatedtime, dtr.lastmodifiedtime as dlastmodifiedtime, ";

    private static final String ADDRESS_SELECT_QUERY = " add.id as aid, add.tenantid as atenantid, add.buildingname as abuildingname, add.houseno as ahouseno, add.streetname as astreetname, add.locality as alocality, add.tehsil as atehsil, add.district as adistrict, add.city as acity, add.state as astate, add.pinno as pinno, add.country as acountry, add.createdby as acreatedby, add.createdtime as acreatedtime, add.lastmodifiedby as lastmodifiedby, add.lastmodifiedtime as alastmodifiedtime, add.deathregid as deathregid ";

    private static final String FROM_TABLES = " FROM eg_dt_registration dtr LEFT JOIN eg_dt_address add ON dtr.registrationno = add.deathregid ";

    private final String ORDERBY_CREATEDTIME = " ORDER BY dtr.createdtime DESC ";

    public String getDeathApplicationSearchQuery(DeathApplicationSearchCriteria criteria, List<Object> preparedStmtList){
        StringBuilder query = new StringBuilder(BASE_DTR_QUERY);
        query.append(ADDRESS_SELECT_QUERY);
        query.append(FROM_TABLES);

        if(!ObjectUtils.isEmpty(criteria.getTenantId())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" dtr.tenantid = ? ");
            preparedStmtList.add(criteria.getTenantId());
        }
        if(!ObjectUtils.isEmpty(criteria.getStatus())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" dtr.status = ? ");
            preparedStmtList.add(criteria.getStatus());
        }
        if(!CollectionUtils.isEmpty(criteria.getIds())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" dtr.id IN ( ").append(createQuery(criteria.getIds())).append(" ) ");
            addToPreparedStatement(preparedStmtList, criteria.getIds());
        }
        if(!ObjectUtils.isEmpty(criteria.getApplicationNumber())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" dtr.applicationnumber = ? ");
            preparedStmtList.add(criteria.getApplicationNumber());
        }

        // order death registration applications based on their createdtime in latest first manner
        query.append(ORDERBY_CREATEDTIME);

        return query.toString();
    }

    private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList){
        if(preparedStmtList.isEmpty()){
            query.append(" WHERE ");
        }else{
            query.append(" AND ");
        }
    }

    private String createQuery(List<String> ids) {
        StringBuilder builder = new StringBuilder();
        int length = ids.size();
        for (int i = 0; i < length; i++) {
            builder.append(" ?");
            if (i != length - 1)
                builder.append(",");
        }
        return builder.toString();
    }

    private void addToPreparedStatement(List<Object> preparedStmtList, List<String> ids) {
        ids.forEach(id -> {
            preparedStmtList.add(id);
        });
    }
}