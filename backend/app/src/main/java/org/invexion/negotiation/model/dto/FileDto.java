package org.invexion.negotiation.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class FileDto implements Serializable {

    /*
        Mandatory field for fileDTO is only Multipart file.
     */

    private String title;
    private String description;
    @JsonIgnore
    private MultipartFile file;
    private String url;
    private Long size;
    private String filename;
    private String s3UUID;


}