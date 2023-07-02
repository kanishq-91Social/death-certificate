package digit.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * Representation of a address. Indiavidual APIs may choose to extend from this using allOf if more details needed to be added in their case. 
 */
@ApiModel(description = "Representation of a address. Indiavidual APIs may choose to extend from this using allOf if more details needed to be added in their case. ")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2023-06-28T15:48:46.730+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address   {

        @JsonProperty("id")
        private String id = null;

        @JsonProperty("tenantId")
        private String tenantId = null;

        @JsonProperty("buildingName")
        private String buildingName = null;

        @JsonProperty("houseNumber")
        private String houseNumber = null;

        @JsonProperty("streetName")
        private String streetName = null;

        @JsonProperty("locality")
        private String locality = null;

        @JsonProperty("tehsil")
        private String tehsil = null;

        @JsonProperty("district")
        private String district = null;

        @JsonProperty("city")
        private String city = null;

        @JsonProperty("state")
        private String state = null;

        @JsonProperty("pinno")
        private String pinno = null;

        @JsonProperty("country")
        private String country = null;

        @JsonProperty("createdBy")
        private String createdBy = null;

        @JsonProperty("createdTime")
        private Long createdTime = null;

        @JsonProperty("lastModifiedBy")
        private String lastModifiedBy = null;

        @JsonProperty("lastModifiedTime")
        private Long lastModifiedTime = null;

        @JsonProperty("registrationId")
        private String registrationId = null;

}

