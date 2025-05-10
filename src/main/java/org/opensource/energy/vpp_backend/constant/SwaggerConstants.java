package org.opensource.energy.vpp_backend.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SwaggerConstants {
    public static final String VPP_API_VERSION = "v1";
    public static final String VPP_API_TITLE = "VPP Battery API";
    public static final String VPP_API_DESCRIPTION = "      The VPP API provides endpoints to manage and analyze distributed battery data \n" +
            "      within a Virtual Power Plant system. It enables clients to store battery metadata \n" +
            "      (e.g., name, postcode, watt capacity) and retrieve filtered statistics such as \n" +
            "      total, average, minimum, and maximum capacities within specified ranges.\n" +
            "      \n" +
            "      This API is designed for high-performance data ingestion and querying, with \n" +
            "      support for range filtering, validation, and error handling for real-time \n" +
            "      energy management applications.";
}
