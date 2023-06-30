package digit.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import digit.service.DeathRegistrationService;
import digit.util.ResponseInfoFactory;
import digit.web.models.DeathRegistrationApplication;
import digit.web.models.DeathRegistrationRequest;
import digit.web.models.DeathRegistrationResponse;
import io.swagger.annotations.ApiParam;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.List;

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2023-06-28T15:48:46.730+05:30")

@Controller
@RequestMapping("/death-services")
public class V1ApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private DeathRegistrationService deathRegistrationService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    public V1ApiController(ObjectMapper objectMapper, HttpServletRequest request, DeathRegistrationService deathRegistrationService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.deathRegistrationService = deathRegistrationService;
    }

    @RequestMapping(value = "/v1/registration/_create", method = RequestMethod.POST)
    public ResponseEntity<DeathRegistrationResponse> v1RegistrationCreatePost(@ApiParam(value = "Details for the new Death Registration Application(s) + RequestInfo meta data.", required = true) @Valid @RequestBody DeathRegistrationRequest deathRegistrationRequest) {
        List<DeathRegistrationApplication> applications = deathRegistrationService.registerDtRequest(deathRegistrationRequest);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(deathRegistrationRequest.getRequestInfo(), true);
        DeathRegistrationResponse response = DeathRegistrationResponse.builder().deathRegistrationApplications(applications).responseInfo(responseInfo).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/registration/_search", method = RequestMethod.POST)
    public ResponseEntity<DeathRegistrationRequest> v1RegistrationSearchPost(
            @NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
            @ApiParam(value = "Parameter to carry Request metadata in the request body") @Valid @RequestBody RequestInfo requestInfo,
            @ApiParam(value = "Search based on status.") @Valid @RequestParam(value = "status", required = false) String status,
            @Size(max = 50) @ApiParam(value = "Unique identifier of death registration application") @Valid @RequestParam(value = "ids", required = false) List<Long> ids,
            @Size(min = 2, max = 64) @ApiParam(value = "Unique application number for the Death Registration Application") @Valid @RequestParam(value = "applicationNumber", required = false) String applicationNumber
    ) {
        List<DeathRegistrationApplication> applications = deathRegistrationService.searchDtApplications(requestInfoWrapper.getRequestInfo(), birthApplicationSearchCriteria);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true);
        BirthRegistrationResponse response = BirthRegistrationResponse.builder().birthRegistrationApplications(applications).responseInfo(responseInfo).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/registration/_update", method = RequestMethod.POST)
    public ResponseEntity<DeathRegistrationResponse> v1RegistrationUpdatePost(@ApiParam(value = "Details for the new (s) + RequestInfo meta data.", required = true) @Valid @RequestBody DeathRegistrationRequest deathRegistrationRequest) {
        DeathRegistrationApplication application = birthRegistrationService.updateBtApplication(birthRegistrationRequest);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(birthRegistrationRequest.getRequestInfo(), true);
        BirthRegistrationResponse response = BirthRegistrationResponse.builder().birthRegistrationApplications(Collections.singletonList(application)).responseInfo(responseInfo).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
