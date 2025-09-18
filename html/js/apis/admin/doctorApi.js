window.doctorApi = {

    doChat: function(bo) {
        return instance({
            url: '/chat/deepSeek/doChat',
            method: 'post',
            data: bo
        })
    },

    ragSearch: function(bo) {
        return instance({
            url: '/rag/search',
            method: 'post',
            data: bo
        })
    },

    internetSearch: function(bo) {
        return instance({
            url: '/internet/search',
            method: 'post',
            data: bo
        })
    },

    getRecords: function(who) {
        return instance({
            url: '/ollama/getRecords?who=' + who,
            method: 'get',
        })
    },

    uploadRagDoc: function(formData) {
        return instance({
            url: '/rag/uploadRagDoc',
            method: 'post',
            data: formData,
            headers: {'Content-Type': 'multipart/form-data'}
        })
    },

}



