package org.dromara.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.dromara.bean.ChatEntity;
import org.dromara.service.IChatService;
import org.dromara.service.IDocumentService;
import org.dromara.utils.Result;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Resource
    private IChatService chatService;


    @PostMapping("/uploadRagDoc")
    public Result uploadRagDoc(@RequestParam("file") MultipartFile file) {
        List<Document> documents = documentService.loadText(file.getResource(), file.getOriginalFilename());
        return Result.ok(documents);
    }

    @GetMapping("/doSearch")
    public Result doSearch(@RequestParam String question) {
        return Result.ok(documentService.doSearch(question));
    }

    @PostMapping("/search")
    public void search(@RequestBody ChatEntity chatEntity, HttpServletResponse response) {
        List<Document> list = documentService.doSearch(chatEntity.getMessage());
        // 解决乱码
        response.setCharacterEncoding("UTF-8");
        chatService.doChatRagSearch(chatEntity, list);
    }


}
