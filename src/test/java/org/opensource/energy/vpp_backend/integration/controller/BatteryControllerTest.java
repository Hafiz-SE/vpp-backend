package org.opensource.energy.vpp_backend.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.opensource.energy.vpp_backend.VppBackendApplication;
import org.opensource.energy.vpp_backend.controller.BatteryController;
import org.opensource.energy.vpp_backend.dto.request.CreateBatteryRequest;
import org.opensource.energy.vpp_backend.dto.response.FilteredBatteryStat;
import org.opensource.energy.vpp_backend.service.BatteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = BatteryController.class, excludeAutoConfiguration = VppBackendApplication.class)
@ActiveProfiles("test")
@Tag("integration")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BatteryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BatteryService batteryService;

    @MockitoBean
    private AuditorAware<String> auditorAware;

    @Test
    void should_return_filtered_stats_successfully() throws Exception {
        FilteredBatteryStat mockStat = FilteredBatteryStat.builder()
                .batteryNames(List.of("Battery1", "Battery2"))
                .totalCapacity(300L)
                .averageCapacity(150L)
                .highestCapacityBatteryName("Battery2")
                .lowestCapacityBatteryName("Battery1")
                .totalCount(2L)
                .build();

        when(batteryService.getFilteredBatteryStat(1000, 2000, null, null)).thenReturn(mockStat);

        mockMvc.perform(get("/batteries")
                        .param("postcodeFrom", "1000")
                        .param("postcodeTo", "2000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCapacity").value(300))
                .andExpect(jsonPath("$.averageCapacity").value(150))
                .andExpect(jsonPath("$.batteryNames[0]").value("Battery1"))
                .andExpect(jsonPath("$.batteryNames[1]").value("Battery2"))
                .andExpect(jsonPath("$.totalCount").value(2))
                .andExpect(jsonPath("$.highestCapacityBatteryName").value("Battery2"))
                .andExpect(jsonPath("$.lowestCapacityBatteryName").value("Battery1"));
    }

    @Test
    void should_save_batteries_successfully() throws Exception {
        List<CreateBatteryRequest> requests = List.of(
                CreateBatteryRequest.builder().name("Battery1").postcode(1000).capacity(100L).build()
        );

        when(batteryService.saveBatteries(any())).thenReturn(List.of(1L));

        mockMvc.perform(post("/batteries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(1L));
    }

    @Test
    void should_return_415_when_content_type_is_missing_on_get() throws Exception {
        mockMvc.perform(get("/batteries")
                        .param("postcodeFrom", "1000")
                        .param("postcodeTo", "2000"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errorCode").value("UNSUPPORTED_MEDIA_TYPE"))
                .andExpect(jsonPath("$.path").value("/batteries"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errors[0].field").value("Content-Type"));
    }

    @Test
    void should_return_415_when_content_type_is_missing_on_post() throws Exception {
        List<CreateBatteryRequest> requests = List.of(
                CreateBatteryRequest.builder().name("Battery1").postcode(1000).capacity(100L).build()
        );

        mockMvc.perform(post("/batteries")
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errorCode").value("UNSUPPORTED_MEDIA_TYPE"))
                .andExpect(jsonPath("$.path").value("/batteries"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errors[0].field").value("Content-Type"));
    }

    @Test
    void should_return_400_when_postcode_to_is_missing() throws Exception {
        mockMvc.perform(get("/batteries")
                        .param("postcodeFrom", "1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_postcode_from_is_invalid() throws Exception {
        mockMvc.perform(get("/batteries")
                        .param("postcodeFrom", "abcd")
                        .param("postcodeTo", "2000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_both_postcode_params_are_missing() throws Exception {
        mockMvc.perform(get("/batteries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_200_with_empty_stats_when_no_data_found() throws Exception {
        when(batteryService.getFilteredBatteryStat(1000, 2000, null, null))
                .thenReturn(FilteredBatteryStat.builder()
                        .batteryNames(List.of())
                        .totalCapacity(0L)
                        .averageCapacity(0L)
                        .totalCount(0L)
                        .build());

        mockMvc.perform(get("/batteries")
                        .param("postcodeFrom", "1000")
                        .param("postcodeTo", "2000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batteryNames.length()").value(0))
                .andExpect(jsonPath("$.totalCapacity").value(0));
    }

    @Test
    void should_return_400_when_posting_empty_list() throws Exception {
        mockMvc.perform(post("/batteries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_required_fields_are_missing() throws Exception {
        String invalidJson = "[{}]";

        mockMvc.perform(post("/batteries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_capacity_is_negative() throws Exception {
        List<CreateBatteryRequest> requests = List.of(
                CreateBatteryRequest.builder().name("Battery1").postcode(1000).capacity(-50L).build()
        );

        mockMvc.perform(post("/batteries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isBadRequest());
    }
}
