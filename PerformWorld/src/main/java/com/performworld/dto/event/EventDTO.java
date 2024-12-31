package com.performworld.dto.event;


import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "event")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventDTO {

    @XmlElement(name = "genrenm")
    private String genreName;

    @XmlElement(name = "prfnm")
    private String title;

    @XmlElement(name = "prfpdfrom")
    private String prfpdfrom;

    @XmlElement(name = "prfpdto")
    private String prfpdto;

    @XmlElement(name = "prfcast")
    private String casting;

    @XmlElement(name = "area")
    private String location;

    @XmlElement(name = "prfruntime")
    private String runtime;

    @XmlElement(name = "poster")
    private String poster;

    @XmlElementWrapper(name = "styurls")
    @XmlElement(name = "styurl")
    private List<String> imageUrls;
}
