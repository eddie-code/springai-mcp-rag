
    @Override
    public void doChatRagSearch(ChatEntity chatEntity, List<Document> ragContext) {
        // 获取当前用户标识、问题内容和机器人消息ID
        String userId = chatEntity.getCurrentUserName();
        String question = chatEntity.getMessage();
        String botMsgId = chatEntity.getBotMsgId();

        // 构建提示词上下文
        String context = null;
        if(ragContext!=null && !ragContext.isEmpty()){
            context = ragContext.stream()
                    .map(Document::getText)
                    .collect(Collectors.joining("\n"));
        }
        // 组装完整的提示词
        assert context != null;
        Prompt prompt = new Prompt(RAG_PROMPT
                .replace("{context}",context)
                .replace("{question}",question)
        );

        System.out.println(prompt.toString());
        
        // 发送提示词到AI模型并获取流式响应
        Flux<String> stringFlux = chatClient.prompt(prompt).stream().content();

        // 处理流式响应并将内容逐段发送给客户端
        List<String> list = stringFlux.toStream().map(chatResponse -> {
            String content = chatResponse.toString();
            SSEServer.sendMsg(userId, content, SSEMsgType.ADD);
            log.info("content:{}", content);
            return content;
        }).collect(Collectors.toList());

        // 拼接完整响应内容
        String fullContent = String.join("", list);
        // 可以保存到数据库

        // 构造最终响应实体并发送完成消息给客户端
        ChatResponseEntity chatResponseEntity = new  ChatResponseEntity(fullContent,botMsgId);

        SSEServer.sendMsg(userId, JSONUtil.toJsonStr(chatResponseEntity), SSEMsgType.FINISH);
    }
