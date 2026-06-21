package com.happydent.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Returns static clinic data (services, reviews).
 * Move to a DB-backed table whenever content needs to be managed dynamically.
 */
@RestController
@RequestMapping("/api")
public class StaticDataController {

    @GetMapping("/services")
    public ResponseEntity<List<Map<String, String>>> services() {
        return ResponseEntity.ok(List.of(
            Map.of("icon","🦷","title","Check-up & Cleaning","desc","Comprehensive oral exam with professional scaling and polishing.","bg","#E0F7FA"),
            Map.of("icon","🔬","title","Root Canal Treatment","desc","Advanced therapy to save infected teeth with minimal discomfort.","bg","#E8F5E9"),
            Map.of("icon","✨","title","Teeth Whitening","desc","Professional bleaching for a noticeably brighter, whiter smile.","bg","#FFF8E1"),
            Map.of("icon","🦿","title","Dental Implants","desc","Permanent tooth replacement that looks and feels completely natural.","bg","#F3E5F5"),
            Map.of("icon","📐","title","Orthodontics","desc","Braces and clear aligners for a perfect, confident smile.","bg","#E3F2FD"),
            Map.of("icon","💎","title","Smile Makeover","desc","Veneers, whitening, and contouring for your dream smile.","bg","#FCE4EC"),
            Map.of("icon","👶","title","Pediatric Dentistry","desc","Gentle, child-friendly care that builds positive dental habits.","bg","#FFF3E0"),
            Map.of("icon","🔧","title","Tooth Extraction","desc","Safe removal of problematic and wisdom teeth with expert aftercare.","bg","#E8EAF6"),
            Map.of("icon","👑","title","Crowns & Bridges","desc","Quality porcelain restorations that look and function naturally.","bg","#E0F2F1"),
            Map.of("icon","🩺","title","Gum Treatment","desc","Modern diagnosis and treatment of gum disease for lasting oral health.","bg","#FFEBEE")
        ));
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<Map<String, Object>>> reviews() {
        return ResponseEntity.ok(List.of(
            Map.of("name","Kavitha R.","r",5,"date","March 2024","t","Root Canal","c","#1B9AAA",
                   "txt","\"Completely painless and the staff made me feel at ease. Highly recommend!\""),
            Map.of("name","Srinivasan K.","r",5,"date","January 2024","t","Dental Implants","c","#0B2545",
                   "txt","\"Exceptional service. Implants look and feel natural. Spotlessly clean clinic.\""),
            Map.of("name","Priya L.","r",5,"date","February 2024","t","Smile Makeover","c","#C9953A",
                   "txt","\"Absolutely thrilled with my smile makeover results. Incredible team!\""),
            Map.of("name","Anand M.","r",4,"date","December 2023","t","Braces","c","#6B46C1",
                   "txt","\"Great clinic with professional staff. The doctor follows up regularly.\""),
            Map.of("name","Deepa S.","r",5,"date","November 2023","t","Teeth Whitening","c","#B7791F",
                   "txt","\"Amazing whitening results — I can't stop smiling! Friendly staff.\""),
            Map.of("name","Rajesh P.","r",5,"date","October 2023","t","Check-up & Cleaning","c","#276749",
                   "txt","\"Best clinic in Urapakkam. Affordable and top-notch quality.\"")
        ));
    }
}
