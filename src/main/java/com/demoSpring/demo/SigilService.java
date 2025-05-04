package com.demoSpring.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

// import javax.annotation.PostConstruct;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class SigilService {
    private List<Sigil> sigils;

    @PostConstruct
    public void init() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Sigil>> typeRef = new TypeReference<>() {
        };
        InputStream inputStream = getClass().getResourceAsStream("/sigils.json");

        try {
            sigils = mapper.readValue(inputStream, typeRef);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load sigils.json", e);
        }
    }

    public Optional<Sigil> findByName(String name) {
        return sigils.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public List<Sigil> getSigils() {
        return sigils;
    }
}
