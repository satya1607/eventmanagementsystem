package com.example.eventmanagementsystem.controller;

import com.example.eventmanagementsystem.config.ProjectSecurityConfig;
import com.example.eventmanagementsystem.entity.Speaker;
import com.example.eventmanagementsystem.repository.SpeakerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(SpeakerController.class)

class SpeakerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpeakerRepository speakerRepository;

    private Speaker speaker;

    @BeforeEach
    void setup() {
        speaker = new Speaker();
        speaker.setId("1");
        speaker.setName("John Doe");
        speaker.setBio("Java Development");
    }

    // ✅ Test GET /admin/speakers (view all)
    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void testViewSpeakers() throws Exception {
        when(speakerRepository.findAll()).thenReturn(Arrays.asList(speaker));

        mockMvc.perform(get("/admin/speakers"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/speakers/list"))
                .andExpect(model().attributeExists("speakers"));

        verify(speakerRepository, times(1)).findAll();
    }

    // ✅ Test GET /admin/speakers/create (show create form)
    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/admin/speakers/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/speakers/form"))
                .andExpect(model().attributeExists("speaker"));
    }

    // ✅ Test POST /admin/speakers/save (save new speaker)
    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void testSaveSpeaker() throws Exception {
        when(speakerRepository.save(any(Speaker.class))).thenReturn(speaker);

        mockMvc.perform(post("/admin/speakers/save")
        		        .with(csrf())
                        .flashAttr("speaker", speaker))
                       
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/speakers"));

        verify(speakerRepository, times(1)).save(any(Speaker.class));
    }

    // ✅ Test GET /admin/speakers/edit/{id} (edit existing speaker)
    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void testEditSpeakerFound() throws Exception {
        when(speakerRepository.findById("1")).thenReturn(Optional.of(speaker));

        mockMvc.perform(get("/admin/speakers/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/speakers/form"))
                .andExpect(model().attributeExists("speaker"));

        verify(speakerRepository, times(1)).findById("1");
    }

    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void testEditSpeakerNotFound() throws Exception {
        when(speakerRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/speakers/edit/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/speakers"));

        verify(speakerRepository, times(1)).findById("1");
    }

    // ✅ Test GET /admin/speakers/delete/{id}
    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void testDeleteSpeakerExists() throws Exception {
        when(speakerRepository.existsById("1")).thenReturn(true);
        doNothing().when(speakerRepository).deleteById("1");

        mockMvc.perform(get("/admin/speakers/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/speakers"));

        verify(speakerRepository, times(1)).deleteById("1");
    }

    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void testDeleteSpeakerNotFound() throws Exception {
        when(speakerRepository.existsById("1")).thenReturn(false);

        mockMvc.perform(get("/admin/speakers/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/speakers"));

        verify(speakerRepository, never()).deleteById("1");
    }
}

