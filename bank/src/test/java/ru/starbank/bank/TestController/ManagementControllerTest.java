package ru.starbank.bank.TestController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.starbank.bank.controller.ManagementController;
import ru.starbank.bank.dto.InfoDTO;
import ru.starbank.bank.service.ManagementService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ManagementService service;

    @InjectMocks
    private ManagementController managementController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(managementController).build();

    }

    @Test
    public void testGetServiceInfo() throws Exception {
        InfoDTO infoDTO = new InfoDTO("Service Name", "1.0.0");
        when(service.getServiceInfo()).thenReturn(infoDTO);

        mockMvc.perform(get("/management/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Service Name"))
                .andExpect(jsonPath("$.version").value("1.0.0"));
    }

    @Test
    public void testClearAllCache() throws Exception {
        mockMvc.perform(post("/management/clear-caches"))
                .andExpect(status().isOk());

        verify(service).clearAllCaches();
    }

    @Test
    public void testGetServiceInfo_Success() throws Exception {
        InfoDTO info = new InfoDTO("Service Name", "1.0");
        when(service.getServiceInfo()).thenReturn(info);

        mockMvc.perform(get("/management/info"))
                .andExpect(status().isOk());
    }

}
