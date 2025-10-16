package org.dromara.controller;

import org.dromara.service.IDocumentService;
import org.dromara.utils.Result;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lee
 * @description
 */
@RestController
@RequestMapping("/rag")
public class RagController {

    @Autowired
    private IDocumentService documentService;

    @PostMapping("/uploadRagDoc")
    public Result uploadRagDoc(@RequestParam("file") MultipartFile file) {
        Resource resource = file.getResource();
        String originalFilename = file.getOriginalFilename();
        List<Document> list = documentService.loadText(resource, originalFilename);
        return Result.ok(list);
    }

    @GetMapping("/doSearch")
    public Result doSearch(@RequestParam String question){
        return Result.ok(documentService.doSearch(question));
    }

}
