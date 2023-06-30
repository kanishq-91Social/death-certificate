package digit.service;


import digit.enrichment.DeathApplicationEnrichment;
import digit.kafka.Producer;
import digit.repository.DeathRegistrationRepository;
import digit.validators.DeathApplicationValidator;
import digit.web.models.DeathApplicationSearchCriteria;
import digit.web.models.DeathRegistrationApplication;
import digit.web.models.DeathRegistrationRequest;
import digit.web.models.FatherApplicant;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class DeathRegistrationService {

    @Autowired
    private DeathApplicationValidator validator;

    @Autowired
    private DeathApplicationEnrichment enrichmentUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private DeathRegistrationRepository birthRegistrationRepository;

    @Autowired
    private Producer producer;

    public List<DeathRegistrationApplication> registerDtRequest(DeathRegistrationRequest birthRegistrationRequest) {
        // Validate applications
        validator.validateDeathApplication(birthRegistrationRequest);

        // Enrich applications
        enrichmentUtil.enrichDeathApplication(birthRegistrationRequest);

        // Enrich/Upsert user in upon birth registration
        userService.callUserService(birthRegistrationRequest);

        // Initiate workflow for the new application
        workflowService.updateWorkflowStatus(birthRegistrationRequest);

        // Push the application to the topic for persister to listen and persist
        producer.push("save-dt-application", birthRegistrationRequest);

        // Return the response back to user
        return birthRegistrationRequest.getDeathRegistrationApplications();
    }

    public List<DeathRegistrationApplication> searchDtApplications(RequestInfo requestInfo, DeathApplicationSearchCriteria birthApplicationSearchCriteria) {
        // Fetch applications from database according to the given search criteria
        List<DeathRegistrationApplication> applications = birthRegistrationRepository.getApplications(birthApplicationSearchCriteria);

        // If no applications are found matching the given criteria, return an empty list
        if(CollectionUtils.isEmpty(applications))
            return new ArrayList<>();

        // Enrich mother and father of applicant objects
        applications.forEach(application -> {
            enrichmentUtil.enrichFatherApplicantOnSearch(application);
            enrichmentUtil.enrichMotherApplicantOnSearch(application);
        });

        // Otherwise return the found applications
        return applications;
    }

    public DeathRegistrationApplication updateDtApplication(DeathRegistrationRequest birthRegistrationRequest) {
        // Validate whether the application that is being requested for update indeed exists
        DeathRegistrationApplication existingApplication = validator.validateApplicationExistence(birthRegistrationRequest.getDeathRegistrationApplications().get(0));
        existingApplication.setWorkflow(birthRegistrationRequest.getDeathRegistrationApplications().get(0).getWorkflow());
        log.info(existingApplication.toString());
        birthRegistrationRequest.setDeathRegistrationApplications(Collections.singletonList(existingApplication));

        // Enrich application upon update
        enrichmentUtil.enrichDeathApplicationUponUpdate(birthRegistrationRequest);

        workflowService.updateWorkflowStatus(birthRegistrationRequest);

        // Just like create request, update request will be handled asynchronously by the persister
        producer.push("update-dt-application", birthRegistrationRequest);

        return birthRegistrationRequest.getDeathRegistrationApplications().get(0);
    }
}
