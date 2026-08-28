package com.sougata.form_service.projection;

import java.time.Instant;
import java.util.UUID;

public interface FormSummaryProjection {

    UUID getId();

    String getName();

    Instant getLastOpenedOn();

}
