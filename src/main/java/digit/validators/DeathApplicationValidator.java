package digit.validators;

import digit.repository.DeathRegistrationRepository;
import digit.web.models.DeathApplicationSearchCriteria;
import digit.web.models.DeathRegistrationApplication;
import digit.web.models.DeathRegistrationRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class DeathApplicationValidator {

    @Autowired
    private DeathRegistrationRepository repository;

    public void validateDeathApplication(DeathRegistrationRequest birthRegistrationRequest) {
        birthRegistrationRequest.getDeathRegistrationApplications().forEach(application -> {
            if(ObjectUtils.isEmpty(application.getTenantId()))
                throw new CustomException("EG_BT_APP_ERR", "tenantId is mandatory for creating birth registration applications");
        });
    }

    public DeathRegistrationApplication validateApplicationExistence(DeathRegistrationApplication birthRegistrationApplication) {
        return repository.getApplications(DeathApplicationSearchCriteria.builder().applicationNumber(birthRegistrationApplication.getApplicationNumber()).build()).get(0);
    }
}