package digit.enrichment;

import digit.service.UserService;
import digit.utils.IdgenUtil;
import digit.utils.UserUtil;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
@Component
@Slf4j
public class DeathApplicationEnrichment {

    @Autowired
    private IdgenUtil idgenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private UserUtil userUtils;

    public void enrichDeathApplication(DeathRegistrationRequest birthRegistrationRequest) {
        List<String> birthRegistrationIdList = idgenUtil.getIdList(birthRegistrationRequest.getRequestInfo(), birthRegistrationRequest.getDeathRegistrationApplications().get(0).getTenantId(), "btr.registrationid", "", birthRegistrationRequest.getDeathRegistrationApplications().size());
        Integer index = 0;
        for(DeathRegistrationApplication application : birthRegistrationRequest.getDeathRegistrationApplications()){
            // Enrich audit details
            AuditDetails auditDetails = AuditDetails.builder().createdBy(birthRegistrationRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(birthRegistrationRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
            application.setAuditDetails(auditDetails);

            // Enrich UUID
            application.setId(UUID.randomUUID().toString());

//            application.getFather().setId(application.getId());
//            application.getMother().setId(application.getId());

            // Enrich registration Id
            application.getAddress().setRegistrationId(application.getId());

            // Enrich address UUID
            application.getAddress().setId(UUID.randomUUID().toString());

            //Enrich application number from IDgen
            application.setApplicationNumber(birthRegistrationIdList.get(index++));

        }
    }

    public void enrichDeathApplicationUponUpdate(DeathRegistrationRequest birthRegistrationRequest) {
        // Enrich lastModifiedTime and lastModifiedBy in case of update
        birthRegistrationRequest.getDeathRegistrationApplications().get(0).getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
        birthRegistrationRequest.getDeathRegistrationApplications().get(0).getAuditDetails().setLastModifiedBy(birthRegistrationRequest.getRequestInfo().getUserInfo().getUuid());
    }

    public void enrichFatherApplicantOnSearch(DeathRegistrationApplication application) {
        UserDetailResponse fatherUserResponse = userService.searchUser(userUtils.getStateLevelTenant(application.getTenantId()),application.getFather().getId(),null);
        User fatherUser = fatherUserResponse.getUser().get(0);
        log.info(fatherUser.toString());
        FatherApplicant fatherApplicant = FatherApplicant.builder().aadhaarNumber(fatherUser.getAadhaarNumber())
                .accountLocked(fatherUser.getAccountLocked())
                .active(fatherUser.getActive())
                .altContactNumber(fatherUser.getAltContactNumber())
                .bloodGroup(fatherUser.getBloodGroup())
                .correspondenceAddress(fatherUser.getCorrespondenceAddress())
                .correspondenceCity(fatherUser.getCorrespondenceCity())
                .correspondencePincode(fatherUser.getCorrespondencePincode())
                .gender(fatherUser.getGender())
                .id(fatherUser.getUuid())
                .name(fatherUser.getName())
                .type(fatherUser.getType())
                .roles(fatherUser.getRoles()).build();
        application.setFather(fatherApplicant);
    }

    public void enrichMotherApplicantOnSearch(DeathRegistrationApplication application) {
        UserDetailResponse motherUserResponse = userService.searchUser(userUtils.getStateLevelTenant(application.getTenantId()),application.getFather().getId(),null);
        User motherUser = motherUserResponse.getUser().get(0);
        log.info(motherUser.toString());
        MotherApplicant motherApplicant = MotherApplicant.builder().aadhaarNumber(motherUser.getAadhaarNumber())
                .accountLocked(motherUser.getAccountLocked())
                .active(motherUser.getActive())
                .altContactNumber(motherUser.getAltContactNumber())
                .bloodGroup(motherUser.getBloodGroup())
                .correspondenceAddress(motherUser.getCorrespondenceAddress())
                .correspondenceCity(motherUser.getCorrespondenceCity())
                .correspondencePincode(motherUser.getCorrespondencePincode())
                .gender(motherUser.getGender())
                .id(motherUser.getUuid())
                .name(motherUser.getName())
                .type(motherUser.getType())
                .roles(motherUser.getRoles()).build();
        application.setMother(motherApplicant);
    }
}