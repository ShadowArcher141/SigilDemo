package com.demoSpring.demo;

import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SigilController {

    @Autowired
    private SigilService sigilService;

    private List<String> keywordsShort = List.of("", "O", "Et", "L", "P", "A", "Di", "Sp", "Li", "C", "W", "E", "F",
            "Ai");

    private static final Map<String, String> existingSigils = Map.ofEntries(
            Map.entry("Object", "Object"),
            Map.entry("Ether", "Ether"),
            Map.entry("Life", "Life"),
            Map.entry("Proliferation", "Proliferation"),
            Map.entry("Atrophy", "Atrophy"),
            Map.entry("Division", "Division"),
            Map.entry("Space", "Space"),
            Map.entry("Light", "Light"),
            Map.entry("Connection", "Connection"),
            Map.entry("Water", "Water"),
            Map.entry("Earth", "Earth"),
            Map.entry("Fire", "Fire"),
            Map.entry("Air", "Air"),
            Map.entry("Life Proliferation", "Health"),
            Map.entry("Life Water", "Aquatic"),
            Map.entry("Life Earth", "Plant"),
            Map.entry("Life Air", "Avian"),
            Map.entry("Life Fire", "Animal"),
            Map.entry("Proliferation Life", "Growth"),
            Map.entry("Water Earth", "Erosion"),
            Map.entry("Water Fire", "Steam"),
            Map.entry("Water Air", "Rain"),
            Map.entry("Earth Water", "Mud"),
            Map.entry("Earth Fire", "Lava"),
            Map.entry("Earth Air", "Sand"),
            Map.entry("Fire Water", "Explosion"),
            Map.entry("Fire Earth", "Cinder"),
            Map.entry("Fire Air", "Combustion"),
            Map.entry("Air Water", "Cloud"),
            Map.entry("Air Earth", "Loess"),
            Map.entry("Air Fire", "Smoke"),
            Map.entry("Space Light", "Motion"),
            Map.entry("Space Light Life", "Animate"),
            Map.entry("Space Light Atrophy", "Slow"),
            Map.entry("Space Light Time", "Acceleration"),
            Map.entry("Space Light Time Connection", "Nexus"),
            Map.entry("Space Light Time Connection Division", "Teleport"));

    @GetMapping("/")
    public String viewSigil(Model model) {
        clearModel(model);
        return "home.html";
    }

    @GetMapping("/sigil")
    public String viewSigil(@RequestParam String sequence, Model model) {

        List<Sigil> sigilList = new ArrayList<>();

        if (sequence == null || sequence.isEmpty()) {
            clearModel(model);
            return "home.html";
        }
        model.addAttribute("sequence", sequence);
        model.addAttribute("prevSequence", getPreviousSequence(sequence));

        List<String> sequenceParts = splitSequence(sequence); // ["L", "F"] → ["Life", "Fire"]
        List<String> listLongNames = sequenceParts.stream()
                .map(this::shortToLongName)
                .collect(Collectors.toList());

        String sigilSequenceLong = convertToLongName(listLongNames); // ["Life", "Fire"] → "Life Fire"
        model.addAttribute("sigilSequenceLong", sigilSequenceLong);

        String sigilName = lookupSigilSequence(sigilSequenceLong);

        if (sigilName != null) {
            Optional<Sigil> sigilOpt = sigilService.findByName(sigilName);
            if (sigilOpt.isPresent()) {
                model.addAttribute("sigil", sigilOpt.get());
            }
        } else {
            model.addAttribute("error", "No matching sigil found for sequence: " + sequence);
        }

        for (String shortName : sequenceParts) {
            String longName = shortToLongName(shortName);
            if (existingSigils.containsKey(longName)) {
                if (sigilService.findByName(longName).isPresent()) {
                    sigilList.add(sigilService.findByName(longName).get());
                }
            }
        }
        toggleButtons(model, sigilList);

        return "sigil-view"; // thymeleaf template
    }

    private void toggleButtons(Model model, List<Sigil> sigilList) {

        model.addAttribute("showObjectButton", true);
        model.addAttribute("showEtherButton", true);
        model.addAttribute("showLifeButton", true);
        model.addAttribute("showProliferationButton", true);
        model.addAttribute("showAtrophyButton", true);
        model.addAttribute("showDivisionButton", true);
        model.addAttribute("showSpaceButton", true);
        model.addAttribute("showLightButton", true);
        model.addAttribute("showConnectionButton", true);
        model.addAttribute("showWaterButton", true);
        model.addAttribute("showEarthButton", true);
        model.addAttribute("showFireButton", true);
        model.addAttribute("showAirButton", true);

        for (Sigil sigil : sigilList) {
            if (sigil.getName().equals("Object")) {
                model.addAttribute("showObjectButton", false);

            }
            if (sigil.getName().equals("Ether")) {
                model.addAttribute("showEtherButton", false);
            }
            if (sigil.getName().equals("Life")) {
                model.addAttribute("showLifeButton", false);
            }
            if (sigil.getName().equals("Proliferation")) {
                model.addAttribute("showProliferationButton", false);
            }
            if (sigil.getName().equals("Atrophy")) {
                model.addAttribute("showAtrophyButton", false);
            }
            if (sigil.getName().equals("Division")) {
                model.addAttribute("showDivisionButton", false);
            }
            if (sigil.getName().equals("Space")) {
                model.addAttribute("showSpaceButton", false);
            }
            if (sigil.getName().equals("Light")) {
                model.addAttribute("showLightButton", false);
            }
            if (sigil.getName().equals("Connection")) {
                model.addAttribute("showConnectionButton", false);
            }
            if (sigil.getName().equals("Water")) {
                model.addAttribute("showWaterButton", false);
            }
            if (sigil.getName().equals("Earth")) {
                model.addAttribute("showEarthButton", false);
            }
            if (sigil.getName().equals("Fire")) {
                model.addAttribute("showFireButton", false);
            }
            if (sigil.getName().equals("Air")) {
                model.addAttribute("showAirButton", false);
            }
        }
    }

    private void clearModel(Model model) {
        model.addAttribute("sequence", "");
        model.addAttribute("sigils", new ArrayList<>());
        model.addAttribute("sigil", "");
        model.addAttribute("prevSequence", "");
        toggleButtons(model, new ArrayList<>());
    }

    private String getPreviousSequence(String sequence) {
        // If sequence has only one character, remove one character
        if (sequence.length() == 1) {
            return sequence.substring(0, sequence.length() - 1);
        }

        // For sequences with two letters (e.g., "AP"), remove two characters
        String lastSigil = shortToLongName(sequence.substring(sequence.length() - 2));

        // Check if the last sigil is valid (exists in your existing sigils)
        if (existingSigils.containsKey(lastSigil)) {
            // Remove the last two characters from the sequence
            return sequence.substring(0, sequence.length() - 2);
            // return sequence.substring(sequence.length() - 2); // Return the last two
            // characters
        } else {
            // Handle cases where sigil is only one character long
            return sequence.substring(0, sequence.length() - 1); // Default case
        }
    }

    private List<String> splitSequence(String sequence) {
        List<String> parts = new ArrayList<>();
        int i = 0;

        while (i < sequence.length()) {
            // Try 2-char match first
            if (i + 2 <= sequence.length() && keywordsShort.contains(sequence.substring(i, i + 2))) {
                parts.add(sequence.substring(i, i + 2));
                i += 2;
            }
            // Fallback to 1-char
            else if (keywordsShort.contains(sequence.substring(i, i + 1))) {
                parts.add(sequence.substring(i, i + 1));
                i += 1;
            } else {
                // unknown token
                throw new IllegalArgumentException("Unknown sigil short name in sequence: " + sequence);
            }
        }

        return parts;
    }

    private String shortToLongName(String shortName) {
        Map<String, String> lookup = Map.ofEntries(
                Map.entry("O", "Object"),
                Map.entry("Et", "Ether"),
                Map.entry("L", "Life"),
                Map.entry("P", "Proliferation"),
                Map.entry("A", "Atrophy"),
                Map.entry("Di", "Division"),
                Map.entry("Sp", "Space"),
                Map.entry("Li", "Light"),
                Map.entry("C", "Connection"),
                Map.entry("W", "Water"),
                Map.entry("E", "Earth"),
                Map.entry("F", "Fire"),
                Map.entry("Ai", "Air"));
        return lookup.getOrDefault(shortName, "UNKNOWN");
    }

    private String convertToLongName(List<String> sigilSequence) {
        String returnSigil = "";
        for (String sigil : sigilSequence) {
            if (returnSigil.isEmpty()) {
                returnSigil = sigil;
            } else {
                returnSigil = returnSigil + " " + sigil;
            }
        }
        return returnSigil;
    }

    private String lookupSigilSequence(String sigilSequenceLong) {
        if (existingSigils.containsKey(sigilSequenceLong)) {
            return existingSigils.get(sigilSequenceLong);
        }
        return null;
    }

}
