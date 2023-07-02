package digit.repository.rowmapper;

import digit.web.models.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DeathApplicationRowMapper implements ResultSetExtractor<List<DeathRegistrationApplication>> {
    public List<DeathRegistrationApplication> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<String,DeathRegistrationApplication> deathRegistrationApplicationMap = new LinkedHashMap<>();

        while (rs.next()){
            String uuid = rs.getString("dregistrationno");
            DeathRegistrationApplication deathRegistrationApplication = deathRegistrationApplicationMap.get(uuid);

            if(deathRegistrationApplication == null) {

                Long lastModifiedTime = rs.getLong("dlastmodifiedtime");
                if (rs.wasNull()) {
                    lastModifiedTime = null;
                }



                AuditDetails auditdetails = AuditDetails.builder()
                        .createdBy(rs.getString("dcreatedby"))
                        .createdTime(rs.getLong("dcreatedtime"))
                        .lastModifiedBy(rs.getString("dlastmodifiedby"))
                        .lastModifiedTime(lastModifiedTime)
                        .build();

                deathRegistrationApplication = DeathRegistrationApplication.builder()
                        .registrationNumber(rs.getString("dregistrationno"))
                        .tenantId(rs.getString("dtenantid"))
                        .id(rs.getString("did"))
                        .deceasedFirstName(rs.getString("dfirstname"))
                        .deceasedMiddleName(rs.getString("dmiddlename"))
                        .deceasedLastName(rs.getString("dlastname"))
                        .placeOfDeath(rs.getString("dplaceOfDeath"))
                        .hospitalName(rs.getString("dhospitalname"))
                        .placeOfDeath(rs.getString("dplaceofdeath"))
                        .dateOfDeath(rs.getTimestamp("ddateofdeath").getTime())
                        .dateOfReport(rs.getTimestamp("ddateofreport").getTime())
                        .informantsName(rs.getString("dinformantsname"))
                        .informantsAddress(rs.getString("dinformantsaddress"))
                        .auditDetails(auditdetails)
                        .build();
            }
            addChildrenToProperty(rs, deathRegistrationApplication);
            deathRegistrationApplicationMap.put(uuid, deathRegistrationApplication);
        }
        return new ArrayList<>(deathRegistrationApplicationMap.values());
    }

    private void addChildrenToProperty(ResultSet rs, DeathRegistrationApplication deathRegistrationApplication)
            throws SQLException {
        addAddressToApplication(rs, deathRegistrationApplication);
    }

    private void addAddressToApplication(ResultSet rs, DeathRegistrationApplication deathRegistrationApplication) throws SQLException {
        Address address = Address.builder()
                .id(rs.getString("aid"))
                .tenantId(rs.getString("atenantid"))
                .buildingName(rs.getString("abuildingname"))
                .houseNumber(rs.getString("ahouseno"))
                .streetName(rs.getString("astreetname"))
                .locality(rs.getString("alocality"))
                .tehsil(rs.getString("atehsil"))
                .district(rs.getString("adistrict"))
                .city(rs.getString("acity"))
                .state(rs.getString("astate"))
                .pinno(rs.getString("apinno"))
                .country(rs.getString("acountry"))
                .streetName(rs.getString("astreetname"))
                .city(rs.getString("acity"))
                .createdBy(rs.getString("acreatedby"))
                .createdTime(rs.getLong("acreatedtime"))
                .lastModifiedBy(rs.getString("alastmodifiedby"))
                .lastModifiedTime(rs.getLong("alastmodifiedtime"))
                .registrationId("deathdtlid")
                .build();

        deathRegistrationApplication.setAddressOfDeceased(address);

    }

}
